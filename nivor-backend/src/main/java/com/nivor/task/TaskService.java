package com.nivor.task;

import com.nivor.common.exception.ResourceNotFoundException;
import com.nivor.goal.GoalRepository;
import com.nivor.project.ProjectRepository;
import com.nivor.task.dto.TaskRequest;
import com.nivor.user.User;
import com.nivor.user.UserService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {
    private final TaskRepository tasks; private final UserService users; private final ProjectRepository projects; private final GoalRepository goals;
    public TaskService(TaskRepository tasks,UserService users,ProjectRepository projects,GoalRepository goals){this.tasks=tasks;this.users=users;this.projects=projects;this.goals=goals;}
    @Transactional(readOnly=true) public List<Task> list(String email,TaskStatus status,TaskPriority priority,java.time.LocalDate dueDate,Long projectId,Long goalId){User u=users.getByEmail(email);return tasks.findAllByUser_IdOrderByDueDateAscDueTimeAsc(u.getId()).stream().filter(t->status==null||t.getStatus()==status).filter(t->priority==null||t.getPriority()==priority).filter(t->dueDate==null||dueDate.equals(t.getDueDate())).filter(t->projectId==null||t.getProject()!=null&&projectId.equals(t.getProject().getId())).filter(t->goalId==null||t.getGoal()!=null&&goalId.equals(t.getGoal().getId())).toList();}
    @Transactional(readOnly=true) public Task get(String email,Long id){return owned(email,id);}
    @Transactional public Task create(String email,TaskRequest r){User u=users.getByEmail(email);Task t=new Task();t.setUser(u);apply(t,r,u);return tasks.save(t);}
    @Transactional public Task update(String email,Long id,TaskRequest r){Task t=owned(email,id);apply(t,r,t.getUser());return t;}
    @Transactional public Task setStatus(String email,Long id,TaskStatus status){Task t=owned(email,id);t.setStatus(status);t.setCompletedAt(status==TaskStatus.COMPLETED?LocalDateTime.now():null);return t;}
    @Transactional public void delete(String email,Long id){tasks.delete(owned(email,id));}
    private Task owned(String email,Long id){User u=users.getByEmail(email);return tasks.findByIdAndUser_Id(id,u.getId()).orElseThrow(()->new ResourceNotFoundException("Task not found"));}
    private void apply(Task t,TaskRequest r,User u){t.setTitle(r.title().trim());t.setDescription(r.description());t.setPriority(r.priority()==null?TaskPriority.MEDIUM:r.priority());t.setStatus(r.status()==null?TaskStatus.TODO:r.status());t.setCompletedAt(t.getStatus()==TaskStatus.COMPLETED?(t.getCompletedAt()==null?LocalDateTime.now():t.getCompletedAt()):null);t.setDueDate(r.dueDate());t.setDueTime(r.dueTime());t.setCategory(r.category());t.setTags(r.tags());t.setProject(r.projectId()==null?null:projects.findByIdAndUser_Id(r.projectId(),u.getId()).orElseThrow(()->new ResourceNotFoundException("Project not found")));t.setGoal(r.goalId()==null?null:goals.findByIdAndUser_Id(r.goalId(),u.getId()).orElseThrow(()->new ResourceNotFoundException("Goal not found")));t.setParentTask(r.parentTaskId()==null?null:tasks.findByIdAndUser_Id(r.parentTaskId(),u.getId()).orElseThrow(()->new ResourceNotFoundException("Parent task not found")));}
}
