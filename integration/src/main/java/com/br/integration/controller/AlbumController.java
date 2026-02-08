package com.br.integration.controller;

import com.br.integration.domain.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/album")

public class AlbumController {

    private final AlbumService albumService;



    @GetMapping("/search")
    public ResponseEntity<?> searchAlbum(@RequestParam(name = "q") String query) {
        return albumService.searchAlbum(query);
    }

    @GetMapping("/{albumId}")
    public ResponseEntity<?> getAlbumId(@PathVariable String albumId) {
        return albumService.getAlbumId(albumId);
    }
}