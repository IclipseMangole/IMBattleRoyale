package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class Scheduler {
    private static BukkitTask task;

    public static void startScheduler(){
        task = Bukkit.getScheduler().runTaskTimer(Data.instance, new Runnable() {
            @Override
            public void run() {
                int i = 0;
                i++;
                if(i == 4) i = 0;

                //Jede Sekunde
                if(i == 0){


                    if(Data.status == Status.STARTING){
                        Data.countdown--;
                        if(Data.countdown > 20){
                            if(Data.countdown % 10 == 0) postCountdown();
                        }else if(Data.countdown > 5){
                            if(Data.countdown % 5 == 0) postCountdown();
                        }else if(Data.countdown > 0){
                            postCountdown();
                        }else{
                            Start.startBARO();
                        }

                    }

                }

            }
        }, 5, 5);
    }

    public static void postCountdown(){
        Bukkit.broadcastMessage(Data.prefix + "Das Spiel startet in " + Data.countdown + " Sekunden!");
    }

    public static void stopScheduler(){
        task.cancel();
    }
}
