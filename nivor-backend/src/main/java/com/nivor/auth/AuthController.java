package com.nivor.auth;

import com.nivor.auth.dto.AuthResponse;
import com.nivor.auth.dto.LoginRequest;
import com.nivor.auth.dto.RegisterRequest;
import com.nivor.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;
    public AuthController(AuthService auth) { this.auth = auth; }
    @PostMapping("/register") public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(auth.register(request)); }
    @PostMapping("/login") public AuthResponse login(@Valid @RequestBody LoginRequest request) { return auth.login(request); }
    @PostMapping("/logout") public ResponseEntity<Void> logout() { return ResponseEntity.noContent().build(); }
    @GetMapping("/me") public UserResponse me(@AuthenticationPrincipal UserDetails principal) { return UserResponse.from(auth.current(principal.getUsername())); }
}
