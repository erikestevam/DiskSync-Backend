package com.br.integration.controller;

import com.br.integration.domain.service.AlbumService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/album")

public class AlbumController {

    private final AlbumService albumService;

    @GetMapping("/search")
    public ResponseEntity<?> searchAlbum(@RequestParam(name = "q") String query) throws JsonProcessingException {
        return albumService.searchAlbum(query);
    }

    @GetMapping("/{albumId}")
    public ResponseEntity<?> getAlbumId(@PathVariable String albumId) throws JsonProcessingException {
        return albumService.getAlbumId(albumId);
    }
}