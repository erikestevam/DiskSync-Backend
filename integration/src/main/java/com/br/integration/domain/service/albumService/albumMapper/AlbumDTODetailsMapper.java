package com.br.integration.domain.service.albumService.albumMapper;

import com.br.integration.domain.dto.AlbumDTODetails;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public class AlbumDTODetailsMapper {

    public AlbumDTODetails map(JsonNode node, double price) {
        return new AlbumDTODetails(
                AlbumJsonNodeReader.textOrEmpty(node, "id"),
                AlbumJsonNodeReader.textOrEmpty(node, "name"),
                AlbumJsonNodeReader.firstArtistName(node),
                AlbumJsonNodeReader.textOrEmpty(node, "release_date"),
                AlbumJsonNodeReader.firstImageUrl(node),
                AlbumJsonNodeReader.spotifyUrl(node),
                AlbumJsonNodeReader.popularity(node),
                AlbumJsonNodeReader.trackCount(node),
                price
        );
    }
}
