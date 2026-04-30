package com.mediremind.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="medicines")
public class Medicine {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    @Column(nullable = false)
    private String dosage;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @ManyToOne()
    @JoinColumn(name="patient_id",nullable = false)
    private Patient patient;

    @OneToMany(mappedBy = "medicine",cascade = CascadeType.ALL)
    private List<DoseLog> doselogs;



}
