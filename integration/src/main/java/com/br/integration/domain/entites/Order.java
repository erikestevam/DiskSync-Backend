package com.br.integration.domain.entites;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import com.br.integration.domain.service.cartService.cartState.OrderState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties({"state"})
@Entity
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @ElementCollection
    @CollectionTable(name = "ORDER_ALBUMS", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "album_id")
    private List<String> albumIds = new ArrayList<>();

    @Column(name = "total_value", nullable = false)
    private Double totalValue; 

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "status")
    private String status;

    @Transient
    private OrderState state;


    public void startPreparation() {
        state.inPreparation(this);
    }

    public void startDelivery() {
        state.forDelivery(this);
    }

    public void confirmReceipt() {
        state.received(this);
    }
}