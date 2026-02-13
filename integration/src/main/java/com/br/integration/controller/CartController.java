package com.br.integration.controller;

import com.br.integration.domain.dto.CartDTO;
import com.br.integration.domain.exception.cartException.CartException;
import com.br.integration.domain.service.cartService.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Carrinho", description = "Adicionar, remover álbuns e consultar carrinho do usuário")
@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "Adicionar álbum ao carrinho", description = "Inclui um álbum no carrinho do usuário autenticado")
    @PostMapping("/albums/{albumId}")
    public ResponseEntity<?> addAlbum(@Parameter(description = "ID do álbum") @PathVariable String albumId) {
        try {
            CartDTO updatedCart = cartService.addAlbumToCart(albumId);
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedCart);
        } catch (CartException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Remover álbum do carrinho", description = "Remove um álbum do carrinho do usuário")
    @DeleteMapping("/albums/{albumId}")
    public ResponseEntity<?> removeAlbum(@Parameter(description = "ID do álbum") @PathVariable String albumId) {
        try {
            CartDTO updatedCart = cartService.removeAlbumFromCart(albumId);
            return ResponseEntity.ok(updatedCart);
        } catch (CartException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Obter carrinho", description = "Retorna o carrinho do usuário autenticado")
    @GetMapping
    public ResponseEntity<?> getCart() {
        try {
            CartDTO cart = cartService.getCart();
            return ResponseEntity.ok(cart);
        } catch (CartException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}