package org.arc.chat_backend.room;
import jakarta.persistence.*;
import lombok.Data;
import org.arc.chat_backend.user.User;

@Data
@Entity
@Table(name = "sala_membresias")
public class RoomMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

}