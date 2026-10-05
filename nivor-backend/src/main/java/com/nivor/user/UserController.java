package com.nivor.user;

import com.nivor.user.dto.ProfileUpdateRequest;
import com.nivor.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/api/users/me")
public class UserController {
    private final UserService service;
    public UserController(UserService service) { this.service = service; }
    @GetMapping public UserResponse me(@AuthenticationPrincipal UserDetails user) { return UserResponse.from(service.getByEmail(user.getUsername())); }
    @PutMapping public UserResponse update(@AuthenticationPrincipal UserDetails user, @Valid @RequestBody ProfileUpdateRequest request) { return UserResponse.from(service.update(user.getUsername(), request)); }
}
