package com.nivor.goal.dto;
import com.nivor.goal.*;import java.time.*;
public record GoalResponse(Long id,String title,String description,LocalDate deadline,int progress,GoalStatus status,LocalDateTime createdAt,LocalDateTime updatedAt){public static GoalResponse from(Goal g){return new GoalResponse(g.getId(),g.getTitle(),g.getDescription(),g.getDeadline(),g.getProgress(),g.getStatus(),g.getCreatedAt(),g.getUpdatedAt());}}
