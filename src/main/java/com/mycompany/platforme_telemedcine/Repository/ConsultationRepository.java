package com.mycompany.platforme_telemedcine.Repository;

import com.mycompany.platforme_telemedcine.Models.Consultation;
import com.mycompany.platforme_telemedcine.Models.Ordonance;
import com.mycompany.platforme_telemedcine.Models.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    Consultation findConsultationById(long id);
    Consultation findConsultationByRendezVous(RendezVous rendezVous);
    Consultation findConsultationByOrdonance(Ordonance ordonance);

    // Utilisé pour monthly chart
    List<Consultation> findByDateBetween(Date start, Date end);

    // ✅ KPI présence: nombre de consultations dans une période (réel DB)
    long countByDateBetween(Date start, Date end);

    // Médecins actifs sur une période (Option B)
    @Query("SELECT COUNT(DISTINCT c.medecin.id) " +
            "FROM Consultation c " +
            "WHERE c.medecin IS NOT NULL AND c.date BETWEEN :start AND :end")
    long countDistinctActiveDoctorsBetween(@Param("start") Date start, @Param("end") Date end);

    // Répartition par spécialité (native SQL robuste)
    @Query(value =
            "SELECT COALESCE(NULLIF(TRIM(m.specialte), ''), 'Autres') AS spec, COUNT(*) AS cnt " +
                    "FROM consultation c " +
                    "JOIN medecin m ON m.id = c.medecin_id " +
                    "WHERE DATE(c.date) BETWEEN DATE(:start) AND DATE(:end) " +
                    "GROUP BY spec",
            nativeQuery = true)
    List<Object[]> countBySpecialteBetweenNative(@Param("start") Date start, @Param("end") Date end);

    // ✅ KPI "Temps moyen" : 100% DB, mais 0 tant que tu n’as pas de champ durée
    @Query("SELECT 0 FROM Consultation c")
    double avgDurationMinutesDummy();
}