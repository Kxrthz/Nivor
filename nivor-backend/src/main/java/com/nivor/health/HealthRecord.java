package com.nivor.health;
import com.nivor.common.entity.AuditedEntity;import com.nivor.user.User;import jakarta.persistence.*;import java.math.BigDecimal;import java.time.LocalDate;
@Entity @Table(name="health_records",uniqueConstraints=@UniqueConstraint(name="uq_health_user_date",columnNames={"user_id","record_date"}),indexes=@Index(name="idx_health_user_date",columnList="user_id,record_date"))
public class HealthRecord extends AuditedEntity{
 @ManyToOne(fetch=FetchType.LAZY,optional=false)@JoinColumn(name="user_id",nullable=false)private User user;@Column(name="record_date",nullable=false)private LocalDate date;
 @Column(precision=6,scale=2)private BigDecimal weight;private Integer waterMl;private Integer sleepMinutes;private Integer steps;private Integer exerciseMinutes;@Column(length=2000)private String notes;
 protected HealthRecord(){}public User getUser(){return user;}public void setUser(User v){user=v;}public LocalDate getDate(){return date;}public void setDate(LocalDate v){date=v;}public BigDecimal getWeight(){return weight;}public void setWeight(BigDecimal v){weight=v;}public Integer getWaterMl(){return waterMl;}public void setWaterMl(Integer v){waterMl=v;}public Integer getSleepMinutes(){return sleepMinutes;}public void setSleepMinutes(Integer v){sleepMinutes=v;}public Integer getSteps(){return steps;}public void setSteps(Integer v){steps=v;}public Integer getExerciseMinutes(){return exerciseMinutes;}public void setExerciseMinutes(Integer v){exerciseMinutes=v;}public String getNotes(){return notes;}public void setNotes(String v){notes=v;}
}
