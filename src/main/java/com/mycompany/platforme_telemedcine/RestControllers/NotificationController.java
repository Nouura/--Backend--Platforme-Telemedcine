package com.mycompany.platforme_telemedcine.RestControllers;
import com.mycompany.platforme_telemedcine.Models.Notification;
import com.mycompany.platforme_telemedcine.Services.NotificationService;
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
        // Save notification to database
        Notification savedNotification = null;
        try {
            savedNotification = notificationService.createNotification(message, medecinId);
            System.out.println("💾 Notification saved to DB with ID: " + savedNotification.getId());
        } catch (Exception e) {
            System.err.println("❌ Error saving notification to DB: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Send notification via WebSocket
        String destination = "/topic/medecin-" + medecinId;
        System.out.println("📤 Sending notification to: " + destination);
        System.out.println("📤 Message: " + message);
        System.out.println("📤 Medecin ID: " + medecinId);
        
        try {
            // Send the notification object (with ID) via WebSocket
            if (savedNotification != null) {
                messagingTemplate.convertAndSend(destination, savedNotification);
            } else {
                // Fallback: send just the message if DB save failed
                messagingTemplate.convertAndSend(destination, message);
            }
            System.out.println("✅ Notification sent successfully to " + destination);
        } catch (Exception e) {
            System.err.println("❌ Error sending notification: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

