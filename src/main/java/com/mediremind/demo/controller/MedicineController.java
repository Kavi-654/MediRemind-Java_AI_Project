package com.mediremind.demo.controller;

import com.mediremind.demo.dto.MedicineRequestDTO;
import com.mediremind.demo.dto.MedicineResponseDTO;
import com.mediremind.demo.service.MedicineService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    // POST /api/medicines
    @PostMapping
    public ResponseEntity<MedicineResponseDTO> addMedicine(
            @Valid @RequestBody MedicineRequestDTO dto) {
        MedicineResponseDTO response = medicineService.addMedicine(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/medicines/patient/1
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicineResponseDTO>> getMedicinesByPatient(
            @PathVariable Long patientId) {
        List<MedicineResponseDTO> medicines =
                medicineService.getMedicineByPatient(patientId);
        return ResponseEntity.ok(medicines);
    }

    // DELETE /api/medicines/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedicine(@PathVariable Long id) {
        String message = medicineService.deleteMedicine(id);
        return ResponseEntity.ok(message);
    }
}
