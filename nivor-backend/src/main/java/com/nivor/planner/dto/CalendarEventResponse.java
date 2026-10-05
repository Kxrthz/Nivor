package com.nivor.planner.dto;
import com.nivor.planner.CalendarEvent;import java.time.*;
public record CalendarEventResponse(Long id,String title,String description,LocalDateTime startTime,LocalDateTime endTime,String location,String color,boolean allDay,String recurrenceRule,LocalDateTime createdAt,LocalDateTime updatedAt){public static CalendarEventResponse from(CalendarEvent e){return new CalendarEventResponse(e.getId(),e.getTitle(),e.getDescription(),e.getStartTime(),e.getEndTime(),e.getLocation(),e.getColor(),e.isAllDay(),e.getRecurrenceRule(),e.getCreatedAt(),e.getUpdatedAt());}}
