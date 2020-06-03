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
        mapFileConfiguration.addDefault("lobby.stats", new Location(Bukkit.getWorld("world"), 152.5, 70, -447.5));
        mapFileConfiguration.addDefault("lobby.jumpAndRunReward", 1000);
        //Game
        mapFileConfiguration.addDefault("game.mapScale", 3);
        mapFileConfiguration.addDefault("game.spawnHeight", 220);
        mapFileConfiguration.addDefault("game.fallHeight", 180);
        mapFileConfiguration.addDefault("game.middle", new Location(Bukkit.getWorld("world"), 0, 81, 0));
        mapFileConfiguration.addDefault("game.firstRadius", 450);
        mapFileConfiguration.addDefault("game.progressPerSecond", 0.01);
        mapFileConfiguration.addDefault("game.newRadius", 0.6);
        mapFileConfiguration.addDefault("game.chestAmount", 50);
        addDefaultChests();
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


    public void addDefaultChests() {
        ArrayList<Location> chestlist = new ArrayList<>();
        chestlist.add(new Location(Bukkit.getWorld("world"), -136, 79, 131));
        chestlist.add(new Location(Bukkit.getWorld("world"), -137, 79, 129));
        chestlist.add(new Location(Bukkit.getWorld("world"), 95, 80, 67));
        chestlist.add(new Location(Bukkit.getWorld("world"), 90, 80, 71));
        chestlist.add(new Location(Bukkit.getWorld("world"), 154, 79, 91));
        chestlist.add(new Location(Bukkit.getWorld("world"), 353, 65, 83));
        chestlist.add(new Location(Bukkit.getWorld("world"), 351, 65, 60));
        chestlist.add(new Location(Bukkit.getWorld("world"), 244, 71, 204));
        chestlist.add(new Location(Bukkit.getWorld("world"), 244, 76, 195));
        chestlist.add(new Location(Bukkit.getWorld("world"), 155, 69, 241));
        chestlist.add(new Location(Bukkit.getWorld("world"), 134, 81, 238));
        chestlist.add(new Location(Bukkit.getWorld("world"), 124, 67, 233));
        chestlist.add(new Location(Bukkit.getWorld("world"), -79, 65, 352));
        chestlist.add(new Location(Bukkit.getWorld("world"), -60, 72, 295));
        chestlist.add(new Location(Bukkit.getWorld("world"), -33, 64, 306));
        chestlist.add(new Location(Bukkit.getWorld("world"), -242, 92, 231));
        chestlist.add(new Location(Bukkit.getWorld("world"), -241, 80, 231));
        chestlist.add(new Location(Bukkit.getWorld("world"), -244, 74, 229));
        chestlist.add(new Location(Bukkit.getWorld("world"), -306, 65, 106));
        chestlist.add(new Location(Bukkit.getWorld("world"), -302, 64, 110));
        chestlist.add(new Location(Bukkit.getWorld("world"), -360, 63, -74));
        chestlist.add(new Location(Bukkit.getWorld("world"), -364, 72, -71));
        chestlist.add(new Location(Bukkit.getWorld("world"), -349, 63, -67));
        chestlist.add(new Location(Bukkit.getWorld("world"), -175, 63, -266));
        chestlist.add(new Location(Bukkit.getWorld("world"), -190, 66, -259));
        chestlist.add(new Location(Bukkit.getWorld("world"), -154, 83, -83));
        chestlist.add(new Location(Bukkit.getWorld("world"), -158, 83, -86));
        chestlist.add(new Location(Bukkit.getWorld("world"), -168, 82, -90));
        chestlist.add(new Location(Bukkit.getWorld("world"), 337, 65, 34));
        chestlist.add(new Location(Bukkit.getWorld("world"), 296, 69, -65));
        chestlist.add(new Location(Bukkit.getWorld("world"), 298, 65, -63));
        chestlist.add(new Location(Bukkit.getWorld("world"), 211, 74, -145));
        chestlist.add(new Location(Bukkit.getWorld("world"), 212, 71, -145));
        chestlist.add(new Location(Bukkit.getWorld("world"), 226, 70, -151));
        chestlist.add(new Location(Bukkit.getWorld("world"), 199, 70, -166));
        chestlist.add(new Location(Bukkit.getWorld("world"), 199, 74, -157));
        chestlist.add(new Location(Bukkit.getWorld("world"), 216, 70, -165));
        chestlist.add(new Location(Bukkit.getWorld("world"), 211, 73, -165));
        chestlist.add(new Location(Bukkit.getWorld("world"), 178, 64, -342));
        chestlist.add(new Location(Bukkit.getWorld("world"), 178, 62, -339));
        chestlist.add(new Location(Bukkit.getWorld("world"), 193, 53, -371));
        chestlist.add(new Location(Bukkit.getWorld("world"), -5, 63, -35));
        chestlist.add(new Location(Bukkit.getWorld("world"), -10, 70, -41));
        chestlist.add(new Location(Bukkit.getWorld("world"), -56, 81, -108));
        chestlist.add(new Location(Bukkit.getWorld("world"), -51, 92, -124));
        chestlist.add(new Location(Bukkit.getWorld("world"), -58, 77, -120));
        chestlist.add(new Location(Bukkit.getWorld("world"), -306, 78, -124));
        chestlist.add(new Location(Bukkit.getWorld("world"), 16, 64, 331));
        chestlist.add(new Location(Bukkit.getWorld("world"), 234, 70, 112));
        chestlist.add(new Location(Bukkit.getWorld("world"), 297, 64, 157));
        chestlist.add(new Location(Bukkit.getWorld("world"), 86, 99, -32));
        chestlist.add(new Location(Bukkit.getWorld("world"), 89, 88, -33));
        chestlist.add(new Location(Bukkit.getWorld("world"), -17, 80, 47));
        chestlist.add(new Location(Bukkit.getWorld("world"), -22, 80, 49));
        chestlist.add(new Location(Bukkit.getWorld("world"), 14, 125, 176));
        chestlist.add(new Location(Bukkit.getWorld("world"), 13, 96, 197));
        chestlist.add(new Location(Bukkit.getWorld("world"), 1, 109, 165));
        chestlist.add(new Location(Bukkit.getWorld("world"), 29, 88, 181));
        chestlist.add(new Location(Bukkit.getWorld("world"), -211, 81, 45));
        chestlist.add(new Location(Bukkit.getWorld("world"), -205, 79, 50));
        for (int i = 0; i < chestlist.size(); i++) {
            mapFileConfiguration.addDefault("game.chests.chest" + i, chestlist.get(i));
        }
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
        Data.fallHeight = mapFileConfiguration.getInt("game.fallHeight");
        Data.middle = mapFileConfiguration.getLocation("game.middle");
        Data.firstRadius = mapFileConfiguration.getInt("game.firstRadius");
        Data.progressPerSecond = mapFileConfiguration.getDouble("game.progressPerSecond");
        Data.newRadius = mapFileConfiguration.getDouble("game.newRadius");
        Data.chests = readChests();
        Data.dropHeight = mapFileConfiguration.getInt("game.dropHeight");
        Data.noLobbyIsland = mapFileConfiguration.getBoolean("game.noLobbyIsland");
        Data.eventTimeRunning = mapFileConfiguration.getInt("game.eventTimeRunning");
        Data.timeBetweenEventsMin = mapFileConfiguration.getInt("game.timeBetweenEventsMin");
        Data.timeBetweenEventsMax = mapFileConfiguration.getInt("game.timeBetweenEventsMax");
        Data.eventsOneTime = mapFileConfiguration.getBoolean("game.eventsOneTime");


    }

    public ArrayList<Location> readChests() {
        ArrayList<Location> chests = new ArrayList<>();
        int i = 0;
        while (mapFileConfiguration.contains("game.chests.chest" + i)) {
            chests.add(mapFileConfiguration.getLocation("game.chests.chest" + i));
            i++;
        }
        return chests;
    }

    public void correctLocations() {
        World world = Bukkit.getWorld("world");
        Data.mapLobbySpawn.setWorld(world);
        Data.mapLobbyMiddle.setWorld(world);
        Data.mapLobbyStats.setWorld(world);
        Data.middle.setWorld(world);
        Data.chests.forEach(loc -> loc.setWorld(world));
    }


    public File getMapConfigFile() {
        return mapConfigFile;
    }


}
