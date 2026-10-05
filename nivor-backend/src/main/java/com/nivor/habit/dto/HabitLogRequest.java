package com.nivor.habit.dto;import jakarta.validation.constraints.NotNull;import java.time.LocalDate;public record HabitLogRequest(@NotNull LocalDate date,boolean completed){}
