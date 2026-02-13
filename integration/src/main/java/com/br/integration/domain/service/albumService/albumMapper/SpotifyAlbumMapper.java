package com.br.integration.domain.service.albumService.albumMapper;

import com.br.integration.domain.dto.AlbumDTO;
import com.br.integration.domain.dto.AlbumDTODetails;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public class SpotifyAlbumMapper {

    private final AlbumDTOMapper albumDTOMapper;
    private final AlbumDTODetailsMapper albumDTODetailsMapper;

    public SpotifyAlbumMapper(AlbumDTOMapper albumDTOMapper,
                              AlbumDTODetailsMapper albumDTODetailsMapper) {
        this.albumDTOMapper = albumDTOMapper;
        this.albumDTODetailsMapper = albumDTODetailsMapper;
    }

    public AlbumDTO toAlbumDTO(JsonNode node) {
        return albumDTOMapper.map(node);
    }

    public AlbumDTODetails toAlbumDTODetails(JsonNode node, double price) {
        return albumDTODetailsMapper.map(node, price);
    }
}
