package com.nivor.goal;
import java.util.List;import java.util.Optional;import org.springframework.data.jpa.repository.JpaRepository;
public interface GoalRepository extends JpaRepository<Goal,Long>{List<Goal> findAllByUser_IdOrderByDeadlineAsc(Long userId);Optional<Goal> findByIdAndUser_Id(Long id,Long userId);}
