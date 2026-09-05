package me.atmosync.weather;

import org.bukkit.World;

public class WeatherController {

    public void applyWeather(World world, WeatherType type) {

        switch (type) {
            case CLEAR -> {
                world.setStorm(false);
                world.setThundering(false);
            }

            case RAIN -> {
                world.setStorm(true);
                world.setThundering(false);
            }

            case SNOW -> {
                world.setStorm(true);
                world.setThundering(false);
            }

            case THUNDER -> {
                world.setStorm(true);
                world.setThundering(true);
            }
        }

    }

}
