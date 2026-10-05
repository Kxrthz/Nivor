package com.nivor.planner;
import com.nivor.planner.dto.*;import jakarta.validation.Valid;import java.time.LocalDateTime;import java.util.List;import org.springframework.http.*;import org.springframework.security.core.annotation.AuthenticationPrincipal;import org.springframework.security.core.userdetails.UserDetails;import org.springframework.web.bind.annotation.*;
@RestController@RequestMapping("/api/calendar/events")public class CalendarEventController{
private final CalendarEventService service;public CalendarEventController(CalendarEventService s){service=s;}
@GetMapping public List<CalendarEventResponse> list(@AuthenticationPrincipal UserDetails u,@RequestParam(required=false)LocalDateTime from,@RequestParam(required=false)LocalDateTime to){return service.list(u.getUsername(),from,to).stream().map(CalendarEventResponse::from).toList();}
@GetMapping("/{id}")public CalendarEventResponse get(@AuthenticationPrincipal UserDetails u,@PathVariable Long id){return CalendarEventResponse.from(service.get(u.getUsername(),id));}
@PostMapping public ResponseEntity<CalendarEventResponse> create(@AuthenticationPrincipal UserDetails u,@Valid@RequestBody CalendarEventRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(CalendarEventResponse.from(service.create(u.getUsername(),r)));}
@PutMapping("/{id}")public CalendarEventResponse update(@AuthenticationPrincipal UserDetails u,@PathVariable Long id,@Valid@RequestBody CalendarEventRequest r){return CalendarEventResponse.from(service.update(u.getUsername(),id,r));}
@DeleteMapping("/{id}")public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails u,@PathVariable Long id){service.delete(u.getUsername(),id);return ResponseEntity.noContent().build();}
}
