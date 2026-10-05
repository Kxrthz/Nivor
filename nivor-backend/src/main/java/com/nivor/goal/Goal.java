package com.nivor.goal;

import com.nivor.common.entity.AuditedEntity;
import com.nivor.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDate;

@Entity @Table(name="goals",indexes=@Index(name="idx_goal_user",columnList="user_id"))
public class Goal extends AuditedEntity {
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="user_id",nullable=false) private User user;
    @Column(nullable=false,length=180) private String title;
    @Column(length=4000) private String description;
    private LocalDate deadline;
    @Column(nullable=false) private int progress;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private GoalStatus status=GoalStatus.ACTIVE;
    protected Goal(){}
    public User getUser(){return user;}public void setUser(User v){user=v;}public String getTitle(){return title;}public void setTitle(String v){title=v;}public String getDescription(){return description;}public void setDescription(String v){description=v;}public LocalDate getDeadline(){return deadline;}public void setDeadline(LocalDate v){deadline=v;}public int getProgress(){return progress;}public void setProgress(int v){progress=v;}public GoalStatus getStatus(){return status;}public void setStatus(GoalStatus v){status=v;}
}
