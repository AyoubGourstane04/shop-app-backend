package com.example.shop_app.users.api;

import com.example.shop_app.users.api.dto.UserResponse;
import com.example.shop_app.users.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping("/me")
    public UserResponse getProfile(@AuthenticationPrincipal User currentUser){
        return UserResponse.fromEntity(currentUser);
    }

}
