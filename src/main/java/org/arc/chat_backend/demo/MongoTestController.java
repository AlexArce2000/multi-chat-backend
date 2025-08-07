package org.arc.chat_backend.demo;

import org.arc.chat_backend.message.ChatMessage;
import org.arc.chat_backend.message.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class MongoTestController {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @GetMapping("/test-mongo")
    public ResponseEntity<String> testMongoConnection() {
        System.out.println("\n\n>>> INICIANDO PRUEBA DE CONEXIÓN A MONGODB...\n");
        try {
            ChatMessage testMessage = new ChatMessage();
            testMessage.setRemitente("sistema");
            testMessage.setContenido("Este es un mensaje de prueba de conexión.");
            testMessage.setSalaId("test-sala");
            testMessage.setTimestamp(LocalDateTime.now());

            System.out.println(">>> Intentando guardar el mensaje: " + testMessage);
            chatMessageRepository.save(testMessage);
            System.out.println(">>> ¡Mensaje guardado con ÉXITO en MongoDB!\n");

            return ResponseEntity.ok("¡Conexión y guardado en MongoDB exitosos!");

        } catch (Exception e) {
            System.err.println("\n\n!!!!!! ERROR AL GUARDAR EN MONGODB !!!!!!");
            e.printStackTrace();
            System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\n");

            return ResponseEntity.status(500).body("Error al intentar guardar en MongoDB: " + e.getMessage());
        }
    }
}