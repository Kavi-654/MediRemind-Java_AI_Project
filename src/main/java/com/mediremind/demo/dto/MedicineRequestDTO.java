package com.mediremind.demo.dto;

import com.mediremind.demo.model.Frequency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MedicineRequestDTO {

    @NotBlank(message = "Medicine name cannot be empty")
    private String name;

    @NotBlank(message = "Dosage cannot be empty")
    private String dosage;

    @NotNull(message = "Frequency cannot be empty")
    private Frequency frequency;       // Client sends "ONCE_DAILY", "TWICE_DAILY" etc.

    @NotNull(message = "Start date cannot be empty")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be empty")
    private LocalDate endDate;

    @NotNull(message = "Patient ID cannot be empty")
    private Long patientId;            // Which patient this medicine belongs to
}
