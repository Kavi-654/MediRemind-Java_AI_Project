package com.mediremind.demo.repository;

import com.mediremind.demo.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine,Long> {

    List<Medicine> findByPatientId(Long patientId);

    List<Medicine> findByPatientIdAndEndDateGreaterThanEqual(Long patientId,java.time.LocalDate date);
}
