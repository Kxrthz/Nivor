package com.nivor.auth.dto;

import com.nivor.user.dto.UserResponse;

public record AuthResponse(String token, UserResponse user) { }
