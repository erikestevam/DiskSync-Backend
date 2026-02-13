package com.br.integration.domain.service.albumService.albumMapper;

import com.fasterxml.jackson.databind.JsonNode;

@FunctionalInterface
public interface SpotifyResponseMapper<T> {

    T map(JsonNode node);
}
