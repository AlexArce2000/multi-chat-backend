package org.arc.chat_backend.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomMembershipRepository extends JpaRepository<RoomMembership, Long> {
    // Método para verificar si un usuario ya es miembro de una sala
    Optional<RoomMembership> findByUser_IdAndRoom_Id(Long userId, String roomId);
}
