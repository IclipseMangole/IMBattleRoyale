package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.Border.BorderManager;
import de.Iclipse.BARO.Functions.Chests.LootDrops;
import de.Iclipse.BARO.Functions.Events.*;
import de.Iclipse.BARO.Functions.HUD.BossBar;
import de.Iclipse.BARO.Functions.HUD.Map;
import de.Iclipse.BARO.Functions.HUD.Scoreboard;
import de.Iclipse.BARO.Functions.States.Finish;
import de.Iclipse.BARO.Functions.States.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class Scheduler {
    private static BukkitTask task;
    private static int seconds = 0;

    public static void startScheduler() {
        task = Bukkit.getScheduler().runTaskTimer(Data.instance, new Runnable() {
            @Override
            public void run() {
                if (Data.state == GameState.Lobby) {
                    //Changes the "Teamitem" for every Player without a team
                    if (Data.users.size() != 0) {
                        Data.users.forEach(entry -> {
                            if (!entry.isInATeam()) {
                                entry.getPlayer().getInventory().setItem(0, Data.teams.get(new Random().nextInt(Data.teams.size())).getTeamItem(entry.getPlayer()));
                            }
                        });
                    }

                    Countdown.countdown(seconds);


                } else if (Data.state == GameState.Finished) {
                    Finish.firework();
                } else if (Data.state == GameState.Running) {
                    PlayerSpawns.teleport();
                    Timer.timer();
                    Events.events();
                    BorderManager.border();
                    Scoreboard.scoreboard();
                    BossBar.bossbar();
                    Map.map();
                    Knocked.reviving();
                    LootDrops.lootDrop();
                    Watcher.watcher();
                    Finish.checkFinish();
                    BurningSun.burn();
                    PoisonWater.poison();
                    Endergames.endergames();
                    Levitation.onLevitate();
                }
                seconds = (seconds + 1) % 59;
            }
        }, 20, 20);
    }

    public static void onFish(){
        Bukkit.getScheduler().runTaskLater(Data.instance, new Runnable() {
            @Override
            public void run() {
                if (Data.estate == EventState.FishMutation) {
                    Data.fishmutation.forEach(user -> {
                        if (user.isAlive()) {
                            Player player = user.getPlayer();
                            Location playerhead = player.getLocation();
                            playerhead.setY(playerhead.getY()+1);
                            if(!playerhead.getBlock().getType().equals(Material.WATER)){
                                if(player.getRemainingAir()>0){
                                    player.setRemainingAir(player.getRemainingAir()-1);
                                }
                            }else{
                                if(player.getRemainingAir()<player.getMaximumAir()){
                                    player.setRemainingAir(player.getRemainingAir()+1);
                                }
                            }
                        } else {
                            Data.fishmutation.remove(user);
                        }
                    });
                    onFish();
                }
            }
        },20);
    }

    public static void stopScheduler() {
        task.cancel();
    }
}
