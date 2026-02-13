package com.br.integration.domain.service.albumService.albumAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SpotifyApiClientImpl implements SpotifyApiClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private String accessToken;

    @Override
    public String get(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken != null ? accessToken.trim() : "");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
    }
}
