package com.nivor.task.dto;

import com.nivor.task.TaskPriority;
import com.nivor.task.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record TaskRequest(@NotBlank @Size(max=180) String title, @Size(max=4000) String description, TaskStatus status, TaskPriority priority, LocalDate dueDate, LocalTime dueTime, @Size(max=80) String category, List<@Size(max=40) String> tags, Long projectId, Long goalId, Long parentTaskId) { }
