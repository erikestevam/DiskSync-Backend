package com.br.integration.domain.service;

import com.br.integration.domain.Exception.cartException.CartNotFoundException;
import com.br.integration.domain.Exception.cartException.InvalidCartOperationException;
import com.br.integration.domain.dto.CartDTO;
import com.br.integration.domain.entites.Cart;
import com.br.integration.domain.repository.CartRepository;
import com.br.integration.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final AlbumService albumService;
    private final AuthenticationService authenticationService;

    public CartService(CartRepository cartRepository, UserRepository userRepository, AlbumService albumService, AuthenticationService authenticationService) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.albumService = albumService;
        this.authenticationService = authenticationService;
    }

    @Transactional
    public CartDTO addAlbumToCart(String albumId) {
        String userEmail = authenticationService.getCurrentUserEmail();

        if (albumId == null || albumId.trim().isEmpty()) {
            throw new InvalidCartOperationException("ID do álbum não pode ser vazio.");
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

        cart.addAlbum(albumId);

        try {
            double albumPrice = albumService.getAlbumId(albumId).getBody().price();
            cart.setTotalValue(cart.getTotalValue() + albumPrice);
        } catch (Exception e) {
            throw new InvalidCartOperationException("Erro ao buscar informações do álbum.");
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
    public CartDTO removeAlbumFromCart(String albumId) {
        String userEmail = authenticationService.getCurrentUserEmail();

        Cart cart = cartRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new CartNotFoundException("Carrinho não encontrado para o usuário logado"));

        if (!cart.getAlbumIds().remove(albumId)) {
            throw new InvalidCartOperationException("Álbum não encontrado no seu carrinho.");
        }

        try {
            double albumPrice = albumService.getAlbumId(albumId).getBody().price();
            cart.setTotalValue(cart.getTotalValue() - albumPrice);
        } catch (Exception e) {
            throw new InvalidCartOperationException("Erro ao recalcular o valor do carrinho.");
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

    public CartDTO getCart() {
        String userEmail = authenticationService.getCurrentUserEmail();

        Cart cart = cartRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new CartNotFoundException("Carrinho não encontrado para o usuário logado"));

        return new CartDTO(
                cart.getUserEmail(),
                cart.getAlbumIds(),
                cart.getCreatedAt(),
                cart.getUpdatedAt(),
                cart.getTotalValue()
        );
    }
}