package com.example.shop_app.users.api.dto;

public record AuthResponse(
        String token,
        UserResponse user
) {}
