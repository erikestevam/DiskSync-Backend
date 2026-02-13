package com.br.integration.domain.service.albumService.albumMapper;

import com.fasterxml.jackson.databind.JsonNode;

public final class AlbumJsonNodeReader {

    private AlbumJsonNodeReader() {}

    public static String textOrEmpty(JsonNode node, String field) {
        JsonNode value = node.path(field);
        return value.isMissingNode() ? "" : value.asText();
    }

    public static String firstArtistName(JsonNode node) {
        JsonNode artists = node.path("artists");
        if (!artists.isArray() || artists.size() == 0) return "";
        return artists.get(0).path("name").asText("");
    }

    public static String firstImageUrl(JsonNode node) {
        JsonNode images = node.path("images");
        if (!images.isArray() || images.size() == 0) return null;
        return images.get(0).path("url").asText(null);
    }

    public static String spotifyUrl(JsonNode node) {
        return node.path("external_urls").path("spotify").asText("");
    }

    public static int popularity(JsonNode node) {
        return node.path("popularity").asInt(0);
    }

    public static int trackCount(JsonNode node) {
        return node.path("total_tracks").asInt(0);
    }
}
