package org.arc.chat_backend.security;

import org.arc.chat_backend.room.RoomService;
import org.springframework.stereotype.Service;

@Service("roomSecurityService")
public class RoomSecurityService {

    private final RoomService roomService;

    public RoomSecurityService(RoomService roomService) {
        this.roomService = roomService;
    }


    public boolean isMember(String roomId, String username) {
        if (roomId == null || username == null) return false;
        if (roomService.isRoomPublic(roomId)) {
            return true;
        }
        return roomService.isUserMemberOfRoom(username, roomId);
    }
}