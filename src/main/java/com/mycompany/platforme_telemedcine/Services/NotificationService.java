package com.mycompany.platforme_telemedcine.Services;

import com.mycompany.platforme_telemedcine.Models.Notification;
import com.mycompany.platforme_telemedcine.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification createNotification(String message, Long medecinId) {
        Notification notification = new Notification(message, medecinId);
        return notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsByMedecinId(Long medecinId) {
        return notificationRepository.findByMedecinIdOrderByCreatedAtDesc(medecinId);
    }

    public List<Notification> getUnreadNotificationsByMedecinId(Long medecinId) {
        return notificationRepository.findByMedecinIdAndReadFalseOrderByCreatedAtDesc(medecinId);
    }

    public Notification markAsRead(Long notificationId) {
        Optional<Notification> notification = notificationRepository.findById(notificationId);
        if (notification.isPresent()) {
            Notification notif = notification.get();
            notif.setRead(true);
            return notificationRepository.save(notif);
        }
        return null;
    }

    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    public void deleteAllNotificationsByMedecinId(Long medecinId) {
        notificationRepository.deleteByMedecinId(medecinId);
    }

    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }
}

