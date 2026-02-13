package com.br.integration.domain.service.albumService.albumStrategy;


@FunctionalInterface
public interface AlbumPriceStrategy {


    double calculate(int popularity, int trackCount);
}
