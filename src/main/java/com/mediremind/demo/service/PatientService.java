package com.mediremind.demo.service;

import com.mediremind.demo.dto.PatientRequestDTO;
import com.mediremind.demo.dto.PatientResponseDTO;
import com.mediremind.demo.model.Patient;
import com.mediremind.demo.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private EmailService emailService;

    public PatientResponseDTO registerPatient(PatientRequestDTO dto)
    {

        if(patientRepository.existsByEmail(dto.getEmail()))
        {
            throw new RuntimeException("Email already registered:"+dto.getEmail());
        }
        Patient patient=new Patient();
        patient.setName(dto.getName());
        patient.setAge(dto.getAge());
        patient.setEmail(dto.getEmail());
        patient.setPassword(dto.getPassword());   // In real apps, encrypt this with BCrypt
        patient.setBreakfastTime(dto.getBreakfastTime());
        patient.setLunchTime(dto.getLunchTime());
        patient.setDinnerTime(dto.getDinnerTime());

        Patient savedPatient=patientRepository.save(patient);
        emailService.sendWelcomeEmail(savedPatient.getEmail(), savedPatient.getName());
        return convertToResponseDTO(savedPatient);
    }

    public PatientResponseDTO getPatientById(Long id)
    {
        Patient patient=patientRepository.findById(id)
        .orElseThrow(()->new RuntimeException("Patient not found with id:"+id));
        return convertToResponseDTO(patient);
    }

    public PatientResponseDTO updateMealTimes(Long id,PatientRequestDTO dto)
    {
        Patient patient=patientRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Patient not found with id:"+id));

        patient.setBreakfastTime(dto.getBreakfastTime());
        patient.setLunchTime(dto.getLunchTime());
        patient.setDinnerTime(dto.getDinnerTime());

        Patient updatePatient=patientRepository.save(patient);
        return convertToResponseDTO(updatePatient);
    }

    public String deletePatient(Long id)
    {
        if(!patientRepository.existsById(id))
        {
            throw new RuntimeException("Patient not found with id"+id);
        }
        patientRepository.deleteById(id);
        return "Patient Deleted SuccessFully";
    }

    private PatientResponseDTO convertToResponseDTO(Patient patient)
    {
        PatientResponseDTO response=new PatientResponseDTO();
        response.setId(patient.getId());
        response.setName(patient.getName());
        response.setAge(patient.getAge());
        response.setEmail(patient.getEmail());
        response.setBreakfastTime(patient.getBreakfastTime());
        response.setLunchTime(patient.getLunchTime());
        response.setDinnerTime(patient.getDinnerTime());
        return response;
    }

    public Patient getPatientEntityById(Long id)
    {
        return patientRepository.findById(id).orElseThrow(()->new RuntimeException("Patient not found with id:"+id));
    }
}
