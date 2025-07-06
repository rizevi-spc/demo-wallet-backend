package com.example.demo.entity;

import com.example.demo.enumeration.OppositePartyType;
import com.example.demo.enumeration.TransactionStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing a transaction.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Data
public abstract class Transaction {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private OppositePartyType oppositePartyType;
    // destination or source
    private String oppositePartyId;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    @CreatedDate
    @Column
    private LocalDateTime createdAt;
}