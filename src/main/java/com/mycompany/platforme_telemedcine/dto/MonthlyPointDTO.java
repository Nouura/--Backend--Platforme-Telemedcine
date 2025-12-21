package com.mycompany.platforme_telemedcine.dto;

public class MonthlyPointDTO {
    public String name;         // Jan, Feb...
    public long consultations;  // nb paiements payés du mois
    public double revenus;      // somme montant payés du mois

    public MonthlyPointDTO(String name, long consultations, double revenus) {
        this.name = name;
        this.consultations = consultations;
        this.revenus = revenus;
    }
}