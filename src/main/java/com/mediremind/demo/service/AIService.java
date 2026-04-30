package com.mediremind.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediremind.demo.dto.AIAnalysisResponseDTO;
import com.mediremind.demo.model.DoseLog;
import com.mediremind.demo.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AIService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DoseLogService doseLogService;

    @Autowired
    private PatientService patientService;

    // Reads values from application.properties
    @Value("${ai.groq.api-key}")
    private String groqApiKey;

    @Value("${ai.groq.api-url}")
    private String groqApiUrl;

    @Value("${ai.groq.model}")
    private String groqModel;

    // ObjectMapper converts between Java objects and JSON
    private final ObjectMapper objectMapper = new ObjectMapper();

  
    public AIAnalysisResponseDTO analyzeMissedDoses(Long patientId) {

        // Step 1: Get patient details
        Patient patient = patientService.getPatientEntityById(patientId);

        // Step 2: Get all missed doses for this patient
        List<DoseLog> missedDoses = doseLogService.getMissedDoses(patientId);

        // Step 3: If no missed doses, return early with a positive message
        if (missedDoses.isEmpty()) {
            return new AIAnalysisResponseDTO(
                    patient.getName(),
                    0,
                    "🎉 Excellent! " + patient.getName()
                            + " has not missed any doses. "
                            + "Keep up the great work! Consistency in medication"
                            + " leads to better health outcomes.",
                    LocalDateTime.now().format(
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            );
        }

        // Step 4: Build the prompt
        String prompt = buildMissedDosePrompt(patient, missedDoses);

        // Step 5: Call Groq API
        String aiResponse = callGroqAPI(prompt);

        // Step 6: Return the final response
        return new AIAnalysisResponseDTO(
                patient.getName(),
                missedDoses.size(),
                aiResponse,
                LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }

    // ============================================================
    // 🔧 Build the prompt from patient data and missed doses
    // ============================================================
    private String buildMissedDosePrompt(Patient patient,
                                         List<DoseLog> missedDoses) {

       
        Map<String, List<String>> missedByMedicine = missedDoses.stream()
                .collect(Collectors.groupingBy(
                        log -> log.getMedicine().getName()
                                + " (" + log.getMedicine().getDosage() + ")",
                        Collectors.mapping(
                                log -> log.getTakenDate().toString(),
                                Collectors.toList()
                        )
                ));

        // Build the missed doses section of the prompt
        StringBuilder missedDosesText = new StringBuilder();
        for (Map.Entry<String, List<String>> entry
                : missedByMedicine.entrySet()) {
            missedDosesText.append("- ")
                    .append(entry.getKey())      
                    .append(": missed on ")
                    .append(String.join(", ", entry.getValue()))  
                    .append("\n");
        }

       
        return """
                You are a helpful medical assistant AI. Analyze the following
                missed medicine doses for a patient and provide:
                1. A clear pattern analysis (which days/times are most missed)
                2. Practical tips to improve adherence
                3. A gentle health warning about the risks of missing these medicines
                4. An encouraging closing message

                Keep the response friendly, clear, and under 200 words.
                Do NOT recommend specific medicines or dosages.

                Patient Information:
                - Name: %s
                - Age: %s years old
                - Total missed doses: %d

                Missed Medicines:
                %s

                Please provide your analysis in a warm, supportive tone.
                """.formatted(
                patient.getName(),
                patient.getAge(),
                missedDoses.size(),
                missedDosesText.toString()
        );
    }

    // ============================================================
    // 🔧 Call Groq API and return the AI response text
    // ============================================================
    public String callGroqAPI(String prompt) {

        try {
        
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(groqApiKey);
          

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", groqModel);
            requestBody.put("max_tokens", 500);

         
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);

            requestBody.put("messages", List.of(userMessage));

          
            HttpEntity<Map<String, Object>> requestEntity =
                    new HttpEntity<>(requestBody, headers);

          
            ResponseEntity<String> response = restTemplate.exchange(
                    groqApiUrl,          
                    HttpMethod.POST,
                    requestEntity,
                    String.class         
            );

           
            return parseGroqResponse(response.getBody());

        } catch (Exception e) {
            System.err.println("❌ Groq API call failed: " + e.getMessage());
            return "AI analysis temporarily unavailable."
                    + " Please try again later.";

        }
    }

    // ============================================================
    // 🔧 Parse Groq API JSON response and extract the text
    // ============================================================
    private String parseGroqResponse(String responseBody) {
        try {


            JsonNode root = objectMapper.readTree(responseBody);
            return root
                    .path("choices")   // Go into "choices" array
                    .get(0)            // Get first element
                    .path("message")   // Go into "message" object
                    .path("content")   // Get "content" field
                    .asText();         // Convert to String

        } catch (Exception e) {
            System.err.println("❌ Failed to parse Groq response: "
                    + e.getMessage());
            return "Could not parse AI response. Please try again.";
        }
    }
}
