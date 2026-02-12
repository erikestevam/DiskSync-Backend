package com.br.integration.domain.entites;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "WALLET")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Wallet() {
    }

    public Wallet(BigDecimal balance, Long points, LocalDateTime lastUpdate, User user) {
        this.balance = balance != null ? balance : BigDecimal.ZERO;
        this.lastUpdate = lastUpdate;
        this.user = user;
    }

    public void recharge(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor de recarga deve ser positivo");
        }
        this.balance = this.balance.add(amount);
        this.lastUpdate = LocalDateTime.now();
    }
}