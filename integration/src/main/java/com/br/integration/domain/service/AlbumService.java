package com.br.integration.domain.service;

import com.br.integration.domain.dto.AlbumDTO;
import com.br.integration.domain.dto.AlbumDTODetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Service
public class AlbumService {
    @Autowired
    private String accessToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<AlbumDTODetails> getAlbumId(String albumId) throws JsonProcessingException {
        String url = "https://api.spotify.com/v1/albums/" + albumId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken.trim());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());

        String id = root.path("id").asText();
        String name = root.path("name").asText();

        String artist = "";
        if (root.path("artists").isArray() && root.path("artists").size() > 0) {
            artist = root.path("artists").get(0).path("name").asText();
        }

        String releaseDate = root.path("release_date").asText();

        String imageUrl = null;
        if (root.path("images").isArray() && root.path("images").size() > 0) {
            imageUrl = root.path("images").get(0).path("url").asText();
        }

        String spotifyUrl = root.path("external_urls").path("spotify").asText();

        int popularity = root.path("popularity").asInt();
        int trackCount = root.path("total_tracks").asInt();

        double price = calculatePrice(popularity, trackCount);

        AlbumDTODetails album = new AlbumDTODetails(
                id,
                name,
                artist,
                releaseDate,
                imageUrl,
                spotifyUrl,
                popularity,
                trackCount,
                price
        );

        return ResponseEntity.ok(album);
    }

    public ResponseEntity<List<AlbumDTO>> searchAlbum(String query) throws JsonProcessingException {
        String url = "https://api.spotify.com/v1/search?q=" + query + "&type=album";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken.trim());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        JsonNode items = root.path("albums").path("items");

        List<AlbumDTO> albums = new ArrayList<>();

        for (JsonNode item : items) {
            String id = item.path("id").asText();
            String name = item.path("name").asText();

            String artist = "";
            if (item.path("artists").isArray() && item.path("artists").size() > 0) {
                artist = item.path("artists").get(0).path("name").asText();
            }

            String releaseDate = item.path("release_date").asText();

            String imageUrl = null;
            if (item.path("images").isArray() && item.path("images").size() > 0) {
                imageUrl = item.path("images").get(0).path("url").asText();
            }

            String spotifyUrl = item.path("external_urls").path("spotify").asText();

            albums.add(new AlbumDTO(id, name, artist, releaseDate, imageUrl, spotifyUrl));
        }

        return ResponseEntity.ok(albums);
    }

    private  double calculatePrice(int popularity, int trackCount) {
        double base;
        if (popularity <= 20) base = 50.0;
        else if (popularity <= 40) base = 70.0;
        else if (popularity <= 60) base = 90.0;
        else if (popularity <= 80) base = 120.0;
        else base = 150.0;

        return base + (trackCount * 2.0);

    }

}
