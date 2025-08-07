package org.arc.chat_backend.message;

import org.springframework.stereotype.Service;


@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    // SIN @Transactional
    public ChatMessage saveMessage(ChatMessage chatMessage) {
        System.out.println(">>> CHAT_MESSAGE_SERVICE: Guardando mensaje (sin transacción)...");
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
        System.out.println(">>> CHAT_MESSAGE_SERVICE: Mensaje guardado con ID: " + savedMessage.getId());
        return savedMessage;
    }
}