package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.States.GameState;
import de.Iclipse.IMAPI.Util.Actionbar;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

import static de.Iclipse.BARO.Data.*;

public class PlayerSpawns implements Listener {

    public static void teleport() {
        //Middle X: 0 Z: 0 Radius: 450
        if (spawningPlayers.size() > 0) {
            if (Data.timer < 80) {
                if (Data.timer % 10 == 0) {
                    Data.spawningPlayers.forEach(entry -> {
                        teleportRandom(entry);
                        dsp.send(entry, "teleport.remove", "" + (80 - timer));
                    });
                } else {
                    Data.spawningPlayers.forEach(entry -> {
                        Actionbar.send(entry, dsp.get("teleport.time", entry, 10 - Data.timer % 10 + ""));
                    });
                }
            } else if (timer == 80) {
                spawningPlayers.forEach(p -> {
                    p.getInventory().setChestplate(getElytra(p));
                    p.setGliding(true);
                    p.setGravity(true);
                    p.setAllowFlight(false);
                    dsp.send(p, "teleport.left");
                    flyingPlayers.add(p);
                });
                spawningPlayers.clear();
            }
        }
    }

    public static void teleportRandom(Player p) {
        //Middle X: 0 Z: 0 Radius: 450
        Random random = new Random();
        int radius = (int) Math.round(Math.sqrt(Math.pow(Data.borderManager.getBorder().getCurrentRadius(), 2) - Math.pow(175, 2)));
        int r = (int) Math.round(radius * Math.pow((double) random.nextInt(100) / 100.0, 2));
        int angle = random.nextInt(360);
        int x = (int) (Math.sin(angle) * r);
        int z = (int) (Math.cos(angle) * r);
        Location loc = new Location(p.getWorld(), x, spawnHeight, z, p.getLocation().getYaw(), p.getLocation().getPitch());
        p.teleport(loc);
        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.2f);
        Actionbar.send(p, dsp.get("teleport.teleport", p));
    }


    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        if (state == GameState.Running) {
            if (timer > 3) {
                if (!e.isSneaking()) {
                    if (Data.spawningPlayers.contains(p)) {
                        flyingPlayers.add(p);
                        Data.spawningPlayers.remove(p);
                        p.getInventory().setChestplate(getElytra(p));
                        p.setGliding(true);
                        p.setGravity(true);
                        p.setAllowFlight(false);
                        dsp.send(p, "teleport.left");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (state == GameState.Running) {
            if (spawningPlayers.contains(p)) {
                e.setTo(new Location(p.getWorld(), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getX(), e.getTo().getYaw(), e.getTo().getPitch()));
            }
            if (flyingPlayers.contains(p)) {
                if (p.getLocation().getBlockY() < fallHeight || (p.getLocation().getBlockY() - p.getWorld().getHighestBlockYAt(p.getLocation())) < aboveGround) {
                    fallingPlayers.add(p);
                    flyingPlayers.remove(p);
                    p.getInventory().setChestplate(new ItemStack(Material.AIR));
                    p.setVelocity(p.getVelocity().setY(p.getVelocity().getY() / 4));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 10000, 5, false, false));
                }
            }
            if (fallingPlayers.contains(p)) {
                if (p.isOnGround()) {
                    fallingPlayers.remove(p);
                    p.removePotionEffect(PotionEffectType.SLOW_FALLING);
                } else {
                    Material m = p.getWorld().getBlockAt(p.getLocation().subtract(0, 1, 0)).getType();
                    if (m.equals(Material.WATER) || m.equals(Material.KELP_PLANT)) {
                        fallingPlayers.remove(p);
                        p.removePotionEffect(PotionEffectType.SLOW_FALLING);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            if (spawningPlayers.contains(p) || flyingPlayers.contains(p) || fallingPlayers.contains(p)) {
                e.setCancelled(true);
            }
        }
    }

    public static ItemStack getElytra(Player p) {
        ItemStack item = new ItemStack(Material.ELYTRA);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(dsp.get("parachute.name", p));
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.BINDING_CURSE, 1, false);
        item.setItemMeta(meta);
        return item;
    }
}
