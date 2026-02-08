package com.br.integration.config.spotify;

import com.br.integration.domain.dto.spotifydto.LoginResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Configuration
public class SpotifyConfig {
    @Value("${api.spotify.client-id}")
    private String clientId;
    @Value("${api.spotify.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    @Bean
    public String accessToken() {
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encodedAuth);
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        String body = "grant_type=client_credentials";
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        String tokenUrl = "https://accounts.spotify.com/api/token";
        ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        LoginResponseDTO tokenResponse;
        try {
            tokenResponse = objectMapper.readValue(response.getBody(), LoginResponseDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse access token response");
        }
        return tokenResponse.getAccessToken();
    }
}
