package com.nivor.task;

import com.nivor.common.entity.AuditedEntity;
import com.nivor.goal.Goal;
import com.nivor.project.Project;
import com.nivor.user.User;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name="tasks", indexes={@Index(name="idx_task_user", columnList="user_id"), @Index(name="idx_task_due_date", columnList="due_date")})
public class Task extends AuditedEntity {
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="user_id", nullable=false) private User user;
    @Column(nullable=false, length=180) private String title;
    @Column(length=4000) private String description;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=24) private TaskStatus status=TaskStatus.TODO;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=16) private TaskPriority priority=TaskPriority.MEDIUM;
    @Column(name="due_date") private LocalDate dueDate;
    @Column(name="due_time") private LocalTime dueTime;
    @Column(length=80) private String category;
    @ElementCollection @CollectionTable(name="task_tags", joinColumns=@JoinColumn(name="task_id")) @Column(name="tag", length=40) private List<String> tags=new ArrayList<>();
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="project_id") private Project project;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="goal_id") private Goal goal;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="parent_task_id") private Task parentTask;
    private LocalDateTime completedAt;
    protected Task() { }
    public User getUser(){return user;} public void setUser(User v){user=v;} public String getTitle(){return title;} public void setTitle(String v){title=v;} public String getDescription(){return description;} public void setDescription(String v){description=v;} public TaskStatus getStatus(){return status;} public void setStatus(TaskStatus v){status=v;} public TaskPriority getPriority(){return priority;} public void setPriority(TaskPriority v){priority=v;} public LocalDate getDueDate(){return dueDate;} public void setDueDate(LocalDate v){dueDate=v;} public LocalTime getDueTime(){return dueTime;} public void setDueTime(LocalTime v){dueTime=v;} public String getCategory(){return category;} public void setCategory(String v){category=v;} public List<String> getTags(){return tags;} public void setTags(List<String> v){tags=v==null?new ArrayList<>():new ArrayList<>(v);} public Project getProject(){return project;} public void setProject(Project v){project=v;} public Goal getGoal(){return goal;} public void setGoal(Goal v){goal=v;} public Task getParentTask(){return parentTask;} public void setParentTask(Task v){parentTask=v;} public LocalDateTime getCompletedAt(){return completedAt;} public void setCompletedAt(LocalDateTime v){completedAt=v;}
}
