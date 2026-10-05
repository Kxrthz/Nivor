package com.nivor.goal;
import java.util.List;import java.util.Optional;import org.springframework.data.jpa.repository.JpaRepository;
public interface MilestoneRepository extends JpaRepository<Milestone,Long>{List<Milestone> findAllByGoal_IdOrderByDueDateAsc(Long goalId);Optional<Milestone> findByIdAndGoal_Id(Long id,Long goalId);}
