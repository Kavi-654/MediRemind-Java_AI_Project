package com.mediremind.demo.controller;

import com.mediremind.demo.dto.DoseLogRequestDTO;
import com.mediremind.demo.dto.DoseLogResponseDTO;
import com.mediremind.demo.service.DoseLogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doselogs")
public class DoseLogController {

    @Autowired
    private DoseLogService doseLogService;

    // POST /api/doselogs/mark
    @PostMapping("/mark")
    public ResponseEntity<DoseLogResponseDTO> markDose(
            @Valid @RequestBody DoseLogRequestDTO dto) {
        DoseLogResponseDTO response = doseLogService.markDose(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/doselogs/patient/1
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<DoseLogResponseDTO>> getDoseHistory(
            @PathVariable Long patientId) {
        List<DoseLogResponseDTO> logs = doseLogService.getDoseHistory(patientId);
        return ResponseEntity.ok(logs);
    }
}
