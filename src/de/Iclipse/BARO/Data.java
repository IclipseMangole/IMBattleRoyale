package de.Iclipse.BARO;


import de.Iclipse.BARO.Functions.HUD.Tablist;
import de.Iclipse.BARO.Functions.LastDamage;
import de.Iclipse.BARO.Functions.PlayerManagement.Team;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import de.Iclipse.BARO.Functions.States.GameState;
import de.Iclipse.IMAPI.Util.Dispatching.Dispatcher;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Data {
    public static Plugin instance;
    public static GameState state;
    public static Tablist tablist;
    public static BossBar borderBossBarEN;
    public static BossBar borderBossBarDE;
    public static int defaultcountdown = 100;
    public static int countdown = defaultcountdown;
    public static int teamsize = 1;
    public static int minplayers = teamsize + 1;
    public static int chests = 40;
    public static Date start;
    public static Dispatcher dsp;
    public static ResourceBundle langDE;
    public static ResourceBundle langEN;
    public static ArrayList<Team> teams = new ArrayList<>();
    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<Player> spectators = new ArrayList<>();
    public static ArrayList<Player> spawningPlayers = new ArrayList<>();
    public static HashMap<String, ItemStack> heads = new HashMap<>();
    public static HashMap<Player, LastDamage.LastDamager> lastDamager = new HashMap<>();
    //Elytra
    public static ArrayList<Player> flyingPlayers = new ArrayList<>();
    //Slow Fall
    public static ArrayList<Player> fallingPlayers = new ArrayList<>();
    public static HashMap<ArrayList<User>, Integer> reviving = new HashMap<>();
    public static HashMap<Player, BossBar> playerBossBars = new HashMap<>();
    public static int timer = 0;

    public static boolean stats = false;

    public static Location spawn(World w) {
        Location loc = new Location(w, 145.5, 69.5, -447.5, -90.0f, 0.0f);
        return loc;
    }


}
