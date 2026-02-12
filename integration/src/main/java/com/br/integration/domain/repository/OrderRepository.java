package com.br.integration.domain.repository;

import com.br.integration.domain.entites.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserEmail(String userEmail);

    List<Order> findByStatus(String status);
}

