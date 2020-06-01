package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Config.MapConfig;
import de.Iclipse.BARO.Data;
import de.Iclipse.IMAPI.Database.Server;
import de.Iclipse.IMAPI.IMAPI;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Random;

import static de.Iclipse.BARO.Data.mapConfig;
import static de.Iclipse.BARO.Data.stats;
import static de.Iclipse.IMAPI.IMAPI.copyFilesInDirectory;
import static de.Iclipse.IMAPI.IMAPI.deleteFile;

public class MapLoader implements Listener {
    public MapLoader() {

    }

    public boolean loadDefaultLobby() {
        if (Data.defaultLobby.exists()) {
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
        boolean contains = false;
        for (File file : Data.mapFolder.listFiles()) {
            if (file.getName().equals(name)) {
                contains = true;
            }
            if (Bukkit.getWorld(name) != null) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.teleport(Data.defaultLobbySpawn);
                }
                Bukkit.unloadWorld("world", false);
            }
            copyWorld(file, new File("/world"));
            new WorldCreator("world").createWorld();

            mapConfig = new MapConfig();
            if (!mapConfig.getMapConfigFile().exists()) {
                mapConfig.createDefaultMapConfig();
            }
            mapConfig.readConfig();
            Server.setMap(IMAPI.getServerName(), name);
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
        if (worldDirectory.exists()) {
            deleteFile(worldDirectory);
        }
        File from = new File(worldDirectory + "/region");
        File to = new File(toDirectory.getPath() + "/region");
        try {
            copyFilesInDirectory(from, to);
            Files.copy(new File(worldDirectory + "/level.dat").toPath(), new File(toDirectory.getPath() + "/level.dat").toPath(), StandardCopyOption.REPLACE_EXISTING);
            copyFilesInDirectory(new File(worldDirectory + "/maps"), new File(toDirectory.getPath() + "/maps"));
            Files.copy(new File(worldDirectory + "/config.yml").toPath(), new File(toDirectory.getPath() + "/config.yml").toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onLoad(WorldInitEvent e) {
        if (e.getWorld().getName().equals("world")) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.teleport(Data.mapLobbySpawn);
            }
            stats = new Stats(Data.mapLobbyStats);
        }
    }
}
