package org.arc.chat_backend.message;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    private final ChatMessageRepository chatMessageRepository;

    public ChatWebSocketController(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @MessageMapping("/chat.sendMessage") // El cliente envía mensajes a /app/chat.sendMessage
    @SendTo("/topic/public")             // El valor de retorno se retransmite a /topic/public
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        // Guardamos el mensaje en MongoDB
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Añade el nombre de usuario a la sesión de WebSocket
        headerAccessor.getSessionAttributes().put("username", chatMessage.getRemitente());

        // Guardamos el mensaje de "unión" en MongoDB
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }
}