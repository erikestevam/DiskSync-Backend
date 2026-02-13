package com.br.integration.domain.service.albumService.albumStrategy;

import org.springframework.stereotype.Component;


@Component
public class PopularityAndCountTrackerAlbumPriceStrategy implements AlbumPriceStrategy {

    private static final double PRICE_PER_TRACK = 2.0;

    @Override
    public double calculate(int popularity, int trackCount) {
        double base = getBasePriceByPopularity(popularity);
        return base + (trackCount * PRICE_PER_TRACK);
    }

    private double getBasePriceByPopularity(int popularity) {
        if (popularity <= 20) return 50.0;
        if (popularity <= 40) return 70.0;
        if (popularity <= 60) return 90.0;
        if (popularity <= 80) return 120.0;
        return 150.0;
    }
}
