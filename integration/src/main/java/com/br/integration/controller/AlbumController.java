package com.br.integration.controller;

import com.br.integration.domain.service.albumService.AlbumService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Álbum", description = "Busca e detalhes de álbuns (Spotify)")
@RequiredArgsConstructor
@RestController
@RequestMapping("/album")
public class AlbumController {

    private final AlbumService albumService;

    @Operation(summary = "Buscar álbuns", description = "Busca álbuns na Spotify pelo termo informado")
    @GetMapping("/search")
    public ResponseEntity<?> searchAlbum(@Parameter(description = "Termo de busca") @RequestParam(name = "q") String query) throws JsonProcessingException {
        return albumService.searchAlbum(query);
    }

    @Operation(summary = "Obter álbum por ID", description = "Retorna os detalhes de um álbum pelo ID da Spotify")
    @GetMapping("/{albumId}")
    public ResponseEntity<?> getAlbumId(@Parameter(description = "ID do álbum na Spotify") @PathVariable String albumId) throws JsonProcessingException {
        return albumService.getAlbumId(albumId);
    }
}