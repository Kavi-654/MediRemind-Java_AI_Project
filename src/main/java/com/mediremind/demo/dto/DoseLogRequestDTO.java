package com.mediremind.demo.dto;

import com.mediremind.demo.model.DoseStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DoseLogRequestDTO {

    @NotNull(message = "Medicine ID cannot be empty")
    private Long medicineId;

    @NotNull(message="Date cannot be empty")
    private LocalDate takenDate;

    @NotNull(message="Status cannot be empty")
    private DoseStatus status;

}
