package org.arc.chat_backend.room;

import org.arc.chat_backend.message.ChatMessage;
import org.arc.chat_backend.message.ChatMessageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // <-- IMPORTANTE
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomService roomService;
    private final ChatMessageRepository chatMessageRepository;

    public RoomController(RoomService roomService, ChatMessageRepository chatMessageRepository) {
        this.roomService = roomService;
        this.chatMessageRepository = chatMessageRepository;
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Map<String, Object> payload, Authentication authentication) {
        String name = (String) payload.get("name");
        // Asegurarse de que isPublic no sea nulo
        boolean isPublic = payload.get("isPublic") != null ? (Boolean) payload.get("isPublic") : false;
        String password = (String) payload.get("password");
        String creatorUsername = authentication.getName();

        Room createdRoom = roomService.createRoom(name, isPublic, password, creatorUsername);
        return ResponseEntity.ok(createdRoom);
    }

    @GetMapping("/public")
    public ResponseEntity<List<Room>> getPublicRooms() {
        return ResponseEntity.ok(roomService.getPublicRooms());
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<String> joinRoom(@PathVariable String roomId,
                                           @RequestBody(required = false) Map<String, String> payload,
                                           Authentication authentication) {
        try {
            String password = (payload != null) ? payload.get("password") : null;
            roomService.joinRoom(roomId, authentication.getName(), password);
            return ResponseEntity.ok("Te has unido a la sala exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{roomId}/messages")
    @PreAuthorize("@roomSecurityService.isMember(#roomId, principal.username)")
    public ResponseEntity<List<ChatMessage>> getRoomMessages(@PathVariable String roomId) {
        List<ChatMessage> messages = chatMessageRepository.findBySalaId(roomId);
        return ResponseEntity.ok(messages);
    }
}