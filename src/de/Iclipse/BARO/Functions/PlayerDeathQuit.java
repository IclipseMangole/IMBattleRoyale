package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import de.Iclipse.BARO.Functions.States.Finish;
import de.Iclipse.BARO.Functions.States.GameState;
import de.Iclipse.IMAPI.Database.Server;
import de.Iclipse.IMAPI.IMAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

import static de.Iclipse.BARO.Data.dsp;

public class PlayerDeathQuit implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageEvent e) {
        if (!e.isCancelled()) {
            if (Data.state == GameState.Running) {
                if (e.getEntity() instanceof Player) {
                    if (User.getUser((Player) e.getEntity()) != null) {
                        if (((Player) e.getEntity()).getHealth() <= e.getDamage()) {
                            ((Player) e.getEntity()).spigot().respawn();
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (Data.state == GameState.Running) {
            User u = User.getUser(e.getEntity());
            if (u != null) {
                if (u.getTeam() != null) {
                    Server.setPlayers(IMAPI.getServerName(), Server.getPlayers(IMAPI.getServerName()) - 1);
                    e.setDeathMessage(null);
                    Player killer = null;
                    if (u.isKnocked()) {
                        killer = u.getKnockedBy();
                    } else {
                        if (Data.lastDamager.containsKey(u.getPlayer())) {
                            if (System.currentTimeMillis() < Data.lastDamager.get(u.getPlayer()).getLastDamageTime() + 10000) {
                                killer = Data.lastDamager.get(u.getPlayer()).getLastDamager();
                            }
                        }
                    }

                    if (killer == null) {
                        Bukkit.getOnlinePlayers().forEach(entry -> {
                            dsp.send(entry, "game.death", u.getTeam().getColor() + e.getEntity().getDisplayName());
                        });
                        dsp.send(Bukkit.getConsoleSender(), "game.death", e.getEntity().getDisplayName());
                    } else {
                        for (Player entry : Bukkit.getOnlinePlayers()) {
                            dsp.send(entry, "game.kill", e.getEntity().getDisplayName(), killer.getDisplayName());
                        }
                        dsp.send(Bukkit.getConsoleSender(), "game.kill", u.getTeam().getColor() + e.getEntity().getDisplayName(), User.getUser(killer).getTeam().getColor() + killer.getDisplayName());
                    }


                    Block chest = e.getEntity().getLocation().getBlock();
                    Block chest2 = e.getEntity().getLocation().clone().add(1, 0, 0).getBlock();

                    chest.setType(Material.CHEST);
                    chest2.setType(Material.CHEST);


                    Chest chestBlockState1 = (Chest) chest.getBlockData();
                    chestBlockState1.setType(Chest.Type.LEFT);
                    chest.setBlockData(chestBlockState1, true);

                    Chest chestBlockState2 = (Chest) chest2.getBlockData();
                    chestBlockState2.setType(Chest.Type.RIGHT);

                    Inventory chestInv = ((org.bukkit.block.Chest) chest.getState()).getInventory();
                    e.getEntity().getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                    for (ItemStack content : e.getEntity().getInventory().getContents()) {
                        if (content != null) {
                            chestInv.addItem(content);
                        }
                    }
                    e.getDrops().clear();


                    u.setFinished(Data.timer);
                    e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 10, (float) ((new Random().nextInt(7) / 10) + 0.2));
                    if (u.getTeam().getAlive() == 0) {
                        u.getTeam().getUsers().forEach(entry -> {
                            entry.setPlace(Data.teams.size());
                            if (entry.getPlayer() != null) {
                                entry.getPlayer().sendTitle(dsp.get("finish.team.title", entry.getPlayer()), dsp.get("finish.team.subtitle", entry.getPlayer(), Data.teams.size() + ""), 10, 30, 10);
                            }
                        });
                        Bukkit.getOnlinePlayers().forEach(entry -> {
                            dsp.send(entry, "team.eliminated", u.getTeam().getColor() + "Team " + dsp.get("color." + u.getTeam().getColor().asBungee().getName(), entry));
                        });
                        Data.teams.remove(u.getTeam());
                        if (Data.teams.size() == 1) {
                            Bukkit.getScheduler().runTaskLater(Data.instance, () -> Finish.finish(), 5);
                        }
                    }
                }
            }

        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        e.setRespawnLocation(new Location(e.getPlayer().getWorld(), 0, 100, 0));
        Spectator.setSpectator(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (Data.state == GameState.Running) {
            User u = User.getUser(e.getPlayer());
            if (u != null) {
                if (u.getTeam() != null) {
                    Server.setPlayers(IMAPI.getServerName(), Server.getPlayers(IMAPI.getServerName()) - 1);
                    e.setQuitMessage(null);
                    Player killer = null;
                    if (u.isKnocked()) {
                        killer = u.getKnockedBy();
                    } else {
                        if (Data.lastDamager.containsKey(u.getPlayer())) {
                            if (System.currentTimeMillis() < Data.lastDamager.get(u.getPlayer()).getLastDamageTime() + 10000) {
                                killer = Data.lastDamager.get(u.getPlayer()).getLastDamager();
                            }
                        }
                    }

                    if (killer == null) {
                        Bukkit.getOnlinePlayers().forEach(entry -> {
                            dsp.send(entry, "game.death", e.getPlayer().getDisplayName());
                        });
                        dsp.send(Bukkit.getConsoleSender(), "game.death", e.getPlayer().getDisplayName());
                    } else {
                        for (Player entry : Bukkit.getOnlinePlayers()) {
                            dsp.send(entry, "game.kill", e.getPlayer().getDisplayName(), killer.getDisplayName());
                        }
                        dsp.send(Bukkit.getConsoleSender(), "game.kill", e.getPlayer().getDisplayName(), killer.getDisplayName());
                    }
                    Block chest = e.getPlayer().getLocation().getBlock();
                    Block chest2 = e.getPlayer().getLocation().clone().add(1, 0, 0).getBlock();

                    chest.setType(Material.CHEST);
                    chest2.setType(Material.CHEST);


                    Chest chestBlockState1 = (Chest) chest.getBlockData();
                    chestBlockState1.setType(Chest.Type.LEFT);
                    chest.setBlockData(chestBlockState1, true);

                    Chest chestBlockState2 = (Chest) chest2.getBlockData();
                    chestBlockState2.setType(Chest.Type.RIGHT);

                    org.bukkit.block.Chest c = (org.bukkit.block.Chest) e.getPlayer().getLocation().getBlock().getState();
                    Inventory chestInv = c.getInventory();
                    e.getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                    for (ItemStack content : e.getPlayer().getInventory().getContents()) {
                        if (content != null) {
                            chestInv.addItem(content);
                        }
                    }

                    u.setFinished(Data.timer);
                    e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 10, (float) ((new Random().nextInt(7) / 10) + 0.2));
                    if (u.getTeam().getAlive() == 0) {
                        u.getTeam().getUsers().forEach(entry -> {
                            entry.setPlace(Data.teams.size());
                            if (entry.getPlayer() != null) {
                                entry.getPlayer().sendTitle(dsp.get("finish.team.title", entry.getPlayer()), dsp.get("finish.team.subtitle", entry.getPlayer(), Data.teams.size() + ""), 10, 30, 10);
                            }
                        });
                        Bukkit.getOnlinePlayers().forEach(entry -> {
                            dsp.send(entry, "team.eliminated", Data.teams.get(0).getColor() + "Team " + dsp.get("color." + u.getTeam().getColor().asBungee().getName(), entry));
                        });
                        Data.teams.remove(u.getTeam());
                        if (Data.teams.size() == 1) {
                            Bukkit.getScheduler().runTaskLater(Data.instance, () -> Finish.finish(), 5);
                        }
                    }
                }
            }

        }
    }
}
