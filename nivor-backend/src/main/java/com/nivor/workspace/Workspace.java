package com.nivor.workspace;

import com.nivor.common.entity.AuditedEntity;import com.nivor.user.User;import jakarta.persistence.*;
@Entity @Table(name="workspaces",indexes=@Index(name="idx_workspace_user",columnList="user_id"))
public class Workspace extends AuditedEntity {
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="user_id",nullable=false) private User user;@Column(nullable=false,length=120) private String name;@Column(length=1000) private String description;
 protected Workspace(){}public User getUser(){return user;}public void setUser(User v){user=v;}public String getName(){return name;}public void setName(String v){name=v;}public String getDescription(){return description;}public void setDescription(String v){description=v;}
}
