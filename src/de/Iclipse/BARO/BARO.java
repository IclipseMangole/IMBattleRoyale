package de.Iclipse.BARO;

import de.Iclipse.BARO.Commands.cmd_maxPlayerBars;
import de.Iclipse.BARO.Commands.cmd_pause;
import de.Iclipse.BARO.Commands.cmd_start;
import de.Iclipse.BARO.Commands.cmd_teamsize;
import de.Iclipse.BARO.Functions.Border.BorderManager;
import de.Iclipse.BARO.Functions.Border.Map;
import de.Iclipse.BARO.Functions.Chests.Chests;
import de.Iclipse.BARO.Functions.Chests.Item;
import de.Iclipse.BARO.Functions.Chests.LootDrops;
import de.Iclipse.BARO.Functions.HUD.BossBar;
import de.Iclipse.BARO.Functions.HUD.Scoreboard;
import de.Iclipse.BARO.Functions.HUD.Tablist;
import de.Iclipse.BARO.Functions.*;
import de.Iclipse.BARO.Functions.PlayerManagement.UserStats;
import de.Iclipse.BARO.Functions.States.GameState;
import de.Iclipse.BARO.Functions.States.Lobby;
import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.Dispatching.Dispatcher;
import de.Iclipse.IMAPI.Util.SkullUtils;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
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
import static de.Iclipse.BARO.Functions.PlayerManagement.TeamManager.createTeams;
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
        Bukkit.getWorld("world").setDifficulty(Difficulty.HARD);
        loadCustomHeads();
        Item.loadItems();
        Chests.loadChests();
    }

    @Override
    public void onDisable() {
        Scheduler.stopScheduler();
        super.onDisable();
    }

    public void registerListener() {
        IMAPI.register(new Lobby(), this);
        IMAPI.register(new PlayerSpawns(), this);
        IMAPI.register(new BorderManager(), this);
        IMAPI.register(new Map(), this);
        IMAPI.register(new UserStats(), this);
        IMAPI.register(new PlayerDeathQuit(), this);
        IMAPI.register(new UserStats(), this);
        IMAPI.register(new Knocked(), this);
        IMAPI.register(new Spectator(), this);
        IMAPI.register(new Scoreboard(), this);
        IMAPI.register(new cmd_maxPlayerBars(), this);
        IMAPI.register(new LootDrops(), this);
        IMAPI.register(new Chests(), this);
        IMAPI.register(new BossBar(), this);
        IMAPI.register(new LastDamage(), this);
        IMAPI.register(new Watcher(), this);
    }

    public void registerCommands() {
        IMAPI.register(new cmd_start(), this);
        IMAPI.register(new cmd_teamsize(), this);
        IMAPI.register(new cmd_pause(), this);
        IMAPI.register(new cmd_maxPlayerBars(), this);
    }

    /*
    public void registerPacketListener(){
        protocolManager.addPacketListener(new PacketAdapter(this,
                ListenerPriority.NORMAL,
                PacketType.Play.Client.CHAT) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Knocked.onPacketReceiving(event);
            }
        });
    }
     */

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
        } catch (MissingResourceException e) {
            e.printStackTrace();
            de.Iclipse.IMAPI.Data.dispatching = false;
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("Reload oder Bundle not found!");
            de.Iclipse.IMAPI.Data.dispatching = false;
        }
    }

    public void loadCustomHeads() {
        heads.put("settings", SkullUtils.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmE3MjUzYjAyNTUwMTNlNmYyNjhkNWM0NzkxMTk3NTY2ZTYyZmU5ZDI3OWM4ODliOTA5Yjc0YTQ0OWYwOGNlYyJ9fX0="));
        heads.put("maxPlayerBars", SkullUtils.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2E2ZGNmMjc1Y2Y1OGM2NGNhN2I0ZDFmYzRlYTAwOWEyYjU2OTk1ZjUxYjU0OTg3NGJhNzg5ODZjZGVhYjdkMyJ9fX0="));
        heads.put("arrowUp", SkullUtils.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWFkNmM4MWY4OTlhNzg1ZWNmMjZiZTFkYzQ4ZWFlMmJjZmU3NzdhODYyMzkwZjU3ODVlOTViZDgzYmQxNGQifX19"));
        heads.put("arrowDown", SkullUtils.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODgyZmFmOWE1ODRjNGQ2NzZkNzMwYjIzZjg5NDJiYjk5N2ZhM2RhZDQ2ZDRmNjVlMjg4YzM5ZWI0NzFjZTcifX19"));
        heads.put("barrier", SkullUtils.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWZkMjQwMDAwMmFkOWZiYmJkMDA2Njk0MWViNWIxYTM4NGFiOWIwZTQ4YTE3OGVlOTZlNGQxMjlhNTIwODY1NCJ9fX0="));
    }


}
