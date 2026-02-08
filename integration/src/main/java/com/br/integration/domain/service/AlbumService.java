package com.br.integration.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;


@Service
public class AlbumService {
    @Autowired
    private String accessToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<?> getAlbumId(String albumId) {
        String url = "https://api.spotify.com/v1/albums/"+albumId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken.trim());

        HttpEntity<String> entity = new HttpEntity<>(headers);


        return  restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    public ResponseEntity<?> searchAlbum(String query) {
        String url = "https://api.spotify.com/v1/search?q=" + query + "&type=album";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken.trim());


        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }
}
