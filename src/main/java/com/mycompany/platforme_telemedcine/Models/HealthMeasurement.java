package com.mycompany.platforme_telemedcine.Models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class HealthMeasurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // tension | poids | glycemie
    @Column(nullable = false)
    private String type;

    private Integer systolique;
    private Integer diastolique;
    private Double valeur;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date = new Date();

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // getters & setters
    public Long getId() { return id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Integer getSystolique() { return systolique; }
    public void setSystolique(Integer systolique) { this.systolique = systolique; }

    public Integer getDiastolique() { return diastolique; }
    public void setDiastolique(Integer diastolique) { this.diastolique = diastolique; }

    public Double getValeur() { return valeur; }
    public void setValeur(Double valeur) { this.valeur = valeur; }

    public Date getDate() { return date; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
}
