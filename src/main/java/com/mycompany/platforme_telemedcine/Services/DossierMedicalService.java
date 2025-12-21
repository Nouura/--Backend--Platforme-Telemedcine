package com.mycompany.platforme_telemedcine.Services;

import com.mycompany.platforme_telemedcine.Models.DossierMedical;
import java.util.List;

public interface DossierMedicalService {
    DossierMedical createDossierMedical(DossierMedical dossierMedical);
    List<DossierMedical> getAllDossierMedical();
    DossierMedical getDossierMedicalById(Long id);
    void deleteDossierMedical(Long id);
    DossierMedical updateDossierMedical(DossierMedical dossierMedical);
    List<DossierMedical> getDossiersByPatientId(Long id);
    String getGeneratedFilename(Long id);
    String extractFilenameFromUrl(String url);
}