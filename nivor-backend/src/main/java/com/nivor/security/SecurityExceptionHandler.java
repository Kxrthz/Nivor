package com.nivor.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nivor.common.response.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class SecurityExceptionHandler implements AuthenticationEntryPoint {
    private final ObjectMapper mapper;
    public SecurityExceptionHandler(ObjectMapper mapper) { this.mapper = mapper; }
    @Override public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value()); response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getOutputStream(), new ApiError(Instant.now(), 401, "UNAUTHORIZED", "Authentication is required", request.getRequestURI(), Map.of()));
    }
}
