package com.mycompany.platforme_telemedcine.RestControllers;
import com.mycompany.platforme_telemedcine.Models.Notification;
import com.mycompany.platforme_telemedcine.Services.ImpService.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private NotificationService notificationService;

    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotificationToMedecin(Long medecinId, String message) {
        Notification savedNotification = null;
        try {
            savedNotification = notificationService.createNotification(message, medecinId);
            System.out.println("Notification saved to DB with ID: " + savedNotification.getId());
        } catch (Exception e) {
            System.err.println("Error saving notification to DB: " + e.getMessage());
            e.printStackTrace();
        }
        
        String destination = "/topic/medecin-" + medecinId;
        System.out.println("ending notification to: " + destination);
        System.out.println("Message: " + message);
        System.out.println("Medecin ID: " + medecinId);
        
        try {
            if (savedNotification != null) {
                messagingTemplate.convertAndSend(destination, savedNotification);
            } else {
                messagingTemplate.convertAndSend(destination, message);
            }
            System.out.println("Notification sent successfully to " + destination);
        } catch (Exception e) {
            System.err.println("Error sending notification: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

