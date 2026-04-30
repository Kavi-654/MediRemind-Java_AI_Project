package com.mediremind.demo.repository;

import com.mediremind.demo.model.DoseLog;
import com.mediremind.demo.model.DoseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoseLogRepository extends JpaRepository<DoseLog, Long> {

    // Get all dose logs for a specific medicine
    List<DoseLog> findByMedicineId(Long medicineId);

    // Get all dose logs by status (TAKEN or MISSED) for a medicine
    List<DoseLog> findByMedicineIdAndStatus(Long medicineId, DoseStatus status);

   
    @Query("SELECT d FROM DoseLog d WHERE d.medicine.patient.id = :patientId AND d.status = 'MISSED'")
    List<DoseLog> findMissedDosesByPatientId(@Param("patientId") Long patientId);

    // Get all dose logs for a patient regardless of status
    @Query("SELECT d FROM DoseLog d WHERE d.medicine.patient.id = :patientId")
    List<DoseLog> findAllDosesByPatientId(@Param("patientId") Long patientId);
}
