package com.br.integration.domain.service;

import com.br.integration.domain.dto.CartDTO;
import com.br.integration.domain.entites.Order;
import com.br.integration.domain.entites.Wallet;
import com.br.integration.domain.repository.CartRepository;
import com.br.integration.domain.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final CartService cartService;
    private final WalletRepository walletRepository;
    private final OrderService orderService;
    private final CartRepository cartRepository;

    @Transactional
    public Order confirmCheckout(String userEmail) {

        CartDTO cart = cartService.getCart(userEmail);
        if (cart.albumIds().isEmpty()) {
            throw new RuntimeException("Carrinho vazio, não é possível gerar pedido.");
        }

        double totalValue = cart.totalValue();

        Wallet wallet = walletRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Carteira não encontrada para o usuário"));
        if (wallet.getBalance().doubleValue() < totalValue) {
            throw new RuntimeException("Saldo insuficiente na carteira");
        }

        wallet.setBalance(wallet.getBalance().subtract(BigDecimal.valueOf(totalValue)));
        wallet.setLastUpdate(LocalDateTime.now());
        walletRepository.save(wallet);

        Order order = orderService.createOrder(userEmail, cart.albumIds(), totalValue);

        cartRepository.deleteByUserEmail(userEmail);

        return order;
    }
}
