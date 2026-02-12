package com.br.integration.domain.repository;

import com.br.integration.domain.entites.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserEmail(String userEmail);

    boolean existsByUserEmail(String userEmail);
    void deleteByUserEmail(String userEmail);

}