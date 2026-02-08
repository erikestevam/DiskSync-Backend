package com.br.integration.domain.repository;

import com.br.integration.domain.entites.User;
import com.br.integration.domain.entites.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {
          public Optional<Wallet> findByUsers(User users);
}
