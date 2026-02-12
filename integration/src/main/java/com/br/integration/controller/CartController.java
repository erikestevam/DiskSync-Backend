package com.br.integration.controller;

import com.br.integration.domain.dto.CartDTO;
import com.br.integration.domain.Exception.cartException.CartException;
import com.br.integration.domain.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/albums/{email}/{albumId}")
    public ResponseEntity<?> addAlbum(
            @PathVariable String email,
            @PathVariable String albumId) {
        try {
            CartDTO updatedCart = cartService.addAlbumToCart(email, albumId);
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedCart);
        } catch (CartException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/albums/{email}/{albumId}")
    public ResponseEntity<?> removeAlbum(
            @PathVariable String email,
            @PathVariable String albumId) {
        try {
            CartDTO updatedCart = cartService.removeAlbumFromCart(email, albumId);
            return ResponseEntity.ok(updatedCart);
        } catch (CartException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getCart(@PathVariable String email) {
        try {
            CartDTO cart = cartService.getCart(email);
            return ResponseEntity.ok(cart);
        } catch (CartException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}