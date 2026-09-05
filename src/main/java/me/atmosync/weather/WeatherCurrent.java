package me.atmosync.weather;

public record WeatherCurrent(
        double temperature_2m,
        int weather_code
) {
}
