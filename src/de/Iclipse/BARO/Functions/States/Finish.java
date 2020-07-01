package de.Iclipse.BARO.Functions.States;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Database.BAROGames;
import de.Iclipse.BARO.Functions.HUD.BossBar;
import de.Iclipse.BARO.Functions.HUD.Scoreboard;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import de.Iclipse.BARO.Functions.Spectator;
import de.Iclipse.IMAPI.Database.Server;
import de.Iclipse.IMAPI.Functions.Servers.State;
import de.Iclipse.IMAPI.Functions.Vanish;
import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.Fireworkgenerator;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import net.minecraft.server.v1_16_R1.Entity;
import net.minecraft.server.v1_16_R1.PacketPlayOutCamera;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.event.Listener;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

import static de.Iclipse.BARO.Data.dsp;
import static de.Iclipse.IMAPI.Functions.PlayerReset.resetPlayer;
import static de.Iclipse.IMAPI.IMAPI.getServerName;

public class Finish implements Listener {

    public static void checkFinish() {
        if (Data.teams.size() <= 1) {
            Bukkit.getScheduler().runTask(Data.instance, () -> finish());
        }
    }

    public static void finish() {
        Server.setState(getServerName(), State.Finished);
        Data.state = GameState.Finished;
        de.Iclipse.IMAPI.Data.restart = 30;
        Bukkit.getOnlinePlayers().forEach(entry -> {
            if (entry.isDead()) {
                entry.spigot().respawn();
            }
            if (Data.cameras.containsKey(entry)) {
                PacketPlayOutCamera packet = new PacketPlayOutCamera((Entity) entry);
                ((CraftPlayer) entry).getHandle().playerConnection.sendPacket(packet);
            }
            if (Data.spectators.contains(entry)) {
                Spectator.removeSpectator(entry);
            }
            dsp.send(entry, "finish.finish", Data.teams.get(0).getColor() + "Team " + dsp.get("color." + Data.teams.get(0).getColor().asBungee().getName(), entry));
            entry.teleport(Data.mapLobbySpawn);
            resetPlayer(entry);
            Data.borderManager.removeBorderEffect(entry);
            if (User.getUser(entry) != null && User.getUser(entry).isAlive()) {
                entry.playSound(entry.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 0.9f);
                User.getUser(entry).setPlace(1);
                de.Iclipse.IMAPI.Database.User.addSchnitzel(UUIDFetcher.getUUID(entry.getName()), 250);
            } else {
                entry.playSound(entry.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 0.2f, 1f);
            }
            BossBar.clearBars(entry);
            Scoreboard.boards.get(entry).destroy();
        });
        Server.setPlayers(IMAPI.getServerName(), Bukkit.getOnlinePlayers().size() - Vanish.getVanishsOnServer().size());
        Bukkit.getWorld("map").setStorm(false);
        Bukkit.getWorld("map").setTime(12800);
        save();
    }

    public static void save() {
        Date finish = Date.from(Instant.now());
        BAROGames.createGame(Data.start, finish, Data.teamsize);
        int id = BAROGames.getId(Data.start, finish);
        Data.users.forEach(entry -> {
            entry.save(id);
        });
    }


    public static void firework() {
        Random random = new Random();

        for (int i = 0; i < (de.Iclipse.IMAPI.Data.restart / 6.0); i++) {
            Location spawn = new Location(Bukkit.getWorld("map"), 0, 0, 0);
            spawn.setX(Data.mapLobbyMiddle.getX() + (random.nextInt(11) - 5));
            spawn.setZ(Data.mapLobbyMiddle.getZ() + (random.nextInt(11) - 5));
            spawn.setY(Data.mapLobbyMiddle.getWorld().getHighestBlockAt(spawn.getBlockX(), spawn.getBlockZ()).getY() + 2);


            Fireworkgenerator fireworkgenerator = new Fireworkgenerator(Data.instance);
            fireworkgenerator.setLocation(spawn);
            fireworkgenerator.setLifeTime(20);
            fireworkgenerator.setPower(2);
            FireworkEffect.Type type;
            switch (random.nextInt(5)) {
                case 0:
                    type = FireworkEffect.Type.BALL;
                    break;
                case 1:
                    type = FireworkEffect.Type.BALL_LARGE;
                    break;
                case 2:
                    type = FireworkEffect.Type.BURST;
                    break;
                case 3:
                    type = FireworkEffect.Type.CREEPER;
                    break;
                default:
                    type = FireworkEffect.Type.STAR;
                    break;
            }
            fireworkgenerator.setEffect(FireworkEffect.builder().withColor(Color.fromBGR(random.nextInt(256), random.nextInt(256), random.nextInt(256))).with(type).withFlicker().withTrail().withColor(Color.fromBGR(random.nextInt(256), random.nextInt(256), random.nextInt(256))).withColor(Color.fromBGR(random.nextInt(256), random.nextInt(256), random.nextInt(256))).build());
            fireworkgenerator.spawn();
        }
    }
}
