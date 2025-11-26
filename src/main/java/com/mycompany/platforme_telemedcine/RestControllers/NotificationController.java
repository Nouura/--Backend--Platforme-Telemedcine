package com.mycompany.platforme_telemedcine.RestControllers;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotificationToMedecin(Long medecinId, String message) {
        // Sends a notification to /topic/medecin-{id}
        messagingTemplate.convertAndSend("/topic/medecin-" + medecinId, message);
    }
}

