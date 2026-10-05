package com.nivor.task;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @EntityGraph(attributePaths = "tags")
    List<Task> findAllByUser_IdOrderByDueDateAscDueTimeAsc(Long userId);
    @EntityGraph(attributePaths = "tags")
    Optional<Task> findByIdAndUser_Id(Long id, Long userId);
    List<Task> findAllByUser_IdAndStatus(Long userId, TaskStatus status);
    long countByUser_IdAndDueDateAndStatus(Long userId, LocalDate dueDate, TaskStatus status);
    long countByUser_IdAndDueDate(Long userId, LocalDate dueDate);
    long countByUser_IdAndDueDateIsNotNull(Long userId);
    @Modifying @Query("update Task t set t.project = null where t.project.id = :projectId and t.user.id = :userId") int clearProjectForUser(Long projectId, Long userId);
    @Modifying @Query("update Task t set t.goal = null where t.goal.id = :goalId and t.user.id = :userId") int clearGoalForUser(Long goalId, Long userId);
}
