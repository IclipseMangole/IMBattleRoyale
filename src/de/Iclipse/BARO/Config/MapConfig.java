package de.Iclipse.BARO.Config;

import de.Iclipse.BARO.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class MapConfig {
    private File mapConfigFile;
    private FileConfiguration mapFileConfiguration;

    private File chestFile;
    private FileConfiguration chestFileConfiguration;

    private File villageFile;
    private FileConfiguration villageFileConfiguration;

    public MapConfig() {
        mapConfigFile = new File("map/config", "config.yml");
        mapFileConfiguration = YamlConfiguration.loadConfiguration(mapConfigFile);

        chestFile = new File("map/config", "chest.yml");
        chestFileConfiguration = YamlConfiguration.loadConfiguration(chestFile);

        villageFile = new File("map/config", "village.yml");
        villageFileConfiguration = YamlConfiguration.loadConfiguration(villageFile);
    }


    public void createMapConfig() {
        if (!mapConfigFile.exists()) {
            createDefaultMapConfig();
        }
        if (!chestFile.exists()) {
            createDefaultChests();
        }
        if (!villageFile.exists()) {
            createDefaultVillages();
        }
    }

    public void createDefaultMapConfig() {
        mapFileConfiguration.options().copyDefaults(true);
        //Lobby
        mapFileConfiguration.addDefault("lobby.spawn", new Location(Bukkit.getWorld("map"), 152.5, 69, -447.5));
        mapFileConfiguration.addDefault("lobby.middle", new Location(Bukkit.getWorld("map"), 158.5, 70, -470.5));
        mapFileConfiguration.addDefault("lobby.maxDistance", 60);
        mapFileConfiguration.addDefault("lobby.noWater", true);
        mapFileConfiguration.addDefault("lobby.noWaterMinDistance", 0);
        mapFileConfiguration.addDefault("lobby.stats", new Location(Bukkit.getWorld("map"), 152.5, 70, -447.5));
        mapFileConfiguration.addDefault("lobby.jumpAndRunReward", 1000);
        //Game
        mapFileConfiguration.addDefault("game.mapScale", 3);
        mapFileConfiguration.addDefault("game.spawnHeight", 220);
        mapFileConfiguration.addDefault("game.fallHeight", 180);
        mapFileConfiguration.addDefault("game.aboveGround", 20);
        mapFileConfiguration.addDefault("game.middle", new Location(Bukkit.getWorld("map"), 0, 81, 0));
        mapFileConfiguration.addDefault("game.firstRadius", 450);
        mapFileConfiguration.addDefault("game.progressPerSecond", 0.0075);
        mapFileConfiguration.addDefault("game.newRadius", 0.6);
        mapFileConfiguration.addDefault("game.chestAmount", 50);
        mapFileConfiguration.addDefault("game.dropHeight", 70);
        mapFileConfiguration.addDefault("game.noLobbyIsland", true);
        mapFileConfiguration.addDefault("game.eventTimeRunning", 60);
        mapFileConfiguration.addDefault("game.timeBetweenEventsMin", 180);
        mapFileConfiguration.addDefault("game.timeBetweenEventsMax", 280);
        mapFileConfiguration.addDefault("game.eventsOneTime", true);


        try {
            mapFileConfiguration.save(mapConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void createDefaultChests() {
        chestFileConfiguration.options().copyDefaults(true);
        ArrayList<Location> chestlist = new ArrayList<>();
        chestlist.add(new Location(Bukkit.getWorld("map"), -136, 79, 131));
        for (int i = 0; i < chestlist.size(); i++) {
            chestFileConfiguration.addDefault("chest" + i, chestlist.get(i));
        }
        try {
            chestFileConfiguration.save(chestFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createDefaultVillages() {
        villageFileConfiguration.options().copyDefaults(true);
        HashMap<String, Location> villages = new HashMap<>();
        villages.put("TiltedTowers", new Location(Bukkit.getWorld("map"), 0, 81, 0));


        final int[] i = {0};
        villages.forEach((name, location) -> {
            villageFileConfiguration.addDefault("village" + i[0] + ".name", name);
            villageFileConfiguration.addDefault("village" + i[0] + ".location", location);
            i[0]++;
        });
        try {
            villageFileConfiguration.save(villageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readMapConfig() {
        readConfig();
        readChests();
        readVillages();
    }


    public void readConfig() {
        //Lobby
        Data.mapLobbySpawn = mapFileConfiguration.getLocation("lobby.spawn");
        Data.mapLobbyMiddle = mapFileConfiguration.getLocation("lobby.middle");
        Data.mapLobbyMaxDistance = mapFileConfiguration.getInt("lobby.maxDistance");
        Data.noWater = mapFileConfiguration.getBoolean("lobby.noWater");
        Data.noWaterMinDistance = mapFileConfiguration.getInt("lobby.noWaterMinDistance");
        Data.mapLobbyStats = mapFileConfiguration.getLocation("lobby.stats");
        Data.jumpAndRunReward = mapFileConfiguration.getInt("lobby.jumpAndRunReward");

        //Game
        Data.mapScale = mapFileConfiguration.getInt("game.mapScale");
        Data.spawnHeight = mapFileConfiguration.getInt("game.spawnHeight");
        Data.aboveGround = mapFileConfiguration.getInt("game.aboveGround");
        Data.fallHeight = mapFileConfiguration.getInt("game.fallHeight");
        Data.middle = mapFileConfiguration.getLocation("game.middle");
        Data.firstRadius = mapFileConfiguration.getInt("game.firstRadius");
        Data.progressPerSecond = mapFileConfiguration.getDouble("game.progressPerSecond");
        Data.newRadius = mapFileConfiguration.getDouble("game.newRadius");
        Data.chestAmount = mapFileConfiguration.getInt("game.chestAmount");
        Data.dropHeight = mapFileConfiguration.getInt("game.dropHeight");
        Data.noLobbyIsland = mapFileConfiguration.getBoolean("game.noLobbyIsland");
        Data.eventTimeRunning = mapFileConfiguration.getInt("game.eventTimeRunning");
        Data.timeBetweenEventsMin = mapFileConfiguration.getInt("game.timeBetweenEventsMin");
        Data.timeBetweenEventsMax = mapFileConfiguration.getInt("game.timeBetweenEventsMax");
        Data.eventsOneTime = mapFileConfiguration.getBoolean("game.eventsOneTime");


    }

    public void readChests() {
        ArrayList<Location> chests = new ArrayList<>();
        int i = 0;
        while (chestFileConfiguration.contains("chest" + i)) {
            chests.add(chestFileConfiguration.getLocation("chest" + i));
            i++;
        }
        Data.chests = chests;
    }

    public void readVillages() {
        HashMap<String, Location> villages = new HashMap<>();
        int i = 0;
        while (villageFileConfiguration.contains("village" + i + ".name")) {
            villages.put(villageFileConfiguration.getString("village" + i + ".name"), villageFileConfiguration.getLocation("village" + i + ".location"));
            i++;
        }
        Data.villages = villages;
    }

    public void correctLocations() {
        World map = Bukkit.getWorld("map");
        Data.mapLobbySpawn.setWorld(map);
        Data.mapLobbyMiddle.setWorld(map);
        Data.mapLobbyStats.setWorld(map);
        Data.middle.setWorld(map);
        Data.chests.forEach(loc -> loc.setWorld(map));
        Data.villages.forEach((name, location) -> location.setWorld(map));
    }



}
