package com.mycompany.platforme_telemedcine.RestControllers;

import com.mycompany.platforme_telemedcine.Models.DossierMedical;
import com.mycompany.platforme_telemedcine.Models.Patient;
import com.mycompany.platforme_telemedcine.Services.ImpService.DossierMedicalServiceImp;
import com.mycompany.platforme_telemedcine.Services.ImpService.PatientServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/dossier-medical")
public class DossierMedicalController {

    @Autowired
    private DossierMedicalServiceImp dossierMedicalService;

    @Autowired
    private PatientServiceImp patientService;

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<DossierMedical>> getDossiersByPatient(@PathVariable Long patientId) {
        try {
            List<DossierMedical> dossiers = dossierMedicalService.getDossiersByPatientId(patientId);
            return ResponseEntity.ok(dossiers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<DossierMedical> getDossierMedical(@PathVariable Long id) {
        try {
            DossierMedical dossier = dossierMedicalService.getDossierMedicalById(id);
            if (dossier != null) {
                return ResponseEntity.ok(dossier);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(
            @RequestParam("patientId") Long patientId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description) {

        try {
            Patient patient = patientService.getPatientById(patientId);
            if (patient == null) {
                return ResponseEntity.badRequest().body("Patient non trouvé");
            }

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Fichier vide");
            }

            DossierMedical dossier = dossierMedicalService.uploadDocumentMedical(
                    patient, file, title, description);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Document uploadé avec succès");
            response.put("dossier", dossier);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur d'upload: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur: " + e.getMessage());
        }
    }


    @GetMapping("/files/{filename}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename) {
        try {
            System.out.println("=== DOWNLOAD REQUEST ===");
            System.out.println("Requested filename: " + filename);

            byte[] fileContent = dossierMedicalService.getDocumentFile(filename);

            if (fileContent == null || fileContent.length == 0) {
                System.out.println("File not found, listing available files...");

                // Debug: List all files in upload directory
                Path uploadPath = Paths.get(dossierMedicalService.getUploadDir());
                if (Files.exists(uploadPath)) {
                    try (Stream<Path> paths = Files.walk(uploadPath, 1)) {
                        System.out.println("Files in upload directory:");
                        paths.filter(Files::isRegularFile)
                                .forEach(path -> System.out.println("  - " + path.getFileName()));
                    }
                }

                return ResponseEntity.notFound().build();
            }

            HttpHeaders headers = new HttpHeaders();

            // Try to get the original filename from database
            String originalFilename = dossierMedicalService.getOriginalFilenameByGeneratedName(filename);
            if (originalFilename == null) {
                originalFilename = filename;
            }

            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", originalFilename);
            headers.setContentLength(fileContent.length);

            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println("Error in downloadFile: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    private void listAvailableFiles() {
        try {
            // Chemin de stockage (à adapter à votre configuration)
            Path uploadPath = Paths.get("uploads");
            if (Files.exists(uploadPath)) {
                System.out.println("Available files in uploads directory:");
                try (Stream<Path> paths = Files.walk(uploadPath)) {
                    paths.filter(Files::isRegularFile)
                            .forEach(path -> System.out.println("  - " + path.getFileName()));
                }
            } else {
                System.out.println("Uploads directory does not exist: " + uploadPath.toAbsolutePath());
            }
        } catch (Exception e) {
            System.out.println("Error listing files: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDossierMedical(@PathVariable Long id) {
        try {
            dossierMedicalService.deleteDossierMedical(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Document supprimé avec succès");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression: " + e.getMessage());
        }
    }


    @GetMapping("/patient/{patientId}/count")
    public ResponseEntity<Long> getDocumentCount(@PathVariable Long patientId) {
        try {
            long count = dossierMedicalService.getDocumentCountByPatient(patientId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}