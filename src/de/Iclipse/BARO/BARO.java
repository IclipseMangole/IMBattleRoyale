package de.Iclipse.BARO;

import de.Iclipse.BARO.Commands.*;
import de.Iclipse.BARO.Config.Config;
import de.Iclipse.BARO.Functions.Border.BorderManager;
import de.Iclipse.BARO.Functions.*;
import de.Iclipse.BARO.Functions.Chests.Chests;
import de.Iclipse.BARO.Functions.Chests.Item;
import de.Iclipse.BARO.Functions.Chests.LootDrops;
import de.Iclipse.BARO.Functions.Events.*;
import de.Iclipse.BARO.Functions.HUD.BossBar;
import de.Iclipse.BARO.Functions.HUD.Map;
import de.Iclipse.BARO.Functions.HUD.Scoreboard;
import de.Iclipse.BARO.Functions.PlayerManagement.UserStats;
import de.Iclipse.BARO.Functions.States.GameState;
import de.Iclipse.BARO.Functions.States.Lobby;
import de.Iclipse.IMAPI.Database.Server;
import de.Iclipse.IMAPI.Functions.Servers.State;
import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.Dispatching.Dispatcher;
import de.Iclipse.IMAPI.Util.SkullUtils;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static de.Iclipse.BARO.Data.*;
import static de.Iclipse.BARO.Database.BAROGames.createBAROGamesTable;
import static de.Iclipse.BARO.Database.BAROStats.createBAROStatsTable;
import static de.Iclipse.BARO.Functions.PlayerManagement.TeamManager.createTeams;
import static de.Iclipse.IMAPI.Data.heads;
import static de.Iclipse.IMAPI.IMAPI.getServerName;

public class BARO extends JavaPlugin {
    @Override
    public void onLoad() {
        super.onLoad();
        Data.instance = this;
        config = new Config();
        config.setStandardConfig();
        config.readConfig();
        mapLoader.loadDefaultLobby();
        mapLoader = new MapLoader();
        mapLoader.loadMap();
    }

    @Override
    public void onEnable() {
        config.correctLocations();
        registerListener();
        registerCommands();
        createTables();
        loadResourceBundles();
        Data.state = GameState.Lobby;
        Data.estate = EventState.None;
        Map.loadMap();
        createTeams();
        scheduler = new Scheduler();
        scheduler.startAsyncScheduler();
        scheduler.startScheduler();
        Bukkit.getWorld("world").setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        Bukkit.getWorld("world").setDifficulty(Difficulty.HARD);
        loadCustomHeads();
        Item.loadItems();
        Chests.loadChests();
        Events.registerEvents();
        Server.setMaxPlayers(getServerName(), 16);
        Server.setState(getServerName(), State.Lobby);
    }

    @Override
    public void onDisable() {
        scheduler.stopScheduler();
        scheduler.stopAsyncScheduler();
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
        IMAPI.register(new Nether(), this);
        IMAPI.register(new BurningSun(), this);
        IMAPI.register(new Confusion(), this);
        IMAPI.register(new Endergames(), this);
        IMAPI.register(new Glowing(), this);
        IMAPI.register(new Lostness(), this);
        IMAPI.register(new PoisonWater(), this);
        IMAPI.register(new LavaEvent(), this);
        IMAPI.register(new Chat(), this);
        IMAPI.register(new Levitation(), this);
        IMAPI.register(new FishMutation(), this);
    }

    public void registerCommands() {
        IMAPI.register(new cmd_start(), this);
        IMAPI.register(new cmd_teamsize(), this);
        IMAPI.register(new cmd_pause(), this);
        IMAPI.register(new cmd_maxPlayerBars(), this);
        IMAPI.register(new cmd_barSettingZone(), this);
        IMAPI.register(new cmd_forceEvent(), this);
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
        heads.put("events", SkullUtils.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjE4MTJiNGUwZjAxYmIxOTM3ZGY5Mzg5ZmU2N2UyNWZhNWQ4NzYxMjQ4NTk4MzcwMTZjNDUxNTRiZWQzY2QxZSJ9fX0="));
        heads.put("zone", SkullUtils.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTQ4Zjc3NzEwN2YzMDFjMjQwZjgxNTRmZDk0ZTcyZThiMjJkYTFlZTQ4NzA1YTYxZWZiZmNmOWVlMjA3ZjkyZCJ9fX0="));
    }


}
