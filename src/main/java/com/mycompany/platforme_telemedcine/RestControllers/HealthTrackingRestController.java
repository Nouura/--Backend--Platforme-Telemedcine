package com.mycompany.platforme_telemedcine.RestControllers;

import com.mycompany.platforme_telemedcine.Models.HealthMeasurement;
import com.mycompany.platforme_telemedcine.Models.Patient;
import com.mycompany.platforme_telemedcine.Services.HealthMeasurementService;
import com.mycompany.platforme_telemedcine.Services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@CrossOrigin(origins = "*")
public class HealthTrackingRestController {

    @Autowired
    private HealthMeasurementService healthService;

    @Autowired
    private PatientService patientService;

    // 🔹 GET mesures
    @GetMapping("/{patientId}/{type}")
    public ResponseEntity<List<HealthMeasurement>> getMeasurements(
            @PathVariable Long patientId,
            @PathVariable String type) {

        return ResponseEntity.ok(
                healthService.getByPatientAndType(patientId, type)
        );
    }

    // 🔹 POST nouvelle mesure
    @PostMapping("/{type}")
    public ResponseEntity<?> addMeasurement(
            @PathVariable String type,
            @RequestBody Map<String, Object> body) {

        Patient patient = patientService.getPatientById(
                Long.valueOf(body.get("patientId").toString())
        );

        if (patient == null) {
            return new ResponseEntity<>("Patient introuvable", HttpStatus.NOT_FOUND);
        }

        HealthMeasurement m = new HealthMeasurement();
        m.setType(type);
        m.setPatient(patient);

        if ("tension".equals(type)) {
            m.setSystolique(Integer.parseInt(body.get("systolique").toString()));
            m.setDiastolique(Integer.parseInt(body.get("diastolique").toString()));
        } else {
            m.setValeur(Double.parseDouble(body.get("valeur").toString()));
        }

        return new ResponseEntity<>(healthService.save(m), HttpStatus.CREATED);
    }
}
