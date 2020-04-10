package de.Iclipse.BARO;

import de.Iclipse.BARO.Functions.GameState;
import de.Iclipse.BARO.Functions.Scheduler;
import de.Iclipse.BARO.Functions.Tablist;
import de.Iclipse.BARO.Listener.LobbyListener;
import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.Dispatching.Dispatcher;
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
        createTeams();
        Scheduler.startScheduler();
    }

    @Override
    public void onDisable() {
        Scheduler.stopScheduler();
        super.onDisable();
    }

    public void registerListener() {
        IMAPI.register(new LobbyListener(), this);
    }

    public void registerCommands() {
    }

    public void createTables() {
        createBAROGamesTable();
        createBAROStatsTable();
    }


    public void loadMap() {
        File from = new File("/home/IMNetzwerk/BuildServer/BAROMap_world/region");
        File to = new File(Data.instance.getDataFolder().getAbsoluteFile().getParentFile().getParentFile().getAbsolutePath() + "/world/region");
        if (to.exists()) {
            to.delete();
        }
        try {
            copyFilesInDirectory(from, to);
            Files.copy(new File("/home/IMNetzwerk/BuildServer/BAROMap_world/level.dat").toPath(), new File(Data.instance.getDataFolder().getAbsoluteFile().getParentFile().getParentFile().getAbsolutePath() + "/world/level.dat").toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadResourceBundles(){
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
