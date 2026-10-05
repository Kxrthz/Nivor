package com.nivor.focus;
import com.nivor.common.exception.ResourceNotFoundException;
import com.nivor.focus.FocusDtos.*;
import com.nivor.task.*;
import com.nivor.user.UserService;
import java.time.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
public class FocusService {
    private final FocusSessionRepository sessions; private final TaskRepository tasks; private final UserService users;
    public FocusService(FocusSessionRepository sessions,TaskRepository tasks,UserService users){this.sessions=sessions;this.tasks=tasks;this.users=users;}
    public SessionView start(String email,StartRequest r){FocusSession s=new FocusSession();s.setUser(users.getByEmail(email));if(r.taskId()!=null)s.setTask(tasks.findByIdAndUser_Id(r.taskId(),s.getUser().getId()).orElseThrow(()->new ResourceNotFoundException("Task not found")));s.setDurationSeconds(r.durationSeconds());s.setElapsedSeconds(0);s.setStartedAt(LocalDateTime.now());return SessionView.from(sessions.save(s));}
    public List<SessionView> list(String email){return sessions.findAllByUser_IdOrderByStartedAtDesc(uid(email)).stream().limit(200).map(SessionView::from).toList();}
    public SessionView pause(String email,long id){FocusSession s=owned(email,id);if(s.getStatus()!=FocusStatus.IN_PROGRESS)throw new IllegalArgumentException("Only active sessions can be paused");LocalDateTime now=LocalDateTime.now();s.setElapsedSeconds(s.getElapsedSeconds()+(int)Math.max(0,Duration.between(s.getStartedAt(),now).getSeconds()));s.setPausedAt(now);s.setStatus(FocusStatus.PAUSED);return SessionView.from(s);}
    public SessionView resume(String email,long id){FocusSession s=owned(email,id);if(s.getStatus()!=FocusStatus.PAUSED)throw new IllegalArgumentException("Only paused sessions can be resumed");s.setStartedAt(LocalDateTime.now());s.setPausedAt(null);s.setStatus(FocusStatus.IN_PROGRESS);return SessionView.from(s);}
    public SessionView complete(String email,long id){return close(email,id,FocusStatus.COMPLETED);}
    public SessionView abandon(String email,long id){return close(email,id,FocusStatus.ABANDONED);}
    private SessionView close(String email,long id,FocusStatus finalStatus){FocusSession s=owned(email,id);if(s.getStatus()!=FocusStatus.IN_PROGRESS&&s.getStatus()!=FocusStatus.PAUSED)throw new IllegalArgumentException("Focus session is already closed");int elapsed=s.getElapsedSeconds();if(s.getStatus()==FocusStatus.IN_PROGRESS)elapsed+=(int)Math.max(0,Duration.between(s.getStartedAt(),LocalDateTime.now()).getSeconds());s.setElapsedSeconds(elapsed);s.setDurationSeconds(elapsed);s.setEndedAt(LocalDateTime.now());s.setStatus(finalStatus);return SessionView.from(s);}
    public Summary summary(String email){List<FocusSession> xs=sessions.findAllByUser_IdOrderByStartedAtDesc(uid(email)).stream().filter(x->x.getStatus()==FocusStatus.COMPLETED).limit(500).toList();long seconds=xs.stream().mapToLong(x->x.getDurationSeconds()==null?0:x.getDurationSeconds()).sum();return new Summary(xs.size(),seconds,seconds/60);}
    private long uid(String e){return users.getByEmail(e).getId();}
    private FocusSession owned(String email,long id){return sessions.findByIdAndUser_Id(id,uid(email)).orElseThrow(()->new ResourceNotFoundException("Focus session not found"));}
}
