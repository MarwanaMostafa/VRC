package com.example.vrc.rooms.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.vrc.rooms.services.RoomService;





    @Component
    public class UserRoomChannelInterceptor implements ChannelInterceptor {

        @Autowired
        private RoomService roomService;

        @Override
        public Message<?> preSend(Message<?> message, MessageChannel channel) {
            SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(message);
            String destination = accessor.getDestination();

            if (destination != null && destination.startsWith("/topic/rooms/")) {
                Authentication auth = (Authentication) accessor.getUser();
                if (auth != null && auth.isAuthenticated()) {
                    String userEmail = auth.getName();
                    String destinationEmail = extractEmailFromDestination(destination);
                    if (!userEmail.equalsIgnoreCase(destinationEmail)) {
                        throw new AccessDeniedException("User not authorized for this room");
                    }
                }
            }
            return message;
        }

        private String extractEmailFromDestination(String destination) {
            String[] parts = destination.split("/");
            return parts.length > 3 ? parts[3] : "";
        }
    }



