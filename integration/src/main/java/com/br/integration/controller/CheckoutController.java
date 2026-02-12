package com.br.integration.controller;

import com.br.integration.domain.entites.Order;
import com.br.integration.domain.service.CheckoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/{email}/confirm")
    public ResponseEntity<Order> confirmCheckout(@PathVariable String email) {
        Order order = checkoutService.confirmCheckout(email);
        return ResponseEntity.ok(order);
    }
}
