package com.nivor.dashboard;

import com.nivor.goal.GoalRepository;
import com.nivor.goal.dto.GoalResponse;
import com.nivor.habit.HabitRepository;
import com.nivor.habit.HabitLogRepository;
import com.nivor.planner.CalendarEventRepository;
import com.nivor.planner.dto.CalendarEventResponse;
import com.nivor.task.Task;
import com.nivor.task.TaskRepository;
import com.nivor.task.TaskStatus;
import com.nivor.task.dto.TaskResponse;
import com.nivor.user.UserService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/api/dashboard")
public class DashboardController {
    private final UserService users; private final TaskRepository tasks; private final CalendarEventRepository events; private final GoalRepository goals; private final HabitRepository habits; private final HabitLogRepository habitLogs;
    public DashboardController(UserService users, TaskRepository tasks, CalendarEventRepository events, GoalRepository goals, HabitRepository habits, HabitLogRepository habitLogs) { this.users=users;this.tasks=tasks;this.events=events;this.goals=goals;this.habits=habits;this.habitLogs=habitLogs; }
    @GetMapping public DashboardResponse summary(@AuthenticationPrincipal UserDetails principal) {
        Long uid=users.getByEmail(principal.getUsername()).getId(); LocalDate today=LocalDate.now();
        List<Task> todayTasks=tasks.findAllByUser_IdOrderByDueDateAscDueTimeAsc(uid).stream().filter(t->today.equals(t.getDueDate())).toList();
        long done=todayTasks.stream().filter(t->t.getStatus()==TaskStatus.COMPLETED).count();
        var todayEvents=events.findAllByUser_IdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualOrderByStartTimeAsc(uid,today.plusDays(1).atStartOfDay(),today.atStartOfDay());
        var userHabits=habits.findAllByUser_IdOrderByCreatedAtAsc(uid).stream().filter(h->h.isActive()).toList();
        int checked=0;for(var habit:userHabits){if(habitLogs.findByHabit_IdAndDate(habit.getId(),today).map(l->l.isCompleted()).orElse(false))checked++;}
        int completion=todayTasks.isEmpty()?0:(int)Math.round(done*100.0/todayTasks.size());
        return new DashboardResponse(new TaskSummary(todayTasks.size(),done,completion),todayTasks.stream().map(TaskResponse::from).toList(),todayEvents.stream().map(CalendarEventResponse::from).toList(),new HabitSummary(userHabits.size(),checked,userHabits.isEmpty()?0:(int)Math.round(checked*100.0/userHabits.size())),goals.findAllByUser_IdOrderByDeadlineAsc(uid).stream().map(GoalResponse::from).toList());
    }
    public record TaskSummary(long total,long completed,int completionPercent){}
    public record HabitSummary(long active,long completedToday,int completionPercent){}
    public record DashboardResponse(TaskSummary taskSummary,List<TaskResponse> todayTasks,List<CalendarEventResponse> todayEvents,HabitSummary habitSummary,List<GoalResponse> goals){}
}
