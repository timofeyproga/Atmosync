package me.atmosync.location;

public record GeocodingResult(
        String name,
        String country,
        Double latitude,
        Double longitude
) {
}
