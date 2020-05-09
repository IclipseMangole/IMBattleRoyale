package de.Iclipse.BARO;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
    public static File getConfigFile() {
        return new File("plugins/" + Data.instance.getDescription().getName(), "config.yml");
    }

    public static FileConfiguration getConfigFileConfiguration() {
        return YamlConfiguration.loadConfiguration(getConfigFile());
    }

    public static void setStandardConfig() {
        FileConfiguration cfg = getConfigFileConfiguration();
        cfg.options().copyDefaults(true);
        cfg.addDefault("teamsize", 1);
        cfg.addDefault("spawn", new Location(Bukkit.getWorld("world"), 145.5, 69.5, -447.5, -90.0f, 0.0f));
        cfg.addDefault("defaultcountdown", 100);
        cfg.addDefault("chests", 60);
        cfg.addDefault("progressPerSecond", 0.0025);
        cfg.addDefault("newRadius", 0.6);
        cfg.addDefault("worldFile", "/home/IMNetzwerk/BuildServer/BAROMap_world");
        cfg.addDefault("event.timeRunning", 60);
        cfg.addDefault("event.timeBetweenMin", 180);
        cfg.addDefault("event.timeBetweenMax", 280);
        cfg.addDefault("event.oneTime", true);
        try {
            cfg.save(getConfigFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readConfig() {
        FileConfiguration cfg = getConfigFileConfiguration();
        Data.teamsize = cfg.getInt("teamsize");
        Data.minplayers = Data.teamsize + 1;
        Data.spawn = cfg.getLocation("spawn");
        Data.defaultcountdown = cfg.getInt("defaultcountdown");
        Data.chests = cfg.getInt("chests");
        Data.progressPerSecond = cfg.getDouble("progressPerSecond");
        Data.newRadius = cfg.getDouble("newRadius");
        Data.worldFile = new File(cfg.getString("worldFile"));
        Data.eventTimeRunning = cfg.getInt("event.timeRunning");
        Data.timeBetweenEventsMin = cfg.getInt("event.timeBetweenMin");
        Data.timeBetweenEventsMax = cfg.getInt("event.timeBetweenMax");
        Data.eventsOneTime = cfg.getBoolean("event.oneTime");
    }

    public static void correctLocations() {
        Data.spawn.setWorld(Bukkit.getWorld("world"));
    }
}
