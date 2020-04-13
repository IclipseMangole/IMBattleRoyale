package de.Iclipse.BARO.Listener;

import de.Iclipse.BARO.Functions.GameState;
import de.Iclipse.BARO.Functions.User;
import de.Iclipse.IMAPI.Database.UserSettings;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import de.Iclipse.IMAPI.Util.menu.MenuItem;
import de.Iclipse.IMAPI.Util.menu.PopupMenu;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

import static de.Iclipse.BARO.Data.*;
import static de.Iclipse.BARO.Functions.User.getUser;

public class LobbyListener implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (state != GameState.Running) {
            Player p = e.getPlayer();
            tablist.setTablist(p);
            tablist.setPlayer(p);
            if (!UserSettings.getBoolean(UUIDFetcher.getUUID(p.getName()), "vanish")) {
                e.setJoinMessage(null);
                Bukkit.getOnlinePlayers().forEach(entry -> {
                    dsp.send(entry, "join.message", p.getDisplayName());
                });
                setLobbyInventory(p);
                new User(p);
            }
            p.teleport(spawn(p));
            p.setAllowFlight(false);
            p.setFlying(false);
            p.setGravity(true);
            if (p.getActivePotionEffects().size() != 0) {
                p.getActivePotionEffects().forEach(entry -> {
                    p.removePotionEffect(entry.getType());
                });
            }
            p.getWorld().setFullTime(6000);
            p.getWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            p.getWorld().setStorm(false);
            p.resetPlayerTime();
            switch (new Random().nextInt(2)) {
                case 0:
                    p.playSound(p.getLocation(), Sound.MUSIC_DISC_WAIT, 1, 1);
                    break;
                case 1:
                    p.playSound(p.getLocation(), Sound.MUSIC_DISC_FAR, 1, 1);
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
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (state != GameState.Running) {
            Player p = e.getPlayer();
            e.setQuitMessage(null);
            Bukkit.getOnlinePlayers().forEach(entry -> {
                dsp.send(entry, "quit.message", p.getDisplayName());
            });
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
                }
            }
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (e.getClickedBlock().getType().equals(Material.STONE_BUTTON)) {
                    BlockFace bf = e.getBlockFace();
                    Block ab = e.getClickedBlock().getRelative(bf);
                    if (ab.getType().equals(Material.GOLD_BLOCK)) {
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
                    System.out.println(u.getTeam());
                    if (u.getTeam() != null) {
                        System.out.println("Teamitem clicked: " + team.toString() + ", Users Team: " + u.getTeam());
                        if (u.getTeam().equals(team)) {
                            dsp.send(p, "team.already");
                            teamInv.closeMenu(p);
                        } else {
                            if (team.getUsers().size() < teamsize) {
                                team.addUser(u);
                                dsp.send(p, "team.changed");
                                teamInv.closeMenu(p);
                                teamInv.updateMenu();
                                tablist.updatePlayer(p);
                                p.getInventory().setItem(0, u.getTeam().getTeamItem(p));
                            } else {
                                dsp.send(p, "team.full");
                            }
                        }
                    } else {
                        if (team.getUsers().size() < teamsize) {
                            team.addUser(u);
                            dsp.send(p, "team.changed");
                            teamInv.closeMenu(p);
                            teamInv.updateMenu();
                            tablist.updatePlayer(p);
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
        if (e.getTo().getBlock().getType().equals(Material.WATER)) {
            e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
        }
    }

    @EventHandler
    public void InventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void InventoryClick(InventoryDragEvent e) {
        if (e.getInventory().equals(e.getWhoClicked().getInventory())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void ItemDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

}
