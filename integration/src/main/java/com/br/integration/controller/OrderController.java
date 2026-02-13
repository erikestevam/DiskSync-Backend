package com.br.integration.controller;

import com.br.integration.domain.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<?> getMyOrders() {
        try {
            return ResponseEntity.ok(orderService.getMyOrders());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar pedidos: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<?> getStatus(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(orderService.getStatus(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar status do pedido: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/delivery")
    public ResponseEntity<?> updateToDelivery(@PathVariable Long id) {
        try {
            orderService.updateToDelivery(id);
            return ResponseEntity.ok("O pedido de numero " + id + " já está a caminho para entrega.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar pedido: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/received")
        public ResponseEntity<?> q(@PathVariable Long id) {
        try {
            orderService.updateToReceived(id);
            return ResponseEntity.ok("O pedido de numero " + id + " foi entregue.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar pedido: " + e.getMessage());
        }
    }
}
