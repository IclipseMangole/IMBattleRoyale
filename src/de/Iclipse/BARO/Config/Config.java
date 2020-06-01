package de.Iclipse.BARO.Config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static de.Iclipse.BARO.Data.*;

public class Config {
    File configFile;
    FileConfiguration fileConfiguration;

    public Config() {
        configFile = new File("plugins/" + instance.getDescription().getName(), "config.yml");
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
    }


    public void setStandardConfig() {
        fileConfiguration.options().copyDefaults(true);
        fileConfiguration.addDefault("mapFolder", "/home/IMNetzwerk/Worlds/BAROMaps");
        fileConfiguration.addDefault("defaultLobby", "/home/IMNetzwerk/Worlds/Lobbys/BAROLobby");
        fileConfiguration.addDefault("defaultLobbySpawn", new Location(Bukkit.getWorld("world_the_end"), 0, 10, 0));
        fileConfiguration.addDefault("defaultCountdown", 100);
        fileConfiguration.addDefault("skipCountdown", 10);
        fileConfiguration.addDefault("teamsize", 2);

        try {
            fileConfiguration.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readConfig() {
        mapFolder = new File(fileConfiguration.getString("mapFolder"));
        defaultLobby = new File(fileConfiguration.getString("defaultLobby"));
        defaultLobbySpawn = fileConfiguration.getLocation("defaultLobbySpawn");
        defaultCountdown = fileConfiguration.getInt("defaultCountdown");
        skipCountdown = fileConfiguration.getInt("skipCountdown");
        teamsize = fileConfiguration.getInt("teamsize");
    }

    public void correctLocations() {
        defaultLobbySpawn.setWorld(Bukkit.getWorld("world_the_end"));
    }
}
