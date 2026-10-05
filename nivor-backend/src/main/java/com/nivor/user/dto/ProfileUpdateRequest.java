package com.nivor.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfileUpdateRequest(@NotBlank @Size(max = 100) String name, @Size(max = 500) String bio, @Size(max = 1000) String avatarUrl) { }
