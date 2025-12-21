package com.mycompany.platforme_telemedcine.RestControllers;

import com.mycompany.platforme_telemedcine.dto.MonthlyPointDTO;
import com.mycompany.platforme_telemedcine.dto.ReportSummaryDTO;
import com.mycompany.platforme_telemedcine.dto.SpecialtyPointDTO;
import com.mycompany.platforme_telemedcine.Models.Consultation;
import com.mycompany.platforme_telemedcine.Repository.ConsultationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormatSymbols;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportAnalyticsRestController {

    @Autowired
    private ConsultationRepository consultationRepository;

    private double round2(double x) {
        return Math.round(x * 100.0) / 100.0;
    }

    private Date startOfMonthsAgo(int months) {
        if (months < 1) months = 1;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Calendar startCal = (Calendar) cal.clone();
        startCal.add(Calendar.MONTH, -(months - 1));
        return startCal.getTime();
    }

    // =========================
    // 1) Summary (cards)
    // =========================
    @GetMapping("/summary")
    public ResponseEntity<ReportSummaryDTO> summary() {

        long totalConsultations = consultationRepository.count();

        Date start = startOfMonthsAgo(5);
        Date end = new Date();

        long activeDoctors = consultationRepository.countDistinctActiveDoctorsBetween(start, end);

        ReportSummaryDTO dto = new ReportSummaryDTO(
                totalConsultations,
                0.0, // satisfaction (à brancher)
                0,   // durée moyenne (à brancher)
                activeDoctors
        );

        return ResponseEntity.ok(dto);
    }

    // =========================
    // 2) Monthly chart (consultations/mois)
    // =========================
    @GetMapping("/monthly")
    public ResponseEntity<List<MonthlyPointDTO>> monthly(@RequestParam(defaultValue = "5") int months) {

        if (months < 1) months = 1;
        if (months > 24) months = 24;

        Date start = startOfMonthsAgo(months);
        Date end = new Date();

        List<Consultation> list = consultationRepository.findByDateBetween(start, end);

        Map<String, List<Consultation>> byMonth = list.stream()
                .filter(c -> c.getDate() != null)
                .collect(Collectors.groupingBy(c -> {
                    Calendar ccal = Calendar.getInstance();
                    ccal.setTime(c.getDate());
                    int y = ccal.get(Calendar.YEAR);
                    int m = ccal.get(Calendar.MONTH) + 1;
                    return y + "-" + String.format("%02d", m);
                }));

        List<MonthlyPointDTO> result = new ArrayList<>();
        Calendar iter = Calendar.getInstance();
        iter.setTime(start);

        for (int i = 0; i < months; i++) {
            int year = iter.get(Calendar.YEAR);
            int monthIndex = iter.get(Calendar.MONTH); // 0-11
            String key = year + "-" + String.format("%02d", monthIndex + 1);

            long consultations = byMonth.getOrDefault(key, Collections.emptyList()).size();
            double revenus = 0.0; // pas de lien paiement-consultation ici

            String monthLabel = new DateFormatSymbols(Locale.ENGLISH).getShortMonths()[monthIndex];
            result.add(new MonthlyPointDTO(monthLabel, consultations, round2(revenus)));

            iter.add(Calendar.MONTH, 1);
        }

        return ResponseEntity.ok(result);
    }

    // =========================
    // 3) Specialties (RÉELLE, plus "Autres 100%")
    // =========================
    @GetMapping("/specialties")
    public ResponseEntity<List<SpecialtyPointDTO>> specialties(@RequestParam(defaultValue = "12") int months) {

        if (months < 1) months = 1;
        if (months > 24) months = 24;

        Date start = startOfMonthsAgo(months);
        Date end = new Date();

        List<Object[]> rows = consultationRepository.countBySpecialteBetweenNative(start, end);

        long total = rows.stream().mapToLong(r -> ((Number) r[1]).longValue()).sum();
        if (total == 0) {
            return ResponseEntity.ok(List.of(new SpecialtyPointDTO("Autres", 100, "#FF8042")));
        }

        String[] palette = new String[] {
                "#0088FE", "#00C49F", "#FFBB28", "#FF8042",
                "#A855F7", "#22C55E", "#EF4444", "#0EA5E9"
        };

        // Trier desc
        rows.sort((a, b) -> Long.compare(((Number) b[1]).longValue(), ((Number) a[1]).longValue()));

        // Pourcentages arrondis + ajustement à 100
        int sumPct = 0;
        List<Integer> pcts = new ArrayList<>();
        for (Object[] r : rows) {
            long cnt = ((Number) r[1]).longValue();
            int pct = (int) Math.round(cnt * 100.0 / total);
            pcts.add(pct);
            sumPct += pct;
        }
        if (!pcts.isEmpty() && sumPct != 100) {
            pcts.set(0, pcts.get(0) + (100 - sumPct));
        }

        List<SpecialtyPointDTO> result = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            String spec = String.valueOf(rows.get(i)[0]);
            int pct = pcts.get(i);
            result.add(new SpecialtyPointDTO(spec, pct, palette[i % palette.length]));
        }

        return ResponseEntity.ok(result);
    }

    // =========================
    // 4) Export CSV
    // =========================
    @GetMapping("/export/monthly.csv")
    public ResponseEntity<byte[]> exportMonthlyCsv(@RequestParam(defaultValue = "5") int months) {

        List<MonthlyPointDTO> points = monthly(months).getBody();
        if (points == null) points = Collections.emptyList();

        StringBuilder sb = new StringBuilder();
        sb.append("month,consultations,revenus\n");
        for (MonthlyPointDTO p : points) {
            sb.append(p.name).append(",")
                    .append(p.consultations).append(",")
                    .append(p.revenus).append("\n");
        }

        byte[] bytes = sb.toString().getBytes();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/csv; charset=utf-8")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=rapport_mensuel.csv")
                .body(bytes);
    }

    // =========================
    // 5) Satisfaction placeholder
    // =========================
    @GetMapping("/satisfaction")
    public ResponseEntity<Map<String, Object>> satisfaction() {
        Map<String, Object> res = new HashMap<>();
        res.put("satisfactionRate", 0.0);
        res.put("definition", "Non disponible: aucune donnée satisfaction liée aux consultations.");
        return ResponseEntity.ok(res);
    }
}