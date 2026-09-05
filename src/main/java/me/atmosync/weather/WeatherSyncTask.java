package me.atmosync.weather;

import me.atmosync.Atmosync;
import me.atmosync.location.LocationCoordinates;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class WeatherSyncTask implements Runnable {

    private final WeatherService weatherService;
    private final WeatherController weatherController;
    private final LocationCoordinates coordinates;
    private final World world;
    private final Atmosync plugin;

    public WeatherSyncTask(
            WeatherService weatherService,
            WeatherController weatherController,
            LocationCoordinates coordinates,
            World world,
            Atmosync plugin
    ) {
       this.coordinates = coordinates;
       this.weatherController = weatherController;
       this.weatherService = weatherService;
       this.world = world;
       this.plugin = plugin;
    }

    @Override
    public void run() {
        try {
            WeatherData weather = weatherService.getWeather(
                    coordinates.latitude(),
                    coordinates.longitude()
            );

            plugin.setCurrentWeather(weather);

            Bukkit.getScheduler().runTask(
                    plugin,
                    () -> {
                        weatherController.applyWeather(
                                world,
                                weather.type()
                        );

                        plugin.getLogger().info(
                                "Weather updated: "
                                + weather.type()
                                + ", temperature: "
                                + weather.temperature()
                        );
                    }
            );
        } catch (Exception e) {
            plugin.getLogger().severe(
                    "Failed to get weather: " + e.getMessage() + ", default weather enabled!"
            );
        }
    }

}
