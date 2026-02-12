package com.br.integration.domain.entites;

import jakarta.persistence.*;
        import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "CART")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    @ElementCollection
    @CollectionTable(name = "cart_albums", joinColumns = @JoinColumn(name = "cart_id"))
    @Column(name = "album_id")
    private List<String> albumIds = new ArrayList<>();

    @Column(name = "total_value", nullable = false)
    private Double totalValue = 0.0;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public void addAlbum(String albumId) {
        albumIds.add(albumId);
        updatedAt = LocalDateTime.now();
    }

    public void removeAlbum(String albumId) {
        albumIds.remove(albumId);
        updatedAt = LocalDateTime.now();
    }
}