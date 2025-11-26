package com.mycompany.platforme_telemedcine.Repository;

import com.mycompany.platforme_telemedcine.Models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByMedecinIdOrderByCreatedAtDesc(Long medecinId);
    List<Notification> findByMedecinIdAndReadFalseOrderByCreatedAtDesc(Long medecinId);
    void deleteByMedecinId(Long medecinId);
}

