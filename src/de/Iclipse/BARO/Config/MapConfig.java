package de.Iclipse.BARO.Config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;


public class MapConfig {
    private File mapConfigFile;
    private FileConfiguration mapFileConfiguration;

    public MapConfig() {
        mapConfigFile = new File("world", "config.yml");
        mapFileConfiguration = YamlConfiguration.loadConfiguration(mapConfigFile);
    }

    public void createDefaultMapConfig() {
        mapFileConfiguration.options().copyDefaults(true);
        //Lobby
        mapFileConfiguration.addDefault("lobby.spawn", new Location(Bukkit.getWorld("world"), 152.5, 69, -447.5));
        mapFileConfiguration.addDefault("lobby.middle", new Location(Bukkit.getWorld("world"), 158.5, 69, -470.5));
        mapFileConfiguration.addDefault("lobby.maxDistance", 60);
        mapFileConfiguration.addDefault("lobby.noWater", true);
        mapFileConfiguration.addDefault("lobby.noWaterMinDistance", 0);
        mapFileConfiguration.addDefault("lobby.stats", new Location(Bukkit.getWorld("world"), 0, 100, 0));
        mapFileConfiguration.addDefault("lobby.jumpAndRunReward", 1000);
        //Game
        mapFileConfiguration.addDefault("game.mapScale", 3);
        mapFileConfiguration.
                mapFileConfiguration.addDefault("game.middle", new Location(Bukkit.getWorld("world"), 0, 81, 0));

        try {
            mapFileConfiguration.save(mapConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getMapConfigFile() {
        return mapConfigFile;
    }

    public void readConfig() {

    }

}
