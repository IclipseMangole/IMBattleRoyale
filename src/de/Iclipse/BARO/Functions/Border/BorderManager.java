package de.Iclipse.BARO.Functions.Border;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import de.Iclipse.BARO.Functions.States.GameState;
import de.Iclipse.IMAPI.Util.Actionbar;
import net.minecraft.server.v1_15_R1.PacketPlayOutWorldBorder;
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

import static de.Iclipse.BARO.Data.*;

public class BorderManager implements Listener {
    public Border border = new Border(Data.middle, Data.firstRadius);
    public ArrayList<Player> outOfBorder;


    public BorderManager() {
        border = new Border(Data.middle, Data.firstRadius);
        outOfBorder = new ArrayList<>();
    }

    public void borderMovement() {
        if (border.getProgress() <= 1.5) {
            border.setProgress(border.getProgress() + Data.progressPerSecond);
            if (border.getProgress() >= 1 && border.getProgress() < 1 + progressPerSecond) {
                Bukkit.getOnlinePlayers().forEach(entry -> {
                    entry.playSound(entry.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1, 1);
                });
            }
        } else {
            border.setRadiusOld(border.getRadiusNew());
            if (border.getRadiusOld() > 10) {
                border.setRadiusNew((int) (border.getRadiusOld() * newRadius));
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
    }

    public void borderEffectsAsync() {
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

    public void borderEffectsSync() {
        Data.users.forEach(u -> {
            Player p = u.getPlayer();
            if (u.isAlive()) {
                if (p.getLocation().distance(border.getCurrentMiddle()) > border.getCurrentRadius()) {
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
                    if (!p.hasPotionEffect(PotionEffectType.CONFUSION)) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 80, 1));
                    }
                }
            }
        });
    }

    private Location newMiddle() {
        Location loc = new Location(Bukkit.getWorld("map"), border.getMiddleNew().getX() + randomXZ(), border.getMiddleNew().getY() + randomY(), border.getMiddleNew().getX() + randomXZ());
        if (loc.distance(new Location(Bukkit.getWorld("map"), 0, loc.getY(), 0)) + border.getRadiusNew() > firstRadius) {
            System.out.println("> 450");
            return newMiddle();
        }
        return loc;
    }

    private double randomXZ() {
        Random random = new Random();
        if (border.getRadiusNew() > 0) {
            return random.nextInt(2 * border.getRadiusNew()) - border.getRadiusNew();
        } else {
            return random.nextInt(2 * 3) - 3;
        }
    }

    private double randomY() {
        Random random = new Random();
        System.out.println(border.getRadiusNew());
        System.out.println(border.getRadiusNew() / 10);
        return random.nextInt(border.getRadiusNew() / 10) - 1 / 5 * border.getRadiusNew();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (Data.state == GameState.Running) {
            Player p = e.getPlayer();
            if (e.getTo().distance(middle) > firstRadius) {
                if (!Data.flyingPlayers.contains(p) || !Data.fallingPlayers.contains(p)) {
                    p.setVelocity(middle.toVector().subtract(p.getLocation().toVector()).normalize());
                } else {
                    p.setVelocity(middle.subtract(p.getLocation()).toVector().normalize().setY(-0.5));
                }
            }
            if (noLobbyIsland) {
                if (e.getTo().distance(mapLobbyMiddle) <= mapLobbyMaxDistance) {
                    p.setVelocity(middle.toVector().subtract(p.getLocation().toVector()).normalize());
                }
            }
        }
    }

    public void showBorder(Player p) {
        if (p.getLocation().distance(border.getCurrentMiddle()) > (border.getCurrentRadius() - 15) && p.getLocation().distance(border.getCurrentMiddle()) < (border.getCurrentRadius() + 15)) {
            for (int x = 0; x < 30; x++) {
                for (int y = 0; y < 30; y++) {
                    for (int z = 0; z < 30; z++) {
                        Location loc = new Location(p.getWorld(), p.getLocation().getX() + (x - 15), p.getLocation().getY() + (y - 15), p.getLocation().getZ() + (z - 15));
                        if (border.getCurrentMiddle().distance(loc) > border.getCurrentRadius() - 0.75 && border.getCurrentMiddle().distance(loc) < border.getCurrentRadius() + 0.75) {
                            if (User.getUser(p) == null) {
                                p.spawnParticle(Particle.SPELL_WITCH, loc, 2);
                            } else {
                                p.spawnParticle(User.getUser(p).getParticle(), loc, 2);
                            }
                        }
                    }
                }
            }
        }
    }

    public void sendBorderEffect(Player p) {
        p.playSound(p.getLocation(), Sound.BLOCK_BEACON_AMBIENT, 1, 1);
        net.minecraft.server.v1_15_R1.WorldBorder w = new net.minecraft.server.v1_15_R1.WorldBorder();
        w.world = ((CraftWorld) p.getWorld()).getHandle();
        w.setSize(30_000_000);
        w.setWarningDistance(30_000_005);
        w.setCenter(p.getLocation().getX(), p.getLocation().getZ());
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldBorder(w, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE));
    }

    public void removeBorderEffect(Player p) {
        net.minecraft.server.v1_15_R1.WorldBorder w = new net.minecraft.server.v1_15_R1.WorldBorder();
        w.world = ((CraftWorld) p.getWorld()).getHandle();
        w.setSize(30_000_000);
        w.setWarningDistance(1);
        w.setCenter(p.getLocation().getX(), p.getLocation().getZ());
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldBorder(w, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE));
    }

    public Border getBorder() {
        return border;
    }

    public ArrayList<Player> getOutOfBorder() {
        return outOfBorder;
    }
}
