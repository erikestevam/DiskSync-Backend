package com.br.integration.domain.service.checkoutService;

import com.br.integration.config.security.AuthenticationService;
import com.br.integration.domain.exception.checkoutException.CheckoutException;
import com.br.integration.domain.dto.CartDTO;
import com.br.integration.domain.entites.Order;
import com.br.integration.domain.entites.Wallet;
import com.br.integration.domain.repository.CartRepository;
import com.br.integration.domain.repository.WalletRepository;
import com.br.integration.domain.service.orderService.OrderService;
import com.br.integration.domain.service.cartService.CartService;
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
    private final AuthenticationService authenticationService;

    @Transactional
    public Order confirmCheckout() {

        CartDTO cart = cartService.getCart();
        if (cart.albumIds().isEmpty()) {
            throw new CheckoutException("Carrinho vazio, não é possível gerar pedido.");
        }

        double totalValue = cart.totalValue();
        String userEmail = authenticationService.getCurrentUserEmail();

        Wallet wallet = walletRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new CheckoutException("Carteira não encontrada para o usuário."));

        if (wallet.getBalance().doubleValue() < totalValue) {
            throw new CheckoutException("Saldo insuficiente na carteira.");
        }

        wallet.setBalance(wallet.getBalance().subtract(BigDecimal.valueOf(totalValue)));
        wallet.setLastUpdate(LocalDateTime.now());
        walletRepository.save(wallet);

        Order order = orderService.createOrder(userEmail, cart.albumIds(), totalValue);
        cartRepository.deleteByUserEmail(userEmail);

        return order;
    }
}
