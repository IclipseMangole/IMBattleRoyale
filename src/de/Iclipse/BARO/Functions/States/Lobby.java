package de.Iclipse.BARO.Functions.States;

import de.Iclipse.BARO.Functions.PlayerManagement.User;
import de.Iclipse.BARO.Functions.Settings;
import de.Iclipse.IMAPI.Database.UserSettings;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import de.Iclipse.IMAPI.Util.menu.MenuItem;
import de.Iclipse.IMAPI.Util.menu.PopupMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Random;

import static de.Iclipse.BARO.Data.*;
import static de.Iclipse.BARO.Functions.PlayerManagement.User.getUser;
import static de.Iclipse.IMAPI.Functions.PlayerReset.resetPlayer;

public class Lobby implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (state != GameState.Running) {
            Player p = e.getPlayer();
            tablist.setTablist(p);
            tablist.setPlayer(p);
            if (!UserSettings.getBoolean(UUIDFetcher.getUUID(p.getName()), "vanish")) {
                e.setJoinMessage(null);
                Bukkit.getOnlinePlayers().forEach(entry -> {
                    if (!entry.equals(p)) {
                        dsp.send(entry, "lobby.join", p.getDisplayName());
                    }
                });
                dsp.send(Bukkit.getConsoleSender(), "lobby.join", p.getDisplayName());
                setLobbyInventory(p);
                new User(p);
            }
            p.teleport(spawn);
            resetPlayer(p);
            switch (new Random().nextInt(2)) {
                case 0:
                    p.playSound(p.getLocation(), Sound.MUSIC_DISC_WAIT, 0.7f, 1.3f);
                    break;
                case 1:
                    p.playSound(p.getLocation(), Sound.MUSIC_DISC_FAR, 0.7f, 1.3f);
                    break;
            }

        }
    }

    public void setLobbyInventory(Player p) {
        p.getInventory().clear();
        p.setExp(0);
        p.setHealth(20.0);
        p.setFoodLevel(20);
        p.setLevel(0);
        p.getInventory().setItem(8, getSettingsItem(p));
    }

    public ItemStack getSettingsItem(Player p) {
        ItemStack item = heads.get("settings");
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setDisplayName(dsp.get("settings.item", p));
        item.setItemMeta(meta);
        return item;
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (state != GameState.Running) {
            Player p = e.getPlayer();
            e.setQuitMessage(null);
            Bukkit.getOnlinePlayers().forEach(entry -> {
                dsp.send(entry, "lobby.quit", p.getDisplayName());
            });
            dsp.send(Bukkit.getConsoleSender(), "lobby.quit", p.getDisplayName());
            if (getUser(p) != null) {
                User u = getUser(p);
                if (u.getTeam() != null) {
                    u.getTeam().removeUser(u);
                }
                users.remove(u);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (state != GameState.Running) {
            Player p = e.getPlayer();
            if (e.hasItem()) {
                if (e.getItem().getType().toString().contains("CONCRETE")) {
                    openTeamMenu(p);
                } else if (e.getItem().getItemMeta().getDisplayName().equals(getSettingsItem(p).getItemMeta().getDisplayName())) {
                    Settings.openSettingsInventory(p);
                }
            }
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (e.getClickedBlock().getType().equals(Material.STONE_BUTTON)) {
                    Switch button = (Switch) e.getClickedBlock().getBlockData();
                    BlockFace face = button.getFacing();
                    switch (button.getFace()) {
                        case FLOOR:
                            face = BlockFace.UP;
                            break;
                        case CEILING:
                            face = BlockFace.DOWN;
                            break;
                    }
                    Block behind = e.getClickedBlock().getRelative(face.getOppositeFace());
                    if (behind.getType().equals(Material.GOLD_BLOCK)) {
                        int schnitzel = 1000;
                        dsp.send(p, "jumpnrun.finished", "" + schnitzel);
                        de.Iclipse.IMAPI.Database.User.addSchnitzel(UUIDFetcher.getUUID(p.getName()), schnitzel);
                    }
                }
            }
        }
    }


    public static void openTeamMenu(Player p) {
        PopupMenu teamInv = new PopupMenu(dsp.get("team.title", p), 5);
        final int[] i = {9};
        //Teams will ordered as a 7x3 rectangle
        teams.forEach(team -> {
            switch (i[0]) {
                //Upper right corner
                case 16:
                    //To middle right
                    i[0] += 9;
                    break;
                //Middle right
                case 25:
                    //To bottom right
                    i[0] += 9;
                    break;
                //Down left corner
                case 28:
                    //To middle left
                    i[0] -= 9;
                    break;
                default:
                    //Bottom line backward
                    if (i[0] > 28) {
                        i[0]--;
                    } else {
                        //Starts from upper right
                        i[0]++;
                    }
            }
            ItemStack item = team.getTeamItem(p);
            teamInv.addMenuItem(new MenuItem(item) {
                @Override
                public void onClick(Player p) {
                    User u = getUser(p);
                    if (u.getTeam() != null) {
                        if (u.getTeam().equals(team)) {
                            dsp.send(p, "team.already");
                            teamInv.closeMenu(p);
                        } else {
                            if (team.getUsers().size() < teamsize) {
                                team.addUser(u);
                                teamInv.closeMenu(p);
                                teamInv.updateMenu();
                                p.getInventory().setItem(0, u.getTeam().getTeamItem(p));
                            } else {
                                dsp.send(p, "team.full");
                            }
                        }
                    } else {
                        if (team.getUsers().size() < teamsize) {
                            team.addUser(u);
                            teamInv.closeMenu(p);
                            teamInv.updateMenu();
                            p.getInventory().setItem(0, u.getTeam().getTeamItem(p));
                        } else {
                            dsp.send(p, "team.full");
                        }
                    }
                }
            }, i[0]);
        });
        teamInv.fill(Material.LIGHT_GRAY_STAINED_GLASS);
        teamInv.openMenu(p);
    }

    @EventHandler
    public void onBlockDestroy(BlockBreakEvent e) {
        if (state != GameState.Running) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (state != GameState.Running) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (state != GameState.Running) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void FoodLevelChangeEvent(FoodLevelChangeEvent e) {
        if (state != GameState.Running) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e) {
        if (state != GameState.Running) {
            Player p = e.getPlayer();
            if (e.getTo().getBlock().getType().equals(Material.WATER) || e.getTo().getBlock().getType().equals(Material.KELP_PLANT)) {
                if (p.getLocation().distance(spawn) < 70) {
                    p.setVelocity(spawn.toVector().subtract(p.getLocation().toVector()).normalize().setY(1.6));
                } else {
                    p.setVelocity(spawn.toVector().subtract(p.getLocation().toVector()).setY(2));
                }
            }
        }
    }

    @EventHandler
    public void InventoryClick(InventoryClickEvent e) {
        if (state != GameState.Running) {
            if (e.getClickedInventory() != null) {
                if (e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void InventoryClick(InventoryDragEvent e) {
        if (state != GameState.Running) {
            if (e.getInventory().equals(e.getWhoClicked().getInventory())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void ItemDrop(PlayerDropItemEvent e) {
        if (state != GameState.Running) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void CreatureSpawn(CreatureSpawnEvent e) {
        if (state != GameState.Running) {
            if (e.getLocation().distance(spawn) < 60) {
                e.setCancelled(true);
            }
        }
    }

}
