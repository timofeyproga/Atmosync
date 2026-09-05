package me.atmosync.command;

import me.atmosync.Atmosync;
import me.atmosync.weather.WeatherData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class AtmosyncCommand implements CommandExecutor {

    private final Atmosync plugin;

    public AtmosyncCommand(Atmosync plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (args.length == 0) {
            sender.sendMessage("Usage: /atmosync <status/update/reload>");
            return true;
        }

        if (args[0].equalsIgnoreCase("status")) {

            String city = plugin.getConfig().getString("location.city");
            String country = plugin.getConfig().getString("location.country");
            String worldName = plugin.getConfig().getString("world.name");

            boolean coordinatesEnabled = plugin.getConfig().getBoolean("location.coordinates.enabled");

            sender.sendMessage("§6§lAtmosync");
            sender.sendMessage("§7Location: §f" + city + ", " + country);
            sender.sendMessage("§7World: §f" + worldName);
            sender.sendMessage(
                    "§7Coordinates mode: §f"
                    + (coordinatesEnabled ? "Manual" : "City search")
            );

            WeatherData weather = plugin.getCurrentWeather();

            if (weather == null) {
                sender.sendMessage("§cWeather data is not available yet.");
                return true;
            }

            sender.sendMessage("§7Weather: §f" + weather.type());
            sender.sendMessage("§7Temperature: §f" + weather.temperature() + "°C");

            return true;

        } else if (args[0].equalsIgnoreCase("update")) {

            if (!sender.isOp()) {
                sender.sendMessage("§cYou must be an operator to use this command.");
                return true;
            }

            plugin.updateWeather();

            sender.sendMessage(
                    "§aWeather updated!"
            );

            return true;

        } else if (args[0].equalsIgnoreCase("reload")) {

            if (!sender.isOp()) {
                sender.sendMessage("§cYou must be an operator to use this command.");
                return true;
            }

            plugin.reloadWeatherSync();

            sender.sendMessage("§aAtmosync configuration reloaded.");

            return true;

        } else {
            sender.sendMessage("§cArgument not found!");

            return true;
        }

    }

}
