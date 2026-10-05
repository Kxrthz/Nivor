package com.nivor.task.dto;

import com.nivor.task.Task;
import com.nivor.task.TaskPriority;
import com.nivor.task.TaskStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record TaskResponse(Long id, String title, String description, TaskStatus status, TaskPriority priority, LocalDate dueDate, LocalTime dueTime, String category, List<String> tags, Long projectId, Long goalId, Long parentTaskId, LocalDateTime completedAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
    public static TaskResponse from(Task t) { return new TaskResponse(t.getId(),t.getTitle(),t.getDescription(),t.getStatus(),t.getPriority(),t.getDueDate(),t.getDueTime(),t.getCategory(),List.copyOf(t.getTags()),t.getProject()==null?null:t.getProject().getId(),t.getGoal()==null?null:t.getGoal().getId(),t.getParentTask()==null?null:t.getParentTask().getId(),t.getCompletedAt(),t.getCreatedAt(),t.getUpdatedAt()); }
}
