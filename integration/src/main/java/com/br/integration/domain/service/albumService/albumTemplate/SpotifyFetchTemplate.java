package com.br.integration.domain.service.albumService.albumTemplate;

import com.br.integration.domain.service.albumService.albumAdapter.SpotifyApiClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class SpotifyFetchTemplate {

    private final SpotifyApiClient apiClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SpotifyFetchTemplate(SpotifyApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public <T> T execute(String url, Function<JsonNode, T> mapResponse) throws JsonProcessingException {
        String json = apiClient.get(url);
        JsonNode root = objectMapper.readTree(json);
        return mapResponse.apply(root);
    }
}
