package com.example.demo.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Entity representing a Withdraw Transaction.
 */
@Entity
@DiscriminatorValue("WITHDRAW")
public class WithdrawTransaction extends Transaction {
}