package com.nivor.goal.dto;
import com.nivor.goal.GoalStatus;import jakarta.validation.constraints.Max;import jakarta.validation.constraints.Min;import jakarta.validation.constraints.NotBlank;import jakarta.validation.constraints.Size;import java.time.LocalDate;
public record GoalRequest(@NotBlank @Size(max=180) String title,@Size(max=4000) String description,LocalDate deadline,@Min(0) @Max(100) int progress,GoalStatus status){}
