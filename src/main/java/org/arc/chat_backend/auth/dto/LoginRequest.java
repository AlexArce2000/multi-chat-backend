package org.arc.chat_backend.auth.dto;
import lombok.Data;
@Data
public class LoginRequest {
    private String username;
    private String password;
}


