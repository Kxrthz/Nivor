package com.nivor.goal.dto;
import jakarta.validation.constraints.NotBlank;import jakarta.validation.constraints.Size;import java.time.LocalDate;
public record MilestoneRequest(@NotBlank @Size(max=180) String title,@Size(max=2000) String description,LocalDate dueDate,Boolean completed){}
