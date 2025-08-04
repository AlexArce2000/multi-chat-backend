package org.arc.chat_backend.room;

import jakarta.persistence.*;
import lombok.Data;
import org.arc.chat_backend.user.User;

@Data
@Entity
@Table(name = "salas")
public class Room {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String name;
    private boolean isPublic;
    private String password;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;
}