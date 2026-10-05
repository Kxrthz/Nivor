package com.nivor.user.dto;

import com.nivor.user.User;
import java.time.LocalDateTime;

public record UserResponse(Long id, String name, String email, String avatarUrl, String bio, LocalDateTime createdAt) {
    public static UserResponse from(User u) { return new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getAvatarUrl(), u.getBio(), u.getCreatedAt()); }
}
