package org.arc.chat_backend.room;

import org.arc.chat_backend.user.User;
import org.arc.chat_backend.user.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RoomService(RoomRepository roomRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Room createRoom(String name, boolean isPublic, String password, String creatorUsername) {
        User creator = userRepository.findByUsername(creatorUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Creador no encontrado"));

        Room room = new Room();
        room.setId(UUID.randomUUID().toString());
        room.setName(name);
        room.setPublic(isPublic);
        room.setCreator(creator);

        if (!isPublic && password != null && !password.isEmpty()) {
            room.setPassword(passwordEncoder.encode(password));
        }

        return roomRepository.save(room);
    }

    public List<Room> getPublicRooms() {
        return roomRepository.findByIsPublic(true);
    }

}