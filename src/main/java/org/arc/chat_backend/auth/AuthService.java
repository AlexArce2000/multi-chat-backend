package org.arc.chat_backend.auth;

import org.arc.chat_backend.auth.dto.LoginRequest;
import org.arc.chat_backend.auth.dto.RegisterRequest;
import org.arc.chat_backend.security.JwtUtil;
import org.arc.chat_backend.user.User;
import org.arc.chat_backend.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    // Inyección de dependencias vía constructor
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
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
    public String login(LoginRequest request) {
        // 1. Autenticar al usuario con Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // 2. Si la autenticación es exitosa, cargar los detalles del usuario
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        // 3. Generar y devolver el token JWT
        return jwtUtil.generateToken(userDetails);
    }

}
