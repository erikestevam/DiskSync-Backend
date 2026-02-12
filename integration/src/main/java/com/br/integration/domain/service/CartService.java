package com.br.integration.domain.service;

import com.br.integration.domain.Exception.cartException.CartNotFoundException;
import com.br.integration.domain.Exception.cartException.InvalidCartOperationException;
import com.br.integration.domain.dto.CartDTO;
import com.br.integration.domain.entites.Cart;
import com.br.integration.domain.Exception.cartException.CartException;
import com.br.integration.domain.repository.CartRepository;
import com.br.integration.domain.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final AlbumService albumService;

    public CartService(CartRepository cartRepository, UserRepository userRepository, AlbumService albumService) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.albumService = albumService;
    }

    @Transactional
    public CartDTO addAlbumToCart(String userEmail, String albumId) {
        if (userEmail == null || userEmail.trim().isEmpty()) {
            throw new InvalidCartOperationException("Email do usuário não pode ser vazio");
        }
        if (albumId == null || albumId.trim().isEmpty()) {
            throw new InvalidCartOperationException("ID do álbum não pode ser vazio");
        }

        if (!userRepository.existsByEmail(userEmail)) {
            throw new InvalidCartOperationException("Usuário não encontrado: " + userEmail);
        }

        Cart cart = cartRepository.findByUserEmail(userEmail)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserEmail(userEmail);
                    newCart.setCreatedAt(LocalDateTime.now());
                    newCart.setTotalValue(0.0);
                    return newCart;
                });

        if (cart.getAlbumIds().contains(albumId)) {
            throw new InvalidCartOperationException("Álbum já está no carrinho");
        }

        cart.addAlbum(albumId);

        try {
            double albumPrice = albumService.getAlbumId(albumId).getBody().price();
            cart.setTotalValue(cart.getTotalValue() + albumPrice);
        } catch (JsonProcessingException e) {
            throw new InvalidCartOperationException("Erro ao calcular preço do álbum: " + albumId);
        }

        cart.setUpdatedAt(LocalDateTime.now());
        Cart saved = cartRepository.save(cart);

        return new CartDTO(
                saved.getUserEmail(),
                saved.getAlbumIds(),
                saved.getCreatedAt(),
                saved.getUpdatedAt(),
                saved.getTotalValue()
        );
    }

    @Transactional
    public CartDTO removeAlbumFromCart(String userEmail, String albumId) {
        Cart cart = cartRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new CartNotFoundException(userEmail));

        if (!cart.getAlbumIds().remove(albumId)) {
            throw new InvalidCartOperationException("Álbum não encontrado no carrinho");
        }

        try {
            double albumPrice = albumService.getAlbumId(albumId).getBody().price();
            cart.setTotalValue(cart.getTotalValue() - albumPrice);
        } catch (JsonProcessingException e) {
            throw new InvalidCartOperationException("Erro ao recalcular preço do álbum removido: " + albumId);
        }

        cart.setUpdatedAt(LocalDateTime.now());
        Cart saved = cartRepository.save(cart);

        return new CartDTO(
                saved.getUserEmail(),
                saved.getAlbumIds(),
                saved.getCreatedAt(),
                saved.getUpdatedAt(),
                saved.getTotalValue()
        );
    }

    public CartDTO getCart(String userEmail) {
        Cart cart = cartRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new CartNotFoundException(userEmail));

        return new CartDTO(
                cart.getUserEmail(),
                cart.getAlbumIds(),
                cart.getCreatedAt(),
                cart.getUpdatedAt(),
                cart.getTotalValue()
        );
    }

    public double calculateTotalValue(String userEmail) {
        Cart cart = cartRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new CartNotFoundException(userEmail));

        double total = cart.getAlbumIds().stream()
                .mapToDouble(albumId -> {
                    try {
                        return albumService.getAlbumId(albumId).getBody().price();
                    } catch (JsonProcessingException e) {
                        throw new InvalidCartOperationException("Erro ao calcular preço do álbum: " + albumId);
                    }
                })
                .sum();

        cart.setTotalValue(total);
        cartRepository.save(cart);
        return total;
    }
}