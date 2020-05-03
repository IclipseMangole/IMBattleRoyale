package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import de.Iclipse.BARO.Functions.States.Finish;
import de.Iclipse.BARO.Functions.States.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

import static de.Iclipse.BARO.Data.dsp;

public class PlayerDeathQuit implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (Data.state == GameState.Running) {
            User u = User.getUser(e.getEntity());
            if (u != null) {
                if (u.getTeam() != null) {
                    e.setDeathMessage(null);
                    Player killer = null;
                    if (u.isKnocked()) {
                        killer = u.getKnockedBy();
                    } else {
                        if (System.currentTimeMillis() < Data.lastDamager.get(u.getPlayer()).getLastDamageTime() + 10000) {
                            killer = Data.lastDamager.get(u.getPlayer()).getLastDamager();
                        }
                    }

                    if (killer == null) {
                        Bukkit.getOnlinePlayers().forEach(entry -> {
                            dsp.send(entry, "game.death", e.getEntity().getDisplayName());
                        });
                        dsp.send(Bukkit.getConsoleSender(), "game.death", e.getEntity().getDisplayName());
                    } else {
                        for (Player entry : Bukkit.getOnlinePlayers()) {
                            dsp.send(entry, "game.kill", e.getEntity().getDisplayName(), killer.getDisplayName());
                        }
                        dsp.send(Bukkit.getConsoleSender(), "game.kill", e.getEntity().getDisplayName(), killer.getDisplayName());
                    }
                    ArrayList<ItemStack> toRemove = new ArrayList<>();
                    e.getDrops().forEach(item -> {
                        if (item.getType().equals(Material.FILLED_MAP)) {
                            toRemove.add(item);
                        }
                    });
                    if (toRemove.size() > 0) {
                        toRemove.forEach(entry -> e.getDrops().remove(entry));
                    }
                    u.setFinished(Data.timer);
                    e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 10, (float) ((new Random().nextInt(7) / 10) + 0.2));
                    if (u.getTeam().getAlive() == 0) {
                        u.getTeam().getUsers().forEach(entry -> {
                            entry.setPlace(Data.teams.size() - 1);
                            if (entry.getPlayer() != null) {
                                entry.getPlayer().sendTitle(dsp.get("finish.team.title", entry.getPlayer()), dsp.get("finish.team.subtitle", entry.getPlayer(), Data.teams.size() + ""), 10, 30, 10);
                            }
                        });
                        Bukkit.getOnlinePlayers().forEach(entry -> {
                            dsp.send(entry, "team.eliminated", Data.teams.get(0).getColor() + "Team " + dsp.get("color." + u.getTeam().getColor().asBungee().getName(), entry));
                        });
                        Data.teams.remove(u.getTeam());
                        if (Data.teams.size() == 1) {
                            Finish.finish();
                        }
                    }
                }
            }

        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        e.setRespawnLocation(new Location(e.getPlayer().getWorld(), 0, 82, 0));
        Spectator.setSpectator(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (Data.state == GameState.Running) {
            User u = User.getUser(e.getPlayer());
            if (u != null) {
                if (u.getTeam() != null) {
                    e.setQuitMessage(null);
                    Player killer = null;
                    if (u.isKnocked()) {
                        killer = u.getKnockedBy();
                    } else {
                        if (System.currentTimeMillis() < Data.lastDamager.get(u.getPlayer()).getLastDamageTime() + 10000) {
                            killer = Data.lastDamager.get(u.getPlayer()).getLastDamager();
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
                    e.getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                    for (int i = 0; i < e.getPlayer().getInventory().getStorageContents().length; i++) {
                        e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), e.getPlayer().getInventory().getStorageContents()[i]);
                    }
                    u.setFinished(Data.timer);
                    e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 10, (float) ((new Random().nextInt(7) / 10) + 0.2));
                    if (u.getTeam().getAlive() == 0) {
                        u.getTeam().getUsers().forEach(entry -> {
                            entry.setPlace(Data.teams.size() - 1);
                            if (entry.getPlayer() != null) {
                                entry.getPlayer().sendTitle(dsp.get("finish.team.title", entry.getPlayer()), dsp.get("finish.team.subtitle", entry.getPlayer(), Data.teams.size() + ""), 10, 30, 10);
                            }
                        });
                        Bukkit.getOnlinePlayers().forEach(entry -> {
                            dsp.send(entry, "team.eliminated", Data.teams.get(0).getColor() + "Team " + dsp.get("color." + u.getTeam().getColor().asBungee().getName(), entry));
                        });
                        Data.teams.remove(u.getTeam());
                        if (Data.teams.size() == 1) {
                            Finish.finish();
                        }
                    }
                }
            }

        }
    }
}
