package com.nivor.health;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController @RequestMapping("/api/health")
public class HealthController {
    private final HealthRecordService records;
    private final JdbcTemplate jdbc;
    public HealthController(HealthRecordService records, JdbcTemplate jdbc) { this.records=records; this.jdbc=jdbc; }
    @GetMapping public Map<String, String> health() { try { jdbc.queryForObject("SELECT 1", Integer.class); return Map.of("status", "UP", "service", "NIVOR API", "database", "UP"); } catch (RuntimeException ex) { throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Service temporarily unavailable."); } }
    @GetMapping("/summary") public HealthDtos.Summary summary(@AuthenticationPrincipal UserDetails user) { return records.summary(user.getUsername()); }
}
