package com.mediremind.demo.scheduler;

import com.mediremind.demo.model.Medicine;
import com.mediremind.demo.model.Patient;
import com.mediremind.demo.repository.PatientRepository;
import com.mediremind.demo.service.EmailService;
import com.mediremind.demo.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class ReminderScheduler {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private EmailService emailService;

    @Scheduled(fixedRate = 60000)
    public void sendMedicineReminders()
    {
        System.out.println("⏰ Scheduler running at: " + LocalTime.now());

        // Get current time (ignore seconds — match only hour and minute)
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);

        // Fetch all patients
        List<Patient> allPatients = patientRepository.findAll();

        for (Patient patient : allPatients) {

            // Skip if patient has no meal times set
            if (patient.getBreakfastTime() == null) continue;

            // Get all active medicines for this patient
            List<Medicine> medicines =
                    medicineService.getActiveMedicinesForPatient(patient.getId());

            for (Medicine medicine : medicines) {

                // Determine reminder times based on frequency
                List<LocalTime> reminderTimes =
                        getReminderTimes(medicine, patient);

                // Check if current time matches any reminder time
                for (LocalTime reminderTime : reminderTimes) {
                    if (now.equals(reminderTime)) {
                        // ✅ Send reminder email!
                        emailService.sendMedicineReminder(
                                patient.getEmail(),
                                patient.getName(),
                                medicine.getName(),
                                medicine.getDosage()
                        );
                        System.out.println("📧 Reminder sent to "
                                + patient.getName()
                                + " for " + medicine.getName());
                    }
                }
            }
        }

    }
    private List<LocalTime> getReminderTimes(Medicine medicine, Patient patient) {

        // Send reminder 15 minutes AFTER meal time
        // e.g. breakfast at 8:00 → reminder at 8:15
        LocalTime afterBreakfast = patient.getBreakfastTime().plusMinutes(15);
        LocalTime afterLunch     = patient.getLunchTime().plusMinutes(15);
        LocalTime afterDinner    = patient.getDinnerTime().plusMinutes(15);

        return switch (medicine.getFrequency()) {
            case ONCE_DAILY ->
                // Once daily → remind after breakfast only
                    List.of(afterBreakfast);

            case TWICE_DAILY ->
                // Twice daily → after breakfast and after dinner
                    List.of(afterBreakfast, afterDinner);

            case THRICE_DAILY ->
                // Three times daily → after every meal
                    List.of(afterBreakfast, afterLunch, afterDinner);
        };
    }
}
