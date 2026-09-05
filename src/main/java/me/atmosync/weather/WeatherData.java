package me.atmosync.weather;

public record WeatherData (
        WeatherType type,
        double temperature
) {
}
