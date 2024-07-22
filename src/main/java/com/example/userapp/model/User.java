package com.example.userapp.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String gender;
    private Integer age;
    private LocalDateTime createdTimestamp;

    public User(String username, String gender, Integer age) {
        this.username = username;
        this.gender = gender;
        this.age = age;
    }

    public User(String gender, Integer age) {
        this.gender = gender;
        this.age = age;
    }

    public User() {

    }


    @PrePersist
    protected void onCreate() {
        createdTimestamp = LocalDateTime.now();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getGender() {
        return gender;
    }

    public Integer getAge() {
        return age;
    }

    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setAge(int age) {
        this.age = age;
    }
}
