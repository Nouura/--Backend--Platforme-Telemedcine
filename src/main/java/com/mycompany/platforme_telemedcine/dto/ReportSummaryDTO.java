package com.mycompany.platforme_telemedcine.dto;

public class ReportSummaryDTO {
    public long totalConsultations;
    public double satisfactionRate; // %
    public long avgDurationMinutes; // si tu n’as pas la durée, on met 0
    public long activeDoctors;      // si tu n’as pas médecins ici, on met 0

    public ReportSummaryDTO(long totalConsultations, double satisfactionRate, long avgDurationMinutes, long activeDoctors) {
        this.totalConsultations = totalConsultations;
        this.satisfactionRate = satisfactionRate;
        this.avgDurationMinutes = avgDurationMinutes;
        this.activeDoctors = activeDoctors;
    }
}