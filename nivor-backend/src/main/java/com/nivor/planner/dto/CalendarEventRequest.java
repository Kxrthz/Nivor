package com.nivor.planner.dto;
import jakarta.validation.constraints.NotBlank;import jakarta.validation.constraints.NotNull;import jakarta.validation.constraints.Size;import java.time.LocalDateTime;
public record CalendarEventRequest(@NotBlank @Size(max=180)String title,@Size(max=4000)String description,@NotNull LocalDateTime startTime,@NotNull LocalDateTime endTime,@Size(max=240)String location,@Size(max=40)String color,boolean allDay,@Size(max=500)String recurrenceRule){}
