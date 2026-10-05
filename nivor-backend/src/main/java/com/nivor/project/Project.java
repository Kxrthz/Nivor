package com.nivor.project;

import com.nivor.common.entity.AuditedEntity;import com.nivor.user.User;import com.nivor.workspace.Workspace;import jakarta.persistence.*;import java.time.LocalDate;
@Entity @Table(name="projects",indexes=@Index(name="idx_project_user",columnList="user_id"))
public class Project extends AuditedEntity {
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="user_id",nullable=false) private User user;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="workspace_id") private Workspace workspace;
 @Column(nullable=false,length=180) private String name;@Column(length=4000) private String description;@Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private ProjectStatus status=ProjectStatus.PLANNED;@Column(nullable=false) private int progress;private LocalDate deadline;
 protected Project(){}public User getUser(){return user;}public void setUser(User v){user=v;}public Workspace getWorkspace(){return workspace;}public void setWorkspace(Workspace v){workspace=v;}public String getName(){return name;}public void setName(String v){name=v;}public String getDescription(){return description;}public void setDescription(String v){description=v;}public ProjectStatus getStatus(){return status;}public void setStatus(ProjectStatus v){status=v;}public int getProgress(){return progress;}public void setProgress(int v){progress=v;}public LocalDate getDeadline(){return deadline;}public void setDeadline(LocalDate v){deadline=v;}
}
