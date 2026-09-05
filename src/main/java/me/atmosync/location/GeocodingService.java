package me.atmosync.location;

import com.google.gson.Gson;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class GeocodingService {

    private final HttpClient httpClient;

    public GeocodingService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public LocationCoordinates findCity(
            String city,
            String country
    )
            throws IOException, InterruptedException {

        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);

        String encodedCountry = URLEncoder.encode(country, StandardCharsets.UTF_8);

        String url = "https://geocoding-api.open-meteo.com/v1/search"
                + "?name=" + encodedCity
                + "&count=10"
                + "&language=en"
                + "&format=json";

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
                    "Geocoding API returned HTTP "
                            + response.statusCode()
            );
        }

        Gson gson = new Gson();

        GeocodingResponse data = gson.fromJson(
                response.body(),
                GeocodingResponse.class
        );

        if (data.results() == null || data.results().length == 0) {
            throw new IOException(
                    "City not found: " + city
            );
        }

        for (GeocodingResult result : data.results()) {

            if (result.country().equalsIgnoreCase(country)) {
                return new LocationCoordinates(
                        result.latitude(),
                        result.longitude()
                );
            }
        }

        throw new IOException(
                "City '" + city + "' not found in country '" + country + "'"
        );

    }

}
