package com.nivor.habit;
import com.nivor.common.entity.AuditedEntity;import jakarta.persistence.*;import java.time.LocalDate;
@Entity@Table(name="habit_logs",uniqueConstraints=@UniqueConstraint(name="uk_habit_log_date",columnNames={"habit_id","log_date"}),indexes=@Index(name="idx_habit_log_date",columnList="log_date"))public class HabitLog extends AuditedEntity{
 @ManyToOne(fetch=FetchType.LAZY,optional=false)@JoinColumn(name="habit_id",nullable=false)private Habit habit;@Column(name="log_date",nullable=false)private LocalDate date;@Column(nullable=false)private boolean completed;
 protected HabitLog(){}public Habit getHabit(){return habit;}public void setHabit(Habit v){habit=v;}public LocalDate getDate(){return date;}public void setDate(LocalDate v){date=v;}public boolean isCompleted(){return completed;}public void setCompleted(boolean v){completed=v;}
}
