package com.br.integration.domain.dto;

public record AlbumDTODetails(
        String id,
        String nome,
        String artista,
        String dataLancamento,
        String imagemUrl,
        String spotifyUrl,
        int popularity,
        int trackCount,
        double price

) { }
