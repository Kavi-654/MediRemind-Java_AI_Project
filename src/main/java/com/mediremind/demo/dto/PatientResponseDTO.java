package com.mediremind.demo.dto;

import lombok.Data;
import java.time.LocalTime;

@Data
public class PatientResponseDTO {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalTime getBreakfastTime() {
        return breakfastTime;
    }

    public void setBreakfastTime(LocalTime breakfastTime) {
        this.breakfastTime = breakfastTime;
    }

    public LocalTime getLunchTime() {
        return lunchTime;
    }

    public void setLunchTime(LocalTime lunchTime) {
        this.lunchTime = lunchTime;
    }

    public LocalTime getDinnerTime() {
        return dinnerTime;
    }

    public void setDinnerTime(LocalTime dinnerTime) {
        this.dinnerTime = dinnerTime;
    }

    private Long id;
    private String name;
    private Integer age;
    private String email;
    private LocalTime breakfastTime;
    private LocalTime lunchTime;
    private LocalTime dinnerTime;
    // No password field — never expose passwords in responses!
