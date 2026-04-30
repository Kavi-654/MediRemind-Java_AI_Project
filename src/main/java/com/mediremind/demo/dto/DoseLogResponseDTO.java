package com.mediremind.demo.dto;

import com.mediremind.demo.model.DoseStatus;

import java.time.LocalDate;

public class DoseLogResponseDTO {
    private Long id;
    private LocalDate takenDate;
    private DoseStatus status;
    private Long medicineId;
    private String medicineName;
    private String dosage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTakenDate() {
        return takenDate;
    }

    public void setTakenDate(LocalDate takenDate) {
        this.takenDate = takenDate;
    }

    public DoseStatus getStatus() {
        return status;
    }

    public void setStatus(DoseStatus status) {
        this.status = status;
    }

    public Long getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Long medicineId) {
        this.medicineId = medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }


}
