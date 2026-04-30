package com.mediremind.demo.service;

import com.mediremind.demo.dto.MedicineRequestDTO;
import com.mediremind.demo.dto.MedicineResponseDTO;
import com.mediremind.demo.model.Medicine;
import com.mediremind.demo.model.Patient;
import com.mediremind.demo.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private PatientService patientService;

    public MedicineResponseDTO addMedicine(MedicineRequestDTO dto)
    {
        if(dto.getEndDate().isBefore(dto.getStartDate()))
        {
            throw new RuntimeException("End date cannot be before start date");
        }
        Patient patient=patientService.getPatientEntityById(dto.getPatientId());

        Medicine medicine=new Medicine();
        medicine.setName(dto.getName());
        medicine.setDosage(dto.getDosage());
        medicine.setFrequency(dto.getFrequency());
        medicine.setStartDate(dto.getStartDate());
        medicine.setEndDate(dto.getEndDate());
        medicine.setPatient(patient);

        Medicine savedMedicine=medicineRepository.save(medicine);
        return convertToResponseDTO(savedMedicine);

    }
    public List<MedicineResponseDTO> getMedicineByPatient(Long patientId)
    {
        patientService.getPatientEntityById(patientId);

        List<Medicine> medicines=medicineRepository.findByPatientId(patientId);

        return medicines.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public String deleteMedicine(Long id)
    {
        if(!medicineRepository.existsById(id))
        {
            throw new RuntimeException("Medicine not found with id:"+id);
        }
        medicineRepository.deleteById(id);
        return "Medicine deleted successfully";
    }
    private MedicineResponseDTO convertToResponseDTO(Medicine medicine) {
        MedicineResponseDTO response = new MedicineResponseDTO();
        response.setId(medicine.getId());
        response.setName(medicine.getName());
        response.setDosage(medicine.getDosage());
        response.setFrequency(medicine.getFrequency());
        response.setStartDate(medicine.getStartDate());
        response.setEndDate(medicine.getEndDate());
        response.setPatientId(medicine.getPatient().getId());
        response.setPatientName(medicine.getPatient().getName());
        return response;
    }

    // 🔧 Used by scheduler — get active medicines for a patient
    public List<Medicine> getActiveMedicinesForPatient(Long patientId) {
        return medicineRepository
                .findByPatientIdAndEndDateGreaterThanEqual(patientId, LocalDate.now());
    }
}
