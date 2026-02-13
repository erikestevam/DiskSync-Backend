package com.br.integration.domain.service.albumService.albumMapper;

import com.br.integration.domain.dto.AlbumDTO;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public class AlbumDTOMapper implements SpotifyResponseMapper<AlbumDTO> {

    @Override
    public AlbumDTO map(JsonNode node) {
        return new AlbumDTO(
                AlbumJsonNodeReader.textOrEmpty(node, "id"),
                AlbumJsonNodeReader.textOrEmpty(node, "name"),
                AlbumJsonNodeReader.firstArtistName(node),
                AlbumJsonNodeReader.textOrEmpty(node, "release_date"),
                AlbumJsonNodeReader.firstImageUrl(node),
                AlbumJsonNodeReader.spotifyUrl(node)
        );
    }
}
