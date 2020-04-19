package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import org.bukkit.Bukkit;
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

                } else if (Data.state == GameState.Running) {
                    PlayerSpawns.teleport();
                    Timer.timer();
                    BorderManager.border();
                    Scoreboard.scoreboard();
                    Map.map();
                }
                seconds = (seconds + 1) % 59;
            }
        }, 20, 20);
    }

    public static void stopScheduler() {
        task.cancel();
    }
}
