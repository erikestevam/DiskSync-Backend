package com.br.integration.domain.dto;

public record AlbumDTO(String id,
                       String nome,
                       String artista,
                       String dataLancamento,
                       String imagemUrl,
                       String spotifyUrl
) {
}
