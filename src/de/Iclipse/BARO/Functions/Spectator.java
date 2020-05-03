package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import de.Iclipse.BARO.Functions.States.GameState;
import de.Iclipse.IMAPI.Util.SkullUtils;
import de.Iclipse.IMAPI.Util.menu.MenuItem;
import de.Iclipse.IMAPI.Util.menu.PopupMenu;
import net.minecraft.server.v1_15_R1.PacketPlayOutCamera;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static de.Iclipse.BARO.Data.dsp;

public class Spectator implements Listener {


    public static void setSpectator(Player p) {
        p.teleport(new Location(p.getWorld(), 0, 82, 0));
        if (User.getUser(p) != null) {
            User u = User.getUser(p);
            if (u.hasLivingMates()) {
                Watcher.setWatcher(u);
                return;
            }
        }
        setRealSpectator(p);
    }


    public static void setRealSpectator(Player p) {
        Data.spectators.add(p);
        p.setCollidable(false);
        p.setAllowFlight(true);
        p.setCanPickupItems(false);
        p.teleport(new Location(p.getWorld(), 0, 82, 0));
        p.setExp(0);
        p.setHealth(20.0);
        p.setFoodLevel(20);
        p.setLevel(0);
        p.getInventory().clear();
        p.getInventory().setItem(0, getCompass(p));
        p.setWalkSpeed(0.2f);
        p.setGlowing(false);
        p.setSneaking(false);
        p.getActivePotionEffects().forEach(effect -> p.removePotionEffect(effect.getType()));
        Data.users.forEach(entry -> {
            if (entry.isAlive()) {
                entry.getPlayer().hidePlayer(Data.instance, p);
            }
        });
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 1, true, false));
    }

    public static void removeSpectator(Player p) {
        Data.spectators.remove(p);
        p.setCollidable(true);
        p.setAllowFlight(false);
        p.setCanPickupItems(true);
        p.getInventory().clear();
        Bukkit.getOnlinePlayers().forEach(entry -> {
            if (!entry.canSee(p)) {
                entry.showPlayer(Data.instance, p);
            }
        });
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 1, true, false));
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e) {
        if (Data.state == GameState.Running) {
            if (e.getRightClicked() instanceof Player) {
                if (Data.spectators.contains(e.getPlayer())) {
                    PacketPlayOutCamera packet = new PacketPlayOutCamera(((CraftEntity) e.getRightClicked()).getHandle());
                    ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(packet);
                    Data.cameras.put(e.getPlayer(), (Player) e.getRightClicked());
                }
            }
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        if (Data.cameras.containsKey(e.getPlayer())) {
            if (!Data.watchers.contains(e.getPlayer())) {
                PacketPlayOutCamera packet = new PacketPlayOutCamera(((CraftEntity) e.getPlayer()).getHandle());
                ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(packet);
                Data.cameras.remove(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (Data.state == GameState.Running) {
            if (Data.spectators.contains(e.getPlayer())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (Data.state == GameState.Running) {
            if (Data.spectators.contains(e.getPlayer())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (Data.state == GameState.Running) {
            if (e.getEntity() instanceof Player) {
                if (Data.spectators.contains(e.getEntity())) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (Data.state == GameState.Running) {
            if (e.getDamager() instanceof Player) {
                if (Data.spectators.contains(e.getDamager())) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (Data.state == GameState.Running) {
            if (Data.spectators.contains(e.getWhoClicked())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBreak(PlayerDropItemEvent e) {
        if (Data.state == GameState.Running) {
            if (Data.spectators.contains(e.getPlayer())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (Data.state == GameState.Running) {
            e.setJoinMessage(null);
            dsp.send(e.getPlayer(), "game.running");
            setSpectator(e.getPlayer());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (Data.state == GameState.Running) {
            if (Data.spectators.contains(e.getPlayer())) {
                if (e.getItem().equals(getCompass(e.getPlayer()))) {
                    openCompassInventory(e.getPlayer());
                }
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onCollect(PlayerPickupItemEvent e) {
        if (Data.state == GameState.Running) {
            if (Data.spectators.contains(e.getPlayer())) {
                e.setCancelled(true);
            }
        }
    }

    public static ItemStack getCompass(Player p) {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(dsp.get("compass.name", p));
        item.setItemMeta(meta);
        return item;
    }

    public void openCompassInventory(Player p) {
        PopupMenu teamInv = new PopupMenu(dsp.get("compass.name", p), 2);
        final int[] slot = {0};
        Data.users.forEach(entry -> {
            if (entry.isAlive()) {
                teamInv.addMenuItem(new MenuItem(getPlayerHead(entry.getPlayer())) {
                    @Override
                    public void onClick(Player player) {
                        player.teleport(entry.getPlayer());
                    }
                }, slot[0]);
                slot[0]++;
            }
        });
        teamInv.openMenu(p);
    }

    public static ItemStack getPlayerHead(Player p) {
        ItemStack item = SkullUtils.getPlayerSkull(p);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(p.getDisplayName());
        item.setItemMeta(meta);
        return item;
    }



    /*
    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e){
        if(Data.state == GameState.Running) {
            Player p = e.getPlayer();
            if (User.getUser(p) != null) {
                User u = User.getUser(p);
                if(u.getFinished() != 0) {
                    if (u.getTeam() != null) {
                        Packet packet = new PacketPlayOutCamera((Entity) u.getTeam().getUsers().get(new Random().nextInt(u.getTeam().getAlive())).getPlayer());
                        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
                    }
                }
            }
        }
    }
     */
}
