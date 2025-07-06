package com.example.demo.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Entity representing a deposit transaction.
 */
@Entity
@DiscriminatorValue("DEPOSIT")
public class DepositTransaction extends Transaction {
}