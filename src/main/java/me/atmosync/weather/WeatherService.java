package me.atmosync.weather;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherService {

    private final HttpClient httpClient;

    public WeatherService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public WeatherData getWeather(double latitude, double longitude)
        throws IOException, InterruptedException {

        String url = "https://api.open-meteo.com/v1/forecast"
                + "?latitude=" + latitude
                + "&longitude=" + longitude
                + "&current=temperature_2m,weather_code";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new IOException(
                    "Weather API returned HTTP " + response.statusCode()
            );
        }

        Gson gson = new Gson();

        WeatherResponse data = gson.fromJson(
                response.body(),
                WeatherResponse.class
        );

        double temperature = data.current().temperature_2m();
        int weatherCode = data.current().weather_code();
        WeatherType type = parseWeatherCode(weatherCode);

        return new WeatherData(
                type,
                temperature
        );

    }

    private WeatherType parseWeatherCode(int weatherCode) {

        if (weatherCode >= 71 && weatherCode <= 77 || weatherCode == 85 || weatherCode == 86) {
            return WeatherType.SNOW;
        }

        switch (weatherCode) {
            case 0, 1, 2, 3:
                return WeatherType.CLEAR;

            case 51, 53, 55, 56, 57,
                 61, 63, 65, 66, 67,
                 80, 81, 82:
                return WeatherType.RAIN;

            case 95, 96, 99:
                return WeatherType.THUNDER;

            default:
                return WeatherType.CLEAR;
        }

    }

}
