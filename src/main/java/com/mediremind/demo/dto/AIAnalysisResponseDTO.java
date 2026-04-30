package com.mediremind.demo.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AIAnalysisResponseDTO {
    private String patientName;       // Who this analysis is for
    private int totalMissedDoses;     // How many doses were missed in total
    private String analysis;          // AI generated health tips and pattern analysis
    private String generatedAt;

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getTotalMissedDoses() {
        return totalMissedDoses;
    }

    public void setTotalMissedDoses(int totalMissedDoses) {
        this.totalMissedDoses = totalMissedDoses;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(String generatedAt) {
        this.generatedAt = generatedAt;
    }

        // Timestamp of when this was generated
}
