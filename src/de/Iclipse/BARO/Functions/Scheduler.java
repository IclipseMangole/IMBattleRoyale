package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.Chests.LootDrops;
import de.Iclipse.BARO.Functions.Events.*;
import de.Iclipse.BARO.Functions.HUD.BossBar;
import de.Iclipse.BARO.Functions.HUD.Scoreboard;
import de.Iclipse.BARO.Functions.States.Finish;
import de.Iclipse.BARO.Functions.States.GameState;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

import static de.Iclipse.BARO.Data.stats;


public class Scheduler {
    private BukkitTask task;
    private BukkitTask asyncTask;
    private BukkitTask mapTask;
    private int seconds = 0;

    public Scheduler() {
    }

    public void startScheduler() {
        task = Bukkit.getScheduler().runTaskTimer(Data.instance, new Runnable() {
            @Override
            public void run() {

                if (Data.state == GameState.Finished) {
                    Finish.firework();
                } else if (Data.state == GameState.Running) {
                    PlayerSpawns.teleport();
                    LootDrops.lootDropMovement();
                    Data.borderManager.borderEffectsSync();
                    if (Data.estate != EventState.None) {
                        BurningSun.burn();
                        PoisonWater.poison();
                        Endergames.endergames();
                        FishMutation.fish();
                    }
                }

            }
        }, 25, 20);
    }


    public void stopScheduler() {
        task.cancel();
    }

    public void startAsyncScheduler() {
        asyncTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Data.instance, new Runnable() {

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
                    stats.update();
                    Countdown.countdown(seconds);
                    seconds = (seconds + 1) % 59;
                } else if (Data.state == GameState.Running) {
                    Timer.timer();
                    Events.events();
                    Data.borderManager.borderMovement();
                    Scoreboard.scoreboard();
                    BossBar.bossbar();

                    Data.borderManager.borderEffectsAsync();
                    Knocked.reviving();
                    LootDrops.newDrops();
                    Watcher.watcher();
                    Finish.checkFinish();
                } else {
                    stats.update();
                }
            }
        }, 0, 10);
    }

    public void stopAsyncScheduler() {
        asyncTask.cancel();
    }


    public void startMapUpdater() {
        int[] tick = {0};
        mapTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Data.instance, new Runnable() {
            @Override
            public void run() {
                if (Data.state == GameState.Running) {
                    Data.map.map(tick[0]);
                }
                tick[0] = (tick[0] + 1) % 2;
            }
        }, 0, 10);
    }

    public void stopMapUpdater() {
        mapTask.cancel();
    }
}
