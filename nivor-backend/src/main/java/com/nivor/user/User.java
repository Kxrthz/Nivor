package com.nivor.user;

import com.nivor.common.entity.AuditedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity @Table(name = "users")
public class User extends AuditedEntity {
    @Column(nullable = false, length = 100) private String name;
    @Column(nullable = false, unique = true, length = 254) private String email;
    @Column(nullable = false, length = 100) private String passwordHash;
    @Column(length = 1000) private String avatarUrl;
    @Column(length = 500) private String bio;
    protected User() { }
    public User(String name, String email, String passwordHash) { this.name = name; this.email = email; this.passwordHash = passwordHash; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
}
