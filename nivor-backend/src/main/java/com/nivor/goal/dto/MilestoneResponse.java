package com.nivor.goal.dto;
import com.nivor.goal.Milestone;import java.time.*;
public record MilestoneResponse(Long id,Long goalId,String title,String description,boolean completed,LocalDate dueDate,LocalDateTime createdAt,LocalDateTime updatedAt){public static MilestoneResponse from(Milestone m){return new MilestoneResponse(m.getId(),m.getGoal().getId(),m.getTitle(),m.getDescription(),m.isCompleted(),m.getDueDate(),m.getCreatedAt(),m.getUpdatedAt());}}
