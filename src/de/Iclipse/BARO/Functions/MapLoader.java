package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Config.MapConfig;
import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.HUD.Map;
import de.Iclipse.IMAPI.Database.ServerManager;
import de.Iclipse.IMAPI.IMAPI;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Random;

import static de.Iclipse.BARO.Data.mapConfig;
import static de.Iclipse.BARO.Data.stats;
import static de.Iclipse.IMAPI.IMAPI.copyFilesInDirectory;
import static de.Iclipse.IMAPI.IMAPI.deleteFile;

public class MapLoader {
    public MapLoader() {
    }

    public boolean loadDefaultLobby() {
        if (Data.defaultLobby.exists()) {
            if (new File("world_the_end").exists()) {
                new File("world_the_end").delete();
            }
            try {
                IMAPI.copyFilesInDirectory(Data.defaultLobby, new File("world_the_end"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                throw new IOException("Default Lobby not found");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean loadMap(String name) {
        System.out.println("Map " + name + " wird geladen!");
        boolean contains = false;
        for (File file : Data.mapFolder.listFiles()) {
            if (file.getName().equals(name)) {
                contains = true;
                if (Bukkit.getWorld("map") != null) {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        Bukkit.getOnlinePlayers().forEach(p -> onlinePlayer.hidePlayer(Data.instance, onlinePlayer));
                        onlinePlayer.teleport(Data.defaultLobbySpawn);
                    }

                    Bukkit.unloadWorld("map", false);
                }
                copyWorld(file, new File(Bukkit.getWorldContainer() + "/map"));

                new WorldCreator("map").createWorld();
                mapConfig = new MapConfig();
                mapConfig.createMapConfig();
                mapConfig.readMapConfig();
                mapConfig.correctLocations();
                stats = new Stats(Data.mapLobbyStats);
                Bukkit.getWorld("map").setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
                Bukkit.getWorld("map").setDifficulty(Difficulty.HARD);
                ServerManager.setMap(IMAPI.getServerName(), name);
                Data.map = new Map(new File(Bukkit.getWorldContainer() + "/map"));

                Bukkit.getScheduler().runTaskLater(Data.instance, () -> {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        Bukkit.getOnlinePlayers().forEach(p -> onlinePlayer.showPlayer(Data.instance, onlinePlayer));
                        onlinePlayer.teleport(Data.mapLobbySpawn);
                        stats.showArmorStands(onlinePlayer);
                    }
                }, 200);
            }
        }
        return contains;
    }


    public boolean loadMap() {
        File[] files = Data.mapFolder.listFiles();
        if (files.length > 0) {
            int random = new Random().nextInt(files.length);
            loadMap(files[random].getName());
            return true;
        }
        return false;
    }


    public void copyWorld(File worldDirectory, File toDirectory) {
        if (toDirectory.exists()) {
            deleteFile(toDirectory);
        }
        File from = new File(worldDirectory + "/region");
        File to = new File(toDirectory.getPath() + "/region");
        try {
            copyFilesInDirectory(from, to);
            Files.copy(new File(worldDirectory + "/level.dat").toPath(), new File(toDirectory.getPath() + "/level.dat").toPath(), StandardCopyOption.REPLACE_EXISTING);
            copyFilesInDirectory(new File(worldDirectory + "/maps"), new File(toDirectory.getPath() + "/maps"));

            File configDir = new File(worldDirectory + "/config");
            if (configDir.isDirectory()) {
                copyFilesInDirectory(configDir, new File(toDirectory.getPath() + "/config"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
