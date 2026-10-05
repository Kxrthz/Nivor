package com.nivor.task;

import com.nivor.task.dto.TaskRequest;
import com.nivor.task.dto.TaskResponse;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService service;
    public TaskController(TaskService service){this.service=service;}
    @GetMapping public List<TaskResponse> list(@AuthenticationPrincipal UserDetails u,@RequestParam(required=false) TaskStatus status,@RequestParam(required=false) TaskPriority priority,@RequestParam(required=false) LocalDate dueDate,@RequestParam(required=false) Long project,@RequestParam(required=false) Long goal){return service.list(u.getUsername(),status,priority,dueDate,project,goal).stream().map(TaskResponse::from).toList();}
    @GetMapping("/{id}") public TaskResponse get(@AuthenticationPrincipal UserDetails u,@PathVariable Long id){return TaskResponse.from(service.get(u.getUsername(),id));}
    @PostMapping public ResponseEntity<TaskResponse> create(@AuthenticationPrincipal UserDetails u,@Valid @RequestBody TaskRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(TaskResponse.from(service.create(u.getUsername(),r)));}
    @PutMapping("/{id}") public TaskResponse update(@AuthenticationPrincipal UserDetails u,@PathVariable Long id,@Valid @RequestBody TaskRequest r){return TaskResponse.from(service.update(u.getUsername(),id,r));}
    @PatchMapping("/{id}/complete") public TaskResponse complete(@AuthenticationPrincipal UserDetails u,@PathVariable Long id){return TaskResponse.from(service.setStatus(u.getUsername(),id,TaskStatus.COMPLETED));}
    @PatchMapping("/{id}/status") public TaskResponse status(@AuthenticationPrincipal UserDetails u,@PathVariable Long id,@RequestBody StatusRequest r){return TaskResponse.from(service.setStatus(u.getUsername(),id,r.status()));}
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails u,@PathVariable Long id){service.delete(u.getUsername(),id);return ResponseEntity.noContent().build();}
    public record StatusRequest(@jakarta.validation.constraints.NotNull TaskStatus status){}
}
