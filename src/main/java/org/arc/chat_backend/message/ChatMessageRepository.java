package org.arc.chat_backend.message;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    // MongoRepository<ChatMessage, String> -> Gestiona documentos ChatMessage cuya clave es de tipo String
    // De nuevo, Spring creará la consulta para encontrar todos los mensajes de una sala.
    List<ChatMessage> findBySalaId(String salaId);
}