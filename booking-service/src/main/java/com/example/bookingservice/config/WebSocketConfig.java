package com.example.bookingservice.config;

import com.example.bookingservice.websocket.BookingSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final BookingSocketHandler bookingSocketHandler;

    public WebSocketConfig(BookingSocketHandler bookingSocketHandler) {
        this.bookingSocketHandler = bookingSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(bookingSocketHandler, "/ws/office")
                .setAllowedOrigins("*");
    }
}
