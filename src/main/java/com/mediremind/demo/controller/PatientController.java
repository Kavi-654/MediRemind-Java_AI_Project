package com.mediremind.demo.controller;

import com.mediremind.demo.dto.PatientRequestDTO;
import com.mediremind.demo.dto.PatientResponseDTO;
import com.mediremind.demo.service.EmailService;
import com.mediremind.demo.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController                     // Combines @Controller + @ResponseBody
@RequestMapping("/api/patients")    // All endpoints in this class start with /api/patients
public class PatientController {

    @Autowired
    private PatientService patientService;

    // POST /api/patients/register
    @PostMapping("/register")
    public ResponseEntity<PatientResponseDTO> registerPatient(
            @Valid @RequestBody PatientRequestDTO dto) {
        // @Valid triggers the validation annotations we added in DTO
        // @RequestBody converts incoming JSON → Java object automatically
        PatientResponseDTO response = patientService.registerPatient(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
        // Returns HTTP 201 Created with the patient data
    }

    // GET /api/patients/1
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> getPatient(@PathVariable Long id) {
        // @PathVariable extracts "1" from the URL and maps it to Long id
        PatientResponseDTO response = patientService.getPatientById(id);
        return ResponseEntity.ok(response);   // Returns HTTP 200 OK
    }

    // PUT /api/patients/1
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updateMealTimes(
            @PathVariable Long id,
            @RequestBody PatientRequestDTO dto) {
        PatientResponseDTO response = patientService.updateMealTimes(id, dto);
        return ResponseEntity.ok(response);
    }

    // DELETE /api/patients/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable Long id) {
        String message = patientService.deletePatient(id);
        return ResponseEntity.ok(message);
    }
    @Autowired
    private EmailService emailService;

    @GetMapping("/test-email/{patientId}")
    public ResponseEntity<String> testEmail(@PathVariable Long patientId) {
        PatientResponseDTO patient = patientService.getPatientById(patientId);
        emailService.sendMedicineReminder(
                patient.getEmail(),
                patient.getName(),
                "Paracetamol 500mg",
                "500mg"
        );
        return ResponseEntity.ok("Test email sent to " + patient.getEmail());
    }
}
