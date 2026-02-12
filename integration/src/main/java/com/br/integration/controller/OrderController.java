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


    @GetMapping("/{id}/status")
    public ResponseEntity<String> getStatus(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getStatus(id));
    }

    @PutMapping("/{id}/delivery")
    public ResponseEntity<String> updateToDelivery(@PathVariable Long id) {
        orderService.updateToDelivery(id);
        return ResponseEntity.ok("O pedido nº " + id + " já está a caminho para entrega.");
    }

    @PutMapping("/{id}/received")
        public ResponseEntity<String> q(@PathVariable Long id) {
        orderService.updateToReceived(id);
        return ResponseEntity.ok("O pedido nº " + id + " foi entregue.");
    }
}
