package com.nivor.user;

import com.nivor.common.exception.ResourceNotFoundException;
import com.nivor.user.dto.ProfileUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository users;
    public UserService(UserRepository users) { this.users = users; }
    public User getByEmail(String email) { return users.findByEmail(email.toLowerCase()).orElseThrow(() -> new ResourceNotFoundException("User not found")); }
    @Transactional public User update(String email, ProfileUpdateRequest request) { User user = getByEmail(email); user.setName(request.name().trim()); user.setBio(request.bio()); user.setAvatarUrl(request.avatarUrl()); return user; }
}
