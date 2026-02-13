package com.br.integration.domain.service.albumService;

import com.br.integration.domain.dto.AlbumDTO;
import com.br.integration.domain.dto.AlbumDTODetails;
import com.br.integration.domain.service.albumService.albumMapper.AlbumJsonNodeReader;
import com.br.integration.domain.service.albumService.albumMapper.SpotifyAlbumMapper;
import com.br.integration.domain.service.albumService.albumStrategy.AlbumPriceStrategy;
import com.br.integration.domain.service.albumService.albumTemplate.SpotifyFetchTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlbumService {

    private static final String SPOTIFY_ALBUM_BASE = "https://api.spotify.com/v1/albums/";
    private static final String SPOTIFY_SEARCH = "https://api.spotify.com/v1/search?q=%s&type=album";

    private final SpotifyFetchTemplate fetchTemplate;
    private final SpotifyAlbumMapper albumMapper;
    private final AlbumPriceStrategy priceStrategy;

    public AlbumService(SpotifyFetchTemplate fetchTemplate,
                        SpotifyAlbumMapper albumMapper,
                        AlbumPriceStrategy priceStrategy) {
        this.fetchTemplate = fetchTemplate;
        this.albumMapper = albumMapper;
        this.priceStrategy = priceStrategy;
    }

    public ResponseEntity<AlbumDTODetails> getAlbumId(String albumId) throws JsonProcessingException {
        String url = SPOTIFY_ALBUM_BASE + albumId;
        AlbumDTODetails album = fetchTemplate.execute(url, this::mapToAlbumDetails);
        return ResponseEntity.ok(album);
    }

    public ResponseEntity<List<AlbumDTO>> searchAlbum(String query) throws JsonProcessingException {
        String url = String.format(SPOTIFY_SEARCH, query);
        List<AlbumDTO> albums = fetchTemplate.execute(url, this::mapToAlbumList);
        return ResponseEntity.ok(albums);
    }

    private AlbumDTODetails mapToAlbumDetails(JsonNode root) {
        int popularity = AlbumJsonNodeReader.popularity(root);
        int trackCount = AlbumJsonNodeReader.trackCount(root);
        double price = priceStrategy.calculate(popularity, trackCount);
        return albumMapper.toAlbumDTODetails(root, price);
    }

    private List<AlbumDTO> mapToAlbumList(JsonNode root) {
        JsonNode items = root.path("albums").path("items");
        List<AlbumDTO> albums = new ArrayList<>();
        for (JsonNode item : items) {
            albums.add(albumMapper.toAlbumDTO(item));
        }
        return albums;
    }
}
