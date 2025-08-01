package org.arc.chat_backend.message;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "mensajes")
public class ChatMessage {

    @Id
    private String id;

    private String salaId;

    private String remitente;

    private String contenido;

    private LocalDateTime timestamp = LocalDateTime.now();
}