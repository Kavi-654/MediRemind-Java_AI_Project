package com.mediremind.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="dose_logs")
public class DoseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate takenDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DoseStatus status;

    @ManyToOne
    @JoinColumn(name="medicine_id",nullable = false)
    private Medicine medicine;

}
