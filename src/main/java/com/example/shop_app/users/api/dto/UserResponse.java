package com.example.shop_app.users.api.dto;

import com.example.shop_app.users.domain.User;
import com.example.shop_app.users.domain.UserRole;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String phone,
        UserRole role
) {
    public static UserResponse fromEntity(User user){
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getRole()
        );
    }
}
