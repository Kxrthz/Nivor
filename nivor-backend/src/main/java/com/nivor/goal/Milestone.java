package com.nivor.goal;

import com.nivor.common.entity.AuditedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity @Table(name="milestones")
public class Milestone extends AuditedEntity {
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="goal_id",nullable=false) private Goal goal;
    @Column(nullable=false,length=180) private String title;
    @Column(length=2000) private String description;
    @Column(nullable=false) private boolean completed;
    private LocalDate dueDate;
    protected Milestone(){}
    public Goal getGoal(){return goal;}public void setGoal(Goal v){goal=v;}public String getTitle(){return title;}public void setTitle(String v){title=v;}public String getDescription(){return description;}public void setDescription(String v){description=v;}public boolean isCompleted(){return completed;}public void setCompleted(boolean v){completed=v;}public LocalDate getDueDate(){return dueDate;}public void setDueDate(LocalDate v){dueDate=v;}
}
