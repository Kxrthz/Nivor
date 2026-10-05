package com.nivor.auth;

import com.nivor.auth.dto.AuthResponse;
import com.nivor.auth.dto.LoginRequest;
import com.nivor.auth.dto.RegisterRequest;
import com.nivor.common.exception.ResourceNotFoundException;
import com.nivor.security.JwtService;
import com.nivor.user.User;
import com.nivor.user.UserRepository;
import com.nivor.user.dto.UserResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository users; private final PasswordEncoder passwords; private final JwtService jwt; private final AuthenticationManager authenticationManager;
    public AuthService(UserRepository users, PasswordEncoder passwords, JwtService jwt, AuthenticationManager authenticationManager) { this.users = users; this.passwords = passwords; this.jwt = jwt; this.authenticationManager = authenticationManager; }
    @Transactional public AuthResponse register(RegisterRequest request) {
        String email = normalize(request.email());
        if (users.existsByEmail(email)) throw new org.springframework.dao.DataIntegrityViolationException("Email already registered");
        User user = users.save(new User(request.name().trim(), email, passwords.encode(request.password())));
        return new AuthResponse(jwt.createToken(user), UserResponse.from(user));
    }
    public AuthResponse login(LoginRequest request) {
        String email = normalize(request.email());
        try { authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.password())); }
        catch (BadCredentialsException ex) { throw new BadCredentialsException("Invalid email or password"); }
        User user = users.findByEmail(email).orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
        return new AuthResponse(jwt.createToken(user), UserResponse.from(user));
    }
    public User current(String email) { return users.findByEmail(normalize(email)).orElseThrow(() -> new ResourceNotFoundException("User not found")); }
    private String normalize(String email) { return email.trim().toLowerCase(java.util.Locale.ROOT); }
}
