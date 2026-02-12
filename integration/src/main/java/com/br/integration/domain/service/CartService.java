package com.br.integration.domain.service;

import com.br.integration.domain.dto.CartDTO;
import com.br.integration.domain.entites.Cart;
import com.br.integration.domain.Exception.cartException.CartException;
import com.br.integration.domain.repository.CartRepository;
import com.br.integration.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CartDTO addAlbumToCart(String userEmail, String albumId) {
        if (userEmail == null || userEmail.trim().isEmpty()) {
            throw new CartException("Email do usuário não pode ser vazio");
        }
        if (albumId == null || albumId.trim().isEmpty()) {
            throw new CartException("ID do álbum não pode ser vazio");
        }

        if (!userRepository.existsByEmail(userEmail)) {
            throw new CartException("Usuário não encontrado: " + userEmail);
        }

        Optional<Cart> optionalCart = cartRepository.findByUserEmail(userEmail);

        Cart cart = optionalCart.orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserEmail(userEmail);
            newCart.setCreatedAt(LocalDateTime.now());
            return newCart;
        });

        if (cart.getAlbumIds().contains(albumId)) {
            throw new CartException("Álbum já está no carrinho");
        }

        cart.addAlbum(albumId);
        cart.setUpdatedAt(LocalDateTime.now());

        Cart saved = cartRepository.save(cart);

        return new CartDTO(
                saved.getUserEmail(),
                saved.getAlbumIds(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }

    @Transactional
    public CartDTO removeAlbumFromCart(String userEmail, String albumId) {
        Cart cart = cartRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new CartException("Carrinho não encontrado para o usuário: " + userEmail));

        if (!cart.getAlbumIds().remove(albumId)) {
            throw new CartException("Álbum não encontrado no carrinho");
        }

        cart.setUpdatedAt(LocalDateTime.now());
        Cart saved = cartRepository.save(cart);

        return new CartDTO(
                saved.getUserEmail(),
                saved.getAlbumIds(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }

    public CartDTO getCart(String userEmail) {
        Cart cart = cartRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new CartException("Carrinho não encontrado para o usuário: " + userEmail));

        return new CartDTO(
                cart.getUserEmail(),
                cart.getAlbumIds(),
                cart.getCreatedAt(),
                cart.getUpdatedAt()
        );
    }
}