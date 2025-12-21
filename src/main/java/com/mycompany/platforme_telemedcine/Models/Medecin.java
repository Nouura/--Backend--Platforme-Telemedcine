package com.mycompany.platforme_telemedcine.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity

public class Medecin extends User{
    private String specialte;
    private String disponibilite;

    @JsonIgnore
    @OneToMany(mappedBy = "medecin")
    private List<RendezVous> rendezVous;

    @ManyToOne
    @JoinColumn(name = "medecin_id") // Crée la clé étrangère dans la table consultation
    private Medecin medecin;

    @ManyToMany
    @JoinTable(
                name = "medecin_patients",
                joinColumns = @JoinColumn(name = "medecin_id"),
                inverseJoinColumns = @JoinColumn(name = "patient_id")
        )
    private List<Patient> patients = new ArrayList<>();

    public List<Patient> getPatients() {
        return patients;
    }
    public void setPatients(List<Patient> patients) {
            this.patients = patients;
        }

        public void addPatient(Patient patient) {
            if (!this.patients.contains(patient)) {
                this.patients.add(patient);
            }
        }

        public void removePatient(Patient patient) {
            this.patients.remove(patient);
        }

    public String getSpecialte() {
        return specialte;
    }

    public void setSpecialte(String specialte) {
        this.specialte = specialte;
    }

    public String getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(String disponibilite) {
        this.disponibilite = disponibilite;
    }


    public List<RendezVous> getRendezVous() {
        return rendezVous;
    }

    public void setRendezVous(List<RendezVous> rendezVous) {
        this.rendezVous = rendezVous;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }
}
