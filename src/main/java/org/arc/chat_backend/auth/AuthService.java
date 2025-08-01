package org.arc.chat_backend.auth;

import org.arc.chat_backend.auth.dto.RegisterRequest;
import org.arc.chat_backend.user.User;
import org.arc.chat_backend.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Inyección de dependencias vía constructor
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequest request) {
        // Verificar si el usuario ya existe
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalStateException("El nombre de usuario ya está registrado");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        //Hashear la contraseña antes de guardarla
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
    }
}
