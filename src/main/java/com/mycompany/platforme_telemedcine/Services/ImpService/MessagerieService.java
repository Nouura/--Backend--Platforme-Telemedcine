package com.mycompany.platforme_telemedcine.Services.ImpService;

import com.mycompany.platforme_telemedcine.Models.Messagerie;
import com.mycompany.platforme_telemedcine.Repository.MessagerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessagerieService {

    @Autowired
    private MessagerieRepository messagerieRepository;

    public List<Messagerie> getMessagesBetween(Long userId1, Long userId2) {
        return messagerieRepository.findConversationBetweenUsers(userId1, userId2);
    }

    public Messagerie saveMessage(Messagerie message) {
        return messagerieRepository.save(message);
    }

    // Méthode pour obtenir les conversations d'un utilisateur
    public List<Messagerie> getUserMessages(Long userId) {
        return messagerieRepository.findByUserId(userId);
    }
}