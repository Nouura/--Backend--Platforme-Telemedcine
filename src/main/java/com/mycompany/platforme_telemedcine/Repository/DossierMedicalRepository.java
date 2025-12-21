package com.mycompany.platforme_telemedcine.Repository;

import com.mycompany.platforme_telemedcine.Models.DossierMedical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DossierMedicalRepository extends JpaRepository<DossierMedical, Long> {

    // Only keep essential methods
    List<DossierMedical> findByPatientIdOrderByUploadDateDesc(Long patientId);

    // Simple count for patient
    long countByPatientId(Long patientId);
    Optional<DossierMedical> findByGeneratedFileName(String generatedFileName);
}

