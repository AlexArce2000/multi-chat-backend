package org.arc.chat_backend.security;

import org.arc.chat_backend.room.RoomService; // Asegúrate de importar esto
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException; // Y esto
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final RoomService roomService;
    private final Pattern chatPattern = Pattern.compile("/app/chat\\..*/(.[^/]+)$");

    public WebSocketAuthInterceptor(JwtUtil jwtUtil, UserDetailsService userDetailsService, RoomService roomService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.roomService = roomService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // 1. Autenticar en la conexión
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwt = authHeader.substring(7);
                String username = jwtUtil.extractUsername(jwt);
                if (username != null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        accessor.setUser(authToken);
                    }
                }
            }
        }

        // Autorizar al enviar un mensaje
        else if (StompCommand.SEND.equals(accessor.getCommand())) {
            String destination = accessor.getDestination();
            if (destination != null) {
                Matcher matcher = chatPattern.matcher(destination);
                if (matcher.matches()) {
                    String roomId = matcher.group(1);
                    Principal principal = accessor.getUser();

                    if (principal == null || principal.getName() == null) {
                        throw new AccessDeniedException("No autenticado para enviar mensajes. Token faltante o inválido en la conexión.");
                    }

                    String username = principal.getName();

                    if (!roomService.isUserMemberOfRoom(username, roomId)) {
                        throw new AccessDeniedException("Acceso denegado: no eres miembro de la sala '" + roomId + "'.");
                    }
                }
            }
        }

        return message;
    }
}