package com.mycompany.platforme_telemedcine.Services.ImpService;

import com.mycompany.platforme_telemedcine.Models.HealthMeasurement;
import com.mycompany.platforme_telemedcine.Repository.HealthMeasurementRepository;
import com.mycompany.platforme_telemedcine.Services.HealthMeasurementService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthMeasurementServiceImpl implements HealthMeasurementService {

    private final HealthMeasurementRepository repository;

    // ✅ Injection par constructeur (BEST PRACTICE)
    public HealthMeasurementServiceImpl(HealthMeasurementRepository repository) {
        this.repository = repository;
    }

    @Override
    public HealthMeasurement save(HealthMeasurement measurement) {
        return repository.save(measurement);
    }

    @Override
    public List<HealthMeasurement> getByPatientAndType(Long patientId, String type) {
        return repository.findByPatientIdAndTypeOrderByDateAsc(patientId, type);
    }
}
