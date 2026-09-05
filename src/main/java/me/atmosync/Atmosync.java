package me.atmosync;

import me.atmosync.command.AtmosyncCommand;
import me.atmosync.command.AtmosyncTabCompleter;
import me.atmosync.location.GeocodingService;
import me.atmosync.location.LocationCoordinates;

import me.atmosync.weather.WeatherController;
import me.atmosync.weather.WeatherData;
import me.atmosync.weather.WeatherService;
import me.atmosync.weather.WeatherSyncTask;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Atmosync extends JavaPlugin {

    private WeatherData currentWeather;
    private WeatherService weatherService;
    private WeatherController weatherController;
    private LocationCoordinates coordinates;
    private World world;
    private BukkitTask weatherTask;

    @Override
    public void onEnable() {
        getLogger().info("Atmosync enabled!");

        saveDefaultConfig();

        startWeatherSync();

        getCommand("atmosync")
                .setExecutor(new AtmosyncCommand(this));

        getCommand("atmosync")
                .setTabCompleter(new AtmosyncTabCompleter());

    }

    @Override
    public void onDisable() {
        getLogger().info("Atmosync disabled!");
    }

    public void setCurrentWeather(WeatherData currentWeather) {
        this.currentWeather = currentWeather;
    }

    public WeatherData getCurrentWeather() {
        return currentWeather;
    }

    public void updateWeather() {
        Bukkit.getScheduler().runTaskAsynchronously(
                this,
                () -> {
                    try {

                        WeatherData weather =
                                weatherService.getWeather(
                                        coordinates.latitude(),
                                        coordinates.longitude()
                                );

                        currentWeather = weather;

                        Bukkit.getScheduler().runTask(
                                this,
                                () -> {
                                    weatherController.applyWeather(
                                            world,
                                            weather.type()
                                    );

                                    getLogger().info(
                                            "Weather manually updated: "
                                            + weather.type()
                                            + ", temperature: "
                                            + weather.temperature()
                                    );
                                }
                        );

                    } catch (Exception e) {
                        getLogger().severe(
                                "Failed to update weather: "
                                + e.getMessage()
                        );
                    }
                }
        );
    }

    private void startWeatherSync() {

        String city = getConfig().getString("location.city");
        String country = getConfig().getString("location.country");

        boolean coordinatesEnabled =
                getConfig().getBoolean("location.coordinates.enabled");

        double latitude =
                getConfig().getDouble("location.coordinates.latitude");

        double longitude =
                getConfig().getDouble("location.coordinates.longitude");

        getLogger().info("Searching for city: " + city);

        GeocodingService geocodingService = new GeocodingService();

        try {

            if (coordinatesEnabled) {

                if ((latitude >= -90 && latitude <= 90) && (longitude >= -180 && longitude <= 180)) {
                    coordinates = new LocationCoordinates(
                            latitude,
                            longitude
                    );

                    getLogger().info(
                            "Using configured coordinates: "
                                    + latitude + ", " + longitude
                    );
                } else {
                    getLogger().severe(
                            "Invalid coordinates.\n"
                                    + "Latitude must be between -90 and 90.\n"
                                    + "Longitude must be between -180 and 180."
                    );

                    return;
                }

            } else {

                coordinates =
                        geocodingService.findCity(city, country);

                getLogger().info(
                        "Location found: "
                                + coordinates.latitude()
                                + ", "
                                + coordinates.longitude()
                );

            }

            weatherService = new WeatherService();

            WeatherData weather =
                    weatherService.getWeather(
                            coordinates.latitude(),
                            coordinates.longitude()
                    );

            currentWeather = weather;

            weatherController =
                    new WeatherController();

            world = Bukkit.getWorld(getConfig().getString("world.name"));

            weatherController.applyWeather(world, weather.type());

            getLogger().info(
                    "Weather: " + weather.type()
                            + ", temperature: " + weather.temperature()
            );

            WeatherSyncTask task = new WeatherSyncTask(
                    weatherService,
                    weatherController,
                    coordinates,
                    world,
                    this
            );

            long updateInterval =
                    getConfig().getLong("weather.update-interval");
            long updateTicks = updateInterval * 20L;

            weatherTask = Bukkit.getScheduler().runTaskTimerAsynchronously(
                    this,
                    task,
                    updateTicks,
                    updateTicks
            );

        } catch (Exception e) {
            getLogger().severe(
                    "Failed to get weather: " + e.getMessage()
            );
        }

    }

    public void reloadWeatherSync() {
        if (weatherTask != null) {
            weatherTask.cancel();
            weatherTask = null;
        }

        reloadConfig();
        startWeatherSync();
    }
}