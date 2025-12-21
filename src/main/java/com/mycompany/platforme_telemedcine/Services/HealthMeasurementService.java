package com.mycompany.platforme_telemedcine.Services;

import com.mycompany.platforme_telemedcine.Models.HealthMeasurement;

import java.util.List;

public interface HealthMeasurementService {

    HealthMeasurement save(HealthMeasurement measurement);

    List<HealthMeasurement> getByPatientAndType(Long patientId, String type);
}
