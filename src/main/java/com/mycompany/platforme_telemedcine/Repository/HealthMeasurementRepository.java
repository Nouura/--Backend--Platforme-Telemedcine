package com.mycompany.platforme_telemedcine.Repository;

import com.mycompany.platforme_telemedcine.Models.HealthMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HealthMeasurementRepository extends JpaRepository<HealthMeasurement, Long> {

    List<HealthMeasurement> findByPatientIdAndTypeOrderByDateAsc(Long patientId, String type);
}
