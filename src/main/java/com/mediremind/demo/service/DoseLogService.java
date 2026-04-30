package com.mediremind.demo.service;

import com.mediremind.demo.dto.DoseLogRequestDTO;
import com.mediremind.demo.dto.DoseLogResponseDTO;
import com.mediremind.demo.model.DoseLog;
import com.mediremind.demo.model.Medicine;
import com.mediremind.demo.repository.DoseLogRepository;
import com.mediremind.demo.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoseLogService {

    @Autowired
    private DoseLogRepository doseLogRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    public DoseLogResponseDTO markDose(DoseLogRequestDTO dto)
    {
        Medicine medicine=medicineRepository.findById(dto.getMedicineId())
                .orElseThrow(()->new RuntimeException("Medicine not found with id:"+dto.getMedicineId()));
        if(dto.getTakenDate().isBefore(medicine.getStartDate()) ||
        dto.getTakenDate().isAfter(medicine.getEndDate()))
        {
            throw new RuntimeException("Date is outside the medicine's active period");

        }

        DoseLog doseLog=new DoseLog();
        doseLog.setTakenDate(dto.getTakenDate());
        doseLog.setStatus(dto.getStatus());
        doseLog.setMedicine(medicine);
        DoseLog savedLog=doseLogRepository.save(doseLog);
        return convertToResponseDTO(savedLog);
    }

    public List<DoseLogResponseDTO> getDoseHistory(Long patientId)
    {
        List<DoseLog> logs=doseLogRepository.findAllDosesByPatientId(patientId);

        return logs.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<DoseLog> getMissedDoses(Long patientId)
    {
        return doseLogRepository.findMissedDosesByPatientId(patientId);
    }

    private DoseLogResponseDTO convertToResponseDTO(DoseLog log)
    {
        DoseLogResponseDTO response = new DoseLogResponseDTO();
        response.setId(log.getId());
        response.setTakenDate(log.getTakenDate());
        response.setStatus(log.getStatus());
        response.setMedicineId(log.getMedicine().getId());
        response.setMedicineName(log.getMedicine().getName());
        response.setDosage(log.getMedicine().getDosage());
        return response;
    }

}
