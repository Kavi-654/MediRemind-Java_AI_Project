package com.mediremind.demo.controller;

import com.mediremind.demo.dto.AIAnalysisResponseDTO;
import com.mediremind.demo.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    // GET /api/ai/analyze/1
    @GetMapping("/analyze/{patientId}")
    public ResponseEntity<AIAnalysisResponseDTO> analyzeMissedDoses(
            @PathVariable Long patientId) {

        AIAnalysisResponseDTO response =
                aiService.analyzeMissedDoses(patientId);
        return ResponseEntity.ok(response);
    }
}
