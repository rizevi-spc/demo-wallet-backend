package com.example.demo.entity;

import com.example.demo.enumeration.Currency;
import com.example.demo.enumeration.TransactionType;
import com.example.demo.exception.CustomValidationException;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

/**
 * Entity representing a Wallet.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private String name;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    private boolean activeForShopping;
    private boolean activeForWithdraw;

    private BigDecimal balance;

    @Version
    private Long version;

    public void withdraw(BigDecimal amount) {
        if (!this.isActiveForWithdraw()) {
            throw new CustomValidationException("valid.wallet.not.active", this.getId());
        }

        if (this.getBalance().compareTo(amount) < 0) {
            throw new CustomValidationException("valid.wallet.insufficient.balance", this.getId());
        }
        this.setBalance(this.getBalance().subtract(amount));
    }

    public void deposit(BigDecimal amount) {
        this.setBalance(this.getBalance().add(amount));
    }
}
