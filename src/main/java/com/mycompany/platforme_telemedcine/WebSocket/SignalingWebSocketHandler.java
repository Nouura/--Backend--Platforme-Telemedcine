package com.mycompany.platforme_telemedcine.WebSocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.platforme_telemedcine.dto.SignalingMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SignalingWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // roomId -> sessions
    private final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();

    // sessionId -> roomId (cleanup)
    private final Map<String, String> sessionRoom = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("✅ WS connected: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        SignalingMessage payload = objectMapper.readValue(message.getPayload(), SignalingMessage.class);

        if (payload.getRoomId() == null || payload.getRoomId().isBlank()) {
            session.sendMessage(new TextMessage("{\"type\":\"error\",\"message\":\"roomId is required\"}"));
            return;
        }

        payload.setSenderId(session.getId());

        String roomId = payload.getRoomId();
        String type = payload.getType() == null ? "" : payload.getType().toLowerCase();

        switch (type) {
            case "join" -> joinRoom(session, roomId);
            case "offer", "answer", "ice" -> relayToRoom(session, roomId, payload);
            default -> session.sendMessage(new TextMessage("{\"type\":\"error\",\"message\":\"unknown type\"}"));
        }
    }

    private void joinRoom(WebSocketSession session, String roomId) throws Exception {
        rooms.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session);
        sessionRoom.put(session.getId(), roomId);

        session.sendMessage(new TextMessage("{\"type\":\"joined\",\"roomId\":\"" + roomId + "\"}"));
        broadcast(roomId, session, new TextMessage("{\"type\":\"peer-joined\",\"roomId\":\"" + roomId + "\"}"));

        System.out.println("✅ WS " + session.getId() + " joined room " + roomId);
    }

    private void relayToRoom(WebSocketSession sender, String roomId, SignalingMessage payload) throws Exception {
        rooms.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(sender);
        sessionRoom.putIfAbsent(sender.getId(), roomId);

        String json = objectMapper.writeValueAsString(payload);
        broadcast(roomId, sender, new TextMessage(json));
    }

    private void broadcast(String roomId, WebSocketSession sender, TextMessage msg) throws Exception {
        Set<WebSocketSession> sessions = rooms.get(roomId);
        if (sessions == null) return;

        for (WebSocketSession s : sessions) {
            if (s.isOpen() && !s.getId().equals(sender.getId())) {
                s.sendMessage(msg);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("❌ WS disconnected: " + session.getId() + " status=" + status);

        String roomId = sessionRoom.remove(session.getId());
        if (roomId != null) {
            Set<WebSocketSession> sessions = rooms.get(roomId);
            if (sessions != null) {
                sessions.remove(session);
                if (sessions.isEmpty()) rooms.remove(roomId);
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.err.println("WS transport error: " + session.getId() + " " + exception.getMessage());
        session.close(CloseStatus.SERVER_ERROR);
    }
}
