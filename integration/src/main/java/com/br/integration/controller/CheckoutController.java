package com.br.integration.controller;

import com.br.integration.domain.entites.Order;
import com.br.integration.domain.service.checkoutService.CheckoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Checkout", description = "Confirmar finalização do pedido (carrinho → pedido)")
@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @Operation(summary = "Confirmar checkout", description = "Finaliza o carrinho e gera o pedido para o usuário autenticado")
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmCheckout() {
        try {
            Order order = checkoutService.confirmCheckout();
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
