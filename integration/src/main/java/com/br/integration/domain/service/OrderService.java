package com.br.integration.domain.service;

import com.br.integration.domain.Exception.orderException.InvalidOrderStateException;
import com.br.integration.domain.Exception.orderException.OrderNotFoundException;
import com.br.integration.domain.entites.Order;
import com.br.integration.domain.repository.OrderRepository;
import com.br.integration.domain.service.state.InPreparation;
import com.br.integration.domain.service.state.ForDelivery;
import com.br.integration.domain.service.state.Received;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public Order createOrder(String email, java.util.List<String> albumIds, Double totalValue) {
        Order order = Order.builder()
                .userEmail(email)
                .albumIds(new ArrayList<>(albumIds)) // cria nova lista para evitar erro de referência
                .totalValue(totalValue)
                .createdAt(LocalDateTime.now())
                .status("PREPARE")
                .build();
        order.setState(new InPreparation());
        return orderRepository.save(order);
    }

    @Transactional
    public void updateToDelivery(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        reconstituteState(order);
        try {
            order.startDelivery();
        } catch (IllegalStateException e) {
            throw new InvalidOrderStateException(e.getMessage());
        }
        orderRepository.save(order);
    }

    @Transactional
    public void updateToReceived(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        reconstituteState(order);
        try {
            order.confirmReceipt();
        } catch (IllegalStateException e) {
            throw new InvalidOrderStateException(e.getMessage());
        }
        orderRepository.save(order);
    }

    public String getStatus(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return order.getStatus();
    }

    private void reconstituteState(Order order) {
        switch (order.getStatus()) {
            case "PREPARE" -> order.setState(new InPreparation());
            case "FORDELIVERY" -> order.setState(new ForDelivery());
            case "RECEIVED" -> order.setState(new Received());
            default -> throw new InvalidOrderStateException("Estado desconhecido: " + order.getStatus());
        }
    }
}