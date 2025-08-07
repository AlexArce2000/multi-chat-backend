package org.arc.chat_backend.message;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    public ChatWebSocketController(SimpMessagingTemplate messagingTemplate, ChatMessageService chatMessageService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/chat.sendMessage/{roomId}")
    public void sendMessage(@DestinationVariable String roomId, @Payload ChatMessage chatMessage, Principal principal) {
        chatMessage.setRemitente(principal.getName());
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage.setSalaId(roomId);

        ChatMessage savedMessage = chatMessageService.saveMessage(chatMessage);

        String destination = "/topic/rooms/" + roomId;
        messagingTemplate.convertAndSend(destination, savedMessage);
    }

    @MessageMapping("/chat.addUser/{roomId}")
    public void addUser(@DestinationVariable String roomId, @Payload ChatMessage chatMessage, Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        chatMessage.setRemitente(principal.getName());

        headerAccessor.getSessionAttributes().put("username", principal.getName());
        headerAccessor.getSessionAttributes().put("roomId", roomId);

        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage.setSalaId(roomId);

        ChatMessage savedMessage = chatMessageService.saveMessage(chatMessage);

        String destination = "/topic/rooms/" + roomId;
        messagingTemplate.convertAndSend(destination, savedMessage);
    }
}