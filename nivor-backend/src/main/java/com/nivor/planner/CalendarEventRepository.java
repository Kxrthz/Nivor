package com.nivor.planner;
import java.time.LocalDateTime;import java.util.List;import java.util.Optional;import org.springframework.data.jpa.repository.JpaRepository;
public interface CalendarEventRepository extends JpaRepository<CalendarEvent,Long>{List<CalendarEvent> findAllByUser_IdOrderByStartTimeAsc(Long userId);List<CalendarEvent> findAllByUser_IdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualOrderByStartTimeAsc(Long userId,LocalDateTime to,LocalDateTime from);Optional<CalendarEvent> findByIdAndUser_Id(Long id,Long userId);}
