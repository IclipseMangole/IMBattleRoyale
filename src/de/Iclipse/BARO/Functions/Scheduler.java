package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class Scheduler {
    private static BukkitTask task;

    public static void startScheduler() {
        task = Bukkit.getScheduler().runTaskTimer(Data.instance, new Runnable() {
            @Override
            public void run() {
                if (Data.state == GameState.Lobby) {
                    //Changes the "Teamitem" for every Player without a team
                    Data.users.forEach(entry -> {
                        if (!entry.isInATeam()) {
                            entry.getPlayer().getInventory().setItem(0, Data.teams.get(new Random().nextInt(Data.teams.size())).getTeamItem(entry.getPlayer()));
                        }
                    });

                    Countdown.countdown();


                } else if (Data.state == GameState.Finished) {

                } else if (Data.state == GameState.Running) {
                    Duration.duration();
                }
            }
        }, 20, 20);
    }

    public static void stopScheduler() {
        task.cancel();
    }
}
