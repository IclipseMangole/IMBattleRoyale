package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import de.Iclipse.BARO.Functions.States.GameState;
import de.Iclipse.IMAPI.Util.SkullUtils;
import net.minecraft.server.v1_15_R1.PacketPlayOutCamera;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;


public class Watcher implements Listener {

    public static void watcher() {
        Data.watchers.forEach(u -> {
            setWatcherInventory(u);
        });
    }

    public static void setWatcher(User u) {
        ArrayList<User> alives = u.getTeam().getAlives();
        u.getPlayer().setGameMode(GameMode.SPECTATOR);
        PacketPlayOutCamera packet = new PacketPlayOutCamera(((CraftEntity) alives.get(0).getPlayer()).getHandle());
        ((CraftPlayer) u.getPlayer()).getHandle().playerConnection.sendPacket(packet);
        Data.cameras.put(u.getPlayer(), alives.get(0).getPlayer());
        Data.watchers.add(u);
        setWatcherInventory(u);
    }

    private static void setWatcherInventory(User u) {
        ArrayList<User> alives = u.getTeam().getAlives();
        for (int i = 0; i < alives.size() && i < 9; i++) {
            if (u.getPlayer().getInventory().getItem(i) != null) {
                if (!u.getPlayer().getInventory().getItem(i).equals(getPlayerHead(u.getPlayer(), alives.get(i).getPlayer()))) {
                    u.getPlayer().getInventory().setItem(i, getPlayerHead(u.getPlayer(), alives.get(i).getPlayer()));
                }
            } else {
                u.getPlayer().getInventory().setItem(i, getPlayerHead(u.getPlayer(), alives.get(i).getPlayer()));
            }
        }
    }

    private static ItemStack getPlayerHead(Player watcher, Player p) {
        ItemStack item = SkullUtils.getPlayerSkull(p);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(p.getDisplayName());
        if (Data.cameras.get(watcher.getPlayer()) == p) {
            meta.addEnchant(Enchantment.LUCK, 1, false);
        }
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (Data.state == GameState.Running) {
            if (User.getUser(e.getPlayer()) != null) {
                User u = User.getUser(e.getPlayer());
                if (Data.watchers.contains(u)) {
                    if (e.hasItem()) {
                        if (u.getTeam() != null) {
                            if (e.getItem().getType().equals(Material.PLAYER_HEAD)) {
                                SkullMeta meta = (SkullMeta) e.getItem().getItemMeta();
                                ArrayList<User> alives = u.getTeam().getAlives();
                                if (alives.size() > 0) {
                                    System.out.println("Alives Size: " + alives.size());
                                    for (User a : alives) {
                                        System.out.println("User: " + a.getPlayer().getName());
                                        if (meta.getOwningPlayer().getName().equals(a.getPlayer().getName())) {
                                            PacketPlayOutCamera packet = new PacketPlayOutCamera(((CraftEntity) alives.get(0).getPlayer()).getHandle());
                                            ((CraftPlayer) u.getPlayer()).getHandle().playerConnection.sendPacket(packet);
                                            Data.cameras.replace(e.getPlayer(), a.getPlayer());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (Data.state == GameState.Running) {
            if (User.getUser(e.getEntity()) != null) {
                User u = User.getUser(e.getEntity());
                if (Data.cameras.containsValue(u)) {
                    Data.cameras.forEach((camera, player) -> {
                        if (User.getUser(camera) != null) {
                            User c = User.getUser(camera);
                            if (Data.watchers.contains(c)) {
                                Spectator.setSpectator(c.getPlayer());
                            }
                        }
                    });
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerQuitEvent e) {
        if (Data.state == GameState.Running) {
            if (User.getUser(e.getPlayer()) != null) {
                User u = User.getUser(e.getPlayer());
                if (Data.cameras.containsValue(u)) {
                    Data.cameras.forEach((camera, player) -> {
                        if (User.getUser(camera) != null) {
                            User c = User.getUser(camera);
                            if (Data.watchers.contains(c)) {
                                Spectator.setSpectator(c.getPlayer());
                            }
                        }
                    });
                }
            }
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        if (User.getUser(e.getPlayer()) != null) {
            User u = User.getUser(e.getPlayer());
            if (Data.watchers.contains(u)) {
                e.setCancelled(true);
            }
        }
    }
}
