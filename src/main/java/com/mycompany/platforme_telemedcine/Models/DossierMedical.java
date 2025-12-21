package com.mycompany.platforme_telemedcine.Models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dossier_medical")
public class DossierMedical {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Column(name = "title")
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

    @Column(name = "generated_file_name")
    private String generatedFileName;

    // Constructeurs
    public DossierMedical() {}

    public DossierMedical(Patient patient, String title, String description,
                          String fileName, String fileUrl) {
        this.patient = patient;
        this.title = title;
        this.description = description;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.generatedFileName = fileName;
        this.uploadDate = LocalDateTime.now();
    }

    // Getters and Setters (only essential ones)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public LocalDateTime getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }

    public String getGeneratedFileName() { return generatedFileName; }
    public void setGeneratedFileName(String generatedFileName) { this.generatedFileName = generatedFileName; }
}