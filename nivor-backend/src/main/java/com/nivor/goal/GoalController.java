package com.nivor.goal;

import com.nivor.goal.dto.*;import jakarta.validation.Valid;import java.util.List;import org.springframework.http.*;import org.springframework.security.core.annotation.AuthenticationPrincipal;import org.springframework.security.core.userdetails.UserDetails;import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/goals") public class GoalController{
 private final GoalService service;public GoalController(GoalService s){service=s;}
 @GetMapping public List<GoalResponse> list(@AuthenticationPrincipal UserDetails u){return service.list(u.getUsername()).stream().map(GoalResponse::from).toList();}
 @GetMapping("/{id}")public GoalResponse get(@AuthenticationPrincipal UserDetails u,@PathVariable Long id){return GoalResponse.from(service.get(u.getUsername(),id));}
 @PostMapping public ResponseEntity<GoalResponse> create(@AuthenticationPrincipal UserDetails u,@Valid @RequestBody GoalRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(GoalResponse.from(service.create(u.getUsername(),r)));}
 @PutMapping("/{id}")public GoalResponse update(@AuthenticationPrincipal UserDetails u,@PathVariable Long id,@Valid @RequestBody GoalRequest r){return GoalResponse.from(service.update(u.getUsername(),id,r));}
 @DeleteMapping("/{id}")public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails u,@PathVariable Long id){service.delete(u.getUsername(),id);return ResponseEntity.noContent().build();}
 @GetMapping("/{id}/milestones")public List<MilestoneResponse> milestones(@AuthenticationPrincipal UserDetails u,@PathVariable Long id){return service.listMilestones(u.getUsername(),id).stream().map(MilestoneResponse::from).toList();}
 @PostMapping("/{id}/milestones")public ResponseEntity<MilestoneResponse> createMilestone(@AuthenticationPrincipal UserDetails u,@PathVariable Long id,@Valid @RequestBody MilestoneRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(MilestoneResponse.from(service.createMilestone(u.getUsername(),id,r)));}
 @PutMapping("/{id}/milestones/{milestoneId}")public MilestoneResponse updateMilestone(@AuthenticationPrincipal UserDetails u,@PathVariable Long id,@PathVariable Long milestoneId,@Valid @RequestBody MilestoneRequest r){return MilestoneResponse.from(service.updateMilestone(u.getUsername(),id,milestoneId,r));}
 @PatchMapping("/{id}/milestones/{milestoneId}/complete")public MilestoneResponse complete(@AuthenticationPrincipal UserDetails u,@PathVariable Long id,@PathVariable Long milestoneId){return MilestoneResponse.from(service.completeMilestone(u.getUsername(),id,milestoneId));}
 @DeleteMapping("/{id}/milestones/{milestoneId}")public ResponseEntity<Void> deleteMilestone(@AuthenticationPrincipal UserDetails u,@PathVariable Long id,@PathVariable Long milestoneId){service.deleteMilestone(u.getUsername(),id,milestoneId);return ResponseEntity.noContent().build();}
}
