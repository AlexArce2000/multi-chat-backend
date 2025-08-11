package org.arc.chat_backend.room;

import org.arc.chat_backend.user.User;
import org.arc.chat_backend.user.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoomMembershipRepository membershipRepository;

    public RoomService(RoomRepository roomRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, RoomMembershipRepository membershipRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.membershipRepository = membershipRepository;
    }
    @Transactional
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
        Room savedRoom = roomRepository.save(room);
        RoomMembership membership = new RoomMembership();
        membership.setUser(creator);
        membership.setRoom(savedRoom);
        membershipRepository.save(membership);
        return savedRoom;
    }
    @Transactional
    public void joinRoom(String roomId, String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalStateException("Sala no encontrada"));

        if (membershipRepository.findByUser_IdAndRoom_Id(user.getId(), roomId).isPresent()) {
            return;
        }

        // Si la sala es privada, validar la contraseña
        if (!room.isPublic()) {
            if (password == null || !passwordEncoder.matches(password, room.getPassword())) {
                throw new IllegalStateException("Contraseña incorrecta para la sala privada");
            }
        }
        RoomMembership newMembership = new RoomMembership();
        newMembership.setUser(user);
        newMembership.setRoom(room);
        membershipRepository.save(newMembership);
    }
    public List<Room> getPublicRooms() {
        return roomRepository.findByIsPublic(true);
    }
    public boolean isUserMemberOfRoom(String username, String roomId) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return false;
        }
        return membershipRepository.findByUser_IdAndRoom_Id(user.getId(), roomId).isPresent();
    }
    public boolean isRoomPublic(String roomId) {
        return roomRepository.findById(roomId)
                .map(Room::isPublic)
                .orElse(false);
    }
}