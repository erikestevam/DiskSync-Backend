package com.br.integration.domain.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="WALLET")

public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable= false, nullable = false)
    private Long Id;
    @Column(name = "balance")
    private BigDecimal balance;
    @Column(name = "points")
    private Long points;
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User users;


    public Wallet (){

    }
    public Wallet(BigDecimal balance, Long points, LocalDateTime lastUpdate, User users) {
        this.balance = balance;
        this.points = points;
        this.lastUpdate = lastUpdate;
        this.users = users;
    }
}
