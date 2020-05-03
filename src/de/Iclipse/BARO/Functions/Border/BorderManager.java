package de.Iclipse.BARO.Functions.Border;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import de.Iclipse.BARO.Functions.States.GameState;
import de.Iclipse.IMAPI.Util.Actionbar;
import net.minecraft.server.v1_15_R1.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_15_R1.WorldBorder;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static de.Iclipse.BARO.Data.dsp;
import static de.Iclipse.BARO.Data.spawn;

public class BorderManager implements Listener {
    public static Border border = new Border(new Location(Bukkit.getWorld("world"), 0, 81, 0), 450);
    public static ArrayList<Player> outOfBorder = new ArrayList<>();

    public static void border() {
        if (border.getProgress() <= 1.5) {
            border.setProgress(border.getProgress() + 0.0025);
            if (border.getProgress() == 1) {
                Bukkit.getOnlinePlayers().forEach(entry -> {
                    entry.playSound(entry.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1, 1);
                });
            }
        } else {
            border.setRadiusOld(border.getRadiusNew());
            if (border.getRadiusOld() > 10) {
                border.setRadiusNew((int) (border.getRadiusOld() * 0.6));
            } else {
                border.setRadiusNew(0);
            }
            border.setMiddleOld(border.getMiddleNew());
            border.setMiddleNew(newMiddle());
            border.setProgress(0.0);
            Bukkit.getOnlinePlayers().forEach(entry -> {
                entry.playSound(entry.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 1);
            });
        }
        Data.users.forEach(u -> {
            Player p = u.getPlayer();
            showBorder(p);
            if (p.getLocation().distance(border.getCurrentMiddle()) > border.getCurrentRadius()) {
                if (!outOfBorder.contains(p)) {
                    sendBorderEffect(p);
                    p.setPlayerWeather(WeatherType.DOWNFALL);
                    outOfBorder.add(p);
                    boolean contains = false;
                    for (Map.Entry<ArrayList<User>, Integer> e : Data.reviving.entrySet()) {
                        ArrayList<User> list = e.getKey();
                        if (list.contains(u)) {
                            contains = true;
                        }
                    }
                    if (!contains) {
                        Actionbar.send(p, dsp.get("border.left", p));
                    }
                } else {
                    boolean contains = false;
                    for (Map.Entry<ArrayList<User>, Integer> e : Data.reviving.entrySet()) {
                        ArrayList<User> list = e.getKey();
                        if (list.contains(u)) {
                            contains = true;
                        }
                    }
                    if (!contains) {
                        Actionbar.send(p, dsp.get("border.out", p));
                    }
                }
                if (!Data.spectators.contains(p) && p.getGameMode() != GameMode.CREATIVE && p.getGameMode() != GameMode.SPECTATOR) {
                    if (border.getCurrentRadius() > 200) {
                        p.damage(0.5);
                    } else if (border.getCurrentRadius() > 100) {
                        p.damage(1);
                    } else if (border.getCurrentRadius() > 30) {
                        p.damage(1.75);
                    } else {
                        p.damage(2.5);
                    }
                    p.playEffect(EntityEffect.HURT_DROWN);
                }
                if (!p.hasPotionEffect(PotionEffectType.CONFUSION)) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1, 10));
                }
            } else {
                if (outOfBorder.contains(p)) {
                    boolean contains = false;
                    for (Map.Entry<ArrayList<User>, Integer> e : Data.reviving.entrySet()) {
                        ArrayList<User> list = e.getKey();
                        if (list.contains(p)) {
                            contains = true;
                        }
                    }
                    if (!contains) {
                        Actionbar.send(p, dsp.get("border.join", p));
                    }
                    p.resetPlayerWeather();
                    removeBorderEffect(p);
                    outOfBorder.remove(p);
                }
            }
        });
    }

    private static Location newMiddle() {
        Location loc = new Location(Bukkit.getWorld("world"), border.getMiddleNew().getX() + randomXZ(), border.getMiddleNew().getY() + randomY(), border.getMiddleNew().getX() + randomXZ());
        if (loc.distance(new Location(Bukkit.getWorld("world"), 0, loc.getY(), 0)) + border.getRadiusNew() > 450) {
            System.out.println("> 450");
            return newMiddle();
        }
        return loc;
    }

    private static double randomXZ() {
        Random random = new Random();
        if (border.getRadiusNew() > 0) {
            return random.nextInt(2 * border.getRadiusNew()) - border.getRadiusNew();
        } else {
            return random.nextInt(2 * 3) - 3;
        }
    }

    private static double randomY() {
        Random random = new Random();
        System.out.println(border.getRadiusNew());
        System.out.println(border.getRadiusNew() / 10);
        return random.nextInt(border.getRadiusNew() / 10) - 1 / 5 * border.getRadiusNew();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (Data.state == GameState.Running) {
            Player p = e.getPlayer();
            if (e.getTo().distance(new Location(e.getTo().getWorld(), 0, e.getTo().getY(), 0)) > 450) {
                if (!Data.flyingPlayers.contains(p) || !Data.fallingPlayers.contains(p)) {
                    p.setVelocity(new Location(p.getWorld(), 0, 81, 0).toVector().subtract(p.getLocation().toVector()).normalize());
                } else {
                    p.setVelocity(spawn(p.getWorld()).toVector().subtract(p.getLocation().toVector()).normalize().setY(-0.5));
                }
            }
        }
    }

    public static void showBorder(Player p) {
        if (p.getLocation().distance(border.getCurrentMiddle()) > (border.getCurrentRadius() - 15) && p.getLocation().distance(border.getCurrentMiddle()) < (border.getCurrentRadius() + 15)) {
            for (int x = 0; x < 30; x++) {
                for (int y = 0; y < 30; y++) {
                    for (int z = 0; z < 30; z++) {
                        Location loc = new Location(p.getWorld(), p.getLocation().getX() + (x - 15), p.getLocation().getY() + (y - 15), p.getLocation().getZ() + (z - 15));
                        if (border.getCurrentMiddle().distance(loc) > border.getCurrentRadius() - 0.75 && border.getCurrentMiddle().distance(loc) < border.getCurrentRadius() + 0.75) {
                            p.spawnParticle(Particle.SPELL_WITCH, loc, 2);
                        }
                    }
                }
            }
        }
    }

    public static void sendBorderEffect(Player p) {
        p.playSound(p.getLocation(), Sound.BLOCK_BEACON_AMBIENT, 1, 1);
        WorldBorder w = new WorldBorder();
        w.world = ((CraftWorld) p.getWorld()).getHandle();
        w.setSize(30_000_000);
        w.setWarningDistance(30_000_005);
        w.setCenter(p.getLocation().getX(), p.getLocation().getZ());
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldBorder(w, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE));
    }

    public static void removeBorderEffect(Player p) {
        WorldBorder w = new WorldBorder();
        w.world = ((CraftWorld) p.getWorld()).getHandle();
        w.setSize(30_000_000);
        w.setWarningDistance(1);
        w.setCenter(p.getLocation().getX(), p.getLocation().getZ());
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldBorder(w, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE));
    }


}
