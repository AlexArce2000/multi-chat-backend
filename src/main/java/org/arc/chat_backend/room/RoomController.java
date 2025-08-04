package org.arc.chat_backend.room;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Map<String, Object> payload, Authentication authentication) {
        String name = (String) payload.get("name");
        boolean isPublic = (Boolean) payload.get("isPublic");
        String password = (String) payload.get("password");
        String creatorUsername = authentication.getName();

        Room createdRoom = roomService.createRoom(name, isPublic, password, creatorUsername);
        return ResponseEntity.ok(createdRoom);
    }

    @GetMapping("/public")
    public ResponseEntity<List<Room>> getPublicRooms() {
        return ResponseEntity.ok(roomService.getPublicRooms());
    }
}