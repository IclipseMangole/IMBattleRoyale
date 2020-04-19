package de.Iclipse.BARO;

import de.Iclipse.BARO.Commands.cmd_pause;
import de.Iclipse.BARO.Commands.cmd_start;
import de.Iclipse.BARO.Commands.cmd_teamsize;
import de.Iclipse.BARO.Functions.*;
import de.Iclipse.BARO.Listener.LobbyListener;
import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.Dispatching.Dispatcher;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static de.Iclipse.BARO.Data.*;
import static de.Iclipse.BARO.Database.BAROGames.createBAROGamesTable;
import static de.Iclipse.BARO.Database.BAROStats.createBAROStatsTable;
import static de.Iclipse.BARO.Functions.TeamManager.createTeams;
import static de.Iclipse.IMAPI.IMAPI.copyFilesInDirectory;

public class BARO extends JavaPlugin {
    @Override
    public void onLoad() {
        super.onLoad();
        Data.instance = this;
        loadMap();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        registerListener();
        registerCommands();
        createTables();
        loadResourceBundles();
        Data.state = GameState.Lobby;
        tablist = new Tablist();
        Map.loadMap();
        createTeams();
        Scheduler.startScheduler();
        Bukkit.getWorld("world").setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
    }

    @Override
    public void onDisable() {
        Scheduler.stopScheduler();
        super.onDisable();
    }

    public void registerListener() {
        IMAPI.register(new LobbyListener(), this);
        IMAPI.register(new PlayerSpawns(), this);
        IMAPI.register(new BorderManager(), this);
        IMAPI.register(new Map(), this);
        IMAPI.register(new GameFinish(), this);
    }

    public void registerCommands() {
        IMAPI.register(new cmd_start(), this);
        IMAPI.register(new cmd_teamsize(), this);
        IMAPI.register(new cmd_pause(), this);
    }

    public void createTables() {
        createBAROGamesTable();
        createBAROStatsTable();
    }


    public void loadMap() {
        if (new File(Bukkit.getWorldContainer().getAbsolutePath() + "/world").exists()) {
            deleteFolder(new File(Bukkit.getWorldContainer().getAbsolutePath() + "/world"));
        }
        File from = new File("/home/IMNetzwerk/BuildServer/BAROMap_world/region");
        File to = new File(Data.instance.getDataFolder().getAbsoluteFile().getParentFile().getParentFile().getAbsolutePath() + "/world/region");
        if (to.exists()) {
            to.delete();
        }
        try {
            copyFilesInDirectory(from, to);
            Files.copy(new File("/home/IMNetzwerk/BuildServer/BAROMap_world/level.dat").toPath(), new File(Data.instance.getDataFolder().getAbsoluteFile().getParentFile().getParentFile().getAbsolutePath() + "/world/level.dat").toPath(), StandardCopyOption.REPLACE_EXISTING);
            copyFilesInDirectory(new File("/home/IMNetzwerk/BuildServer/BAROMap_world/maps"), new File(Data.instance.getDataFolder().getAbsoluteFile().getParentFile().getParentFile().getAbsolutePath() + "/world/maps"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFolder(File file) {
        if (file.listFiles().length > 0) {
            for (File listFile : file.listFiles()) {
                if (listFile.isDirectory()) {
                    deleteFolder(listFile);
                } else {
                    listFile.delete();
                }
            }
        }
        file.delete();
    }

    public void loadResourceBundles() {
        try {
            HashMap<String, ResourceBundle> langs = new HashMap<>();
            langDE = ResourceBundle.getBundle("i18n.langDE");
            langEN = ResourceBundle.getBundle("i18n.langEN");
            langs.put("DE", langDE);
            langs.put("EN", langEN);
            Data.dsp = new Dispatcher(this,
                    langs);
        } catch(MissingResourceException e){
            e.printStackTrace();
            de.Iclipse.IMAPI.Data.dispatching = false;
        } catch(NullPointerException e){
            e.printStackTrace();
            System.out.println("Reload oder Bundle not found!");
            de.Iclipse.IMAPI.Data.dispatching = false;
        }
    }







}
