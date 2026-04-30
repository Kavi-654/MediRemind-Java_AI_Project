package com.mediremind.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class PatientRequestDTO {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Age cannot be empty")
    private Integer age;

    @Email(message = "Please provide a valid email")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    // Optional — can be updated later
    private LocalTime breakfastTime;
    private LocalTime lunchTime;
    private LocalTime dinnerTime;
}
