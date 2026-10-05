package com.nivor.goal;

import com.nivor.common.exception.ResourceNotFoundException;import com.nivor.goal.dto.*;import com.nivor.task.TaskRepository;import com.nivor.user.*;import java.util.List;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;
@Service public class GoalService{
 private final GoalRepository goals;private final MilestoneRepository milestones;private final UserService users;private final TaskRepository tasks;
 public GoalService(GoalRepository g,MilestoneRepository m,UserService u,TaskRepository t){goals=g;milestones=m;users=u;tasks=t;}
 @Transactional(readOnly=true)public List<Goal> list(String email){return goals.findAllByUser_IdOrderByDeadlineAsc(users.getByEmail(email).getId());}
 @Transactional(readOnly=true)public Goal get(String email,Long id){return owned(email,id);}
 @Transactional public Goal create(String email,GoalRequest r){Goal g=new Goal();g.setUser(users.getByEmail(email));apply(g,r);return goals.save(g);}
 @Transactional public Goal update(String email,Long id,GoalRequest r){Goal g=owned(email,id);apply(g,r);return g;}
 @Transactional public void delete(String email,Long id){Goal g=owned(email,id);tasks.clearGoalForUser(g.getId(),g.getUser().getId());milestones.deleteAll(milestones.findAllByGoal_IdOrderByDueDateAsc(id));goals.delete(g);}
 @Transactional(readOnly=true)public List<Milestone> listMilestones(String email,Long goalId){Goal g=owned(email,goalId);return milestones.findAllByGoal_IdOrderByDueDateAsc(g.getId());}
 @Transactional public Milestone createMilestone(String email,Long goalId,MilestoneRequest r){Milestone m=new Milestone();m.setGoal(owned(email,goalId));apply(m,r);return milestones.save(m);}
 @Transactional public Milestone updateMilestone(String email,Long goalId,Long id,MilestoneRequest r){Goal g=owned(email,goalId);Milestone m=milestones.findByIdAndGoal_Id(id,g.getId()).orElseThrow(()->new ResourceNotFoundException("Milestone not found"));apply(m,r);return m;}
 @Transactional public Milestone completeMilestone(String email,Long goalId,Long id){Goal g=owned(email,goalId);Milestone m=milestones.findByIdAndGoal_Id(id,g.getId()).orElseThrow(()->new ResourceNotFoundException("Milestone not found"));m.setCompleted(true);return m;}
 @Transactional public void deleteMilestone(String email,Long goalId,Long id){Goal g=owned(email,goalId);Milestone m=milestones.findByIdAndGoal_Id(id,g.getId()).orElseThrow(()->new ResourceNotFoundException("Milestone not found"));milestones.delete(m);}
 private Goal owned(String email,Long id){return goals.findByIdAndUser_Id(id,users.getByEmail(email).getId()).orElseThrow(()->new ResourceNotFoundException("Goal not found"));}
 private void apply(Goal g,GoalRequest r){g.setTitle(r.title().trim());g.setDescription(r.description());g.setDeadline(r.deadline());g.setProgress(r.progress());g.setStatus(r.status()==null?GoalStatus.ACTIVE:r.status());}
 private void apply(Milestone m,MilestoneRequest r){m.setTitle(r.title().trim());m.setDescription(r.description());m.setDueDate(r.dueDate());m.setCompleted(Boolean.TRUE.equals(r.completed()));}
}
