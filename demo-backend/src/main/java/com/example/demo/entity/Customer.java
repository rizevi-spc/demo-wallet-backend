package com.example.demo.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.util.List;

/**
 * Entity representing a Customer in the system.
 */
@Entity
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String surname;
    private Long userId;
}
