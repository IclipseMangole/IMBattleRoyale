package de.Iclipse.BARO;


import de.Iclipse.BARO.Functions.Events.EventState;
import de.Iclipse.BARO.Functions.LastDamage;
import de.Iclipse.BARO.Functions.PlayerManagement.Team;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import de.Iclipse.BARO.Functions.Scheduler;
import de.Iclipse.BARO.Functions.States.GameState;
import de.Iclipse.IMAPI.Util.Dispatching.Dispatcher;
import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Data {
    private Data() {
    }

    public static Scheduler scheduler;
    public static Plugin instance;
    public static GameState state;
    public static EventState estate;
    public static EventState nextEvent;
    public static BossBar borderBossBarEN;
    public static BossBar borderBossBarDE;
    public static BossBar eventBossBarEN;
    public static BossBar eventBossBarDE;
    public static int defaultcountdown;
    public static int countdown = defaultcountdown;
    public static int teamsize;
    public static int minplayers = teamsize + 1;
    public static int chests;
    public static Date start;
    public static Dispatcher dsp;
    public static ResourceBundle langDE;
    public static ResourceBundle langEN;
    public static ArrayList<Team> teams = new ArrayList<>();
    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<Player> spectators = new ArrayList<>();
    public static ArrayList<Player> spawningPlayers = new ArrayList<>();
    public static ArrayList<EventState> events = new ArrayList<>();
    public static ArrayList<EventState> allEvents = new ArrayList<>();
    public static HashMap<Player, ItemStack> jumppads = new HashMap<>();
    public static HashMap<Player, LastDamage.LastDamager> lastDamager = new HashMap<>();
    //Elytra
    public static ArrayList<Player> flyingPlayers = new ArrayList<>();
    //Slow Fall
    public static ArrayList<Player> fallingPlayers = new ArrayList<>();
    public static ArrayList<User> watchers = new ArrayList<>();
    public static HashMap<Player, Player> cameras = new HashMap<>();
    public static HashMap<ArrayList<User>, Integer> reviving = new HashMap<>();
    public static HashMap<Player, BossBar> playerBossBars = new HashMap<>();
    public static int timer = 0;
    public static int nextEventTime = -1;
    //Events
    public static ArrayList<User> fishmutation = new ArrayList<>();

    public static Location spawn;
    public static double progressPerSecond;
    public static double newRadius;
    public static File worldFile;
    public static int eventTimeRunning;
    public static int timeBetweenEventsMin;
    public static int timeBetweenEventsMax;
    public static boolean eventsOneTime;


}
