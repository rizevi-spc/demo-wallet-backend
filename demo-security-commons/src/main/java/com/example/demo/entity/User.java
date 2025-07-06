package com.example.demo.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.util.List;

/**
 * user entity
 */
@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String username;
    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Role> roles;

    private String password;
}
