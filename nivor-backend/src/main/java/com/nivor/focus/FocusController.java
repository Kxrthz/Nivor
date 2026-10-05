package com.nivor.focus;
import com.nivor.focus.FocusDtos.*;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/focus")
public class FocusController {
    private final FocusService service;
    public FocusController(FocusService service){this.service=service;}
    @PostMapping("/sessions") public ResponseEntity<SessionView> start(@AuthenticationPrincipal UserDetails u,@Valid@RequestBody StartRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(service.start(u.getUsername(),r));}
    @GetMapping("/sessions") public List<SessionView> list(@AuthenticationPrincipal UserDetails u){return service.list(u.getUsername());}
    @GetMapping("/summary") public Summary summary(@AuthenticationPrincipal UserDetails u){return service.summary(u.getUsername());}
    @PatchMapping("/sessions/{id}/pause") public SessionView pause(@AuthenticationPrincipal UserDetails u,@PathVariable long id){return service.pause(u.getUsername(),id);}
    @PatchMapping("/sessions/{id}/resume") public SessionView resume(@AuthenticationPrincipal UserDetails u,@PathVariable long id){return service.resume(u.getUsername(),id);}
    @PatchMapping("/sessions/{id}/complete") public SessionView complete(@AuthenticationPrincipal UserDetails u,@PathVariable long id){return service.complete(u.getUsername(),id);}
    @PatchMapping("/sessions/{id}/abandon") public SessionView abandon(@AuthenticationPrincipal UserDetails u,@PathVariable long id){return service.abandon(u.getUsername(),id);}
}
