package com.mycompany.platforme_telemedcine.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignalingMessage {

    private String roomId;
    private String type;      // join | offer | answer | ice
    private String senderId;  // rempli côté serveur

    private SessionDescription offer;
    private SessionDescription answer;
    private IceCandidate candidate;

    public SignalingMessage() {}

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public SessionDescription getOffer() { return offer; }
    public void setOffer(SessionDescription offer) { this.offer = offer; }

    public SessionDescription getAnswer() { return answer; }
    public void setAnswer(SessionDescription answer) { this.answer = answer; }

    public IceCandidate getCandidate() { return candidate; }
    public void setCandidate(IceCandidate candidate) { this.candidate = candidate; }

    // --------
    // Inner DTOs (WebRTC objects)
    // --------

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SessionDescription {
        private String type; // "offer" | "answer"
        private String sdp;

        public SessionDescription() {}

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getSdp() { return sdp; }
        public void setSdp(String sdp) { this.sdp = sdp; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IceCandidate {
        private String candidate;
        private String sdpMid;
        private Integer sdpMLineIndex;
        private String usernameFragment;

        public IceCandidate() {}

        public String getCandidate() { return candidate; }
        public void setCandidate(String candidate) { this.candidate = candidate; }

        public String getSdpMid() { return sdpMid; }
        public void setSdpMid(String sdpMid) { this.sdpMid = sdpMid; }

        public Integer getSdpMLineIndex() { return sdpMLineIndex; }
        public void setSdpMLineIndex(Integer sdpMLineIndex) { this.sdpMLineIndex = sdpMLineIndex; }

        public String getUsernameFragment() { return usernameFragment; }
        public void setUsernameFragment(String usernameFragment) { this.usernameFragment = usernameFragment; }
    }
}
