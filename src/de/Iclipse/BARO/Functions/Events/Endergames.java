package de.Iclipse.BARO.Functions.Events;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.PlayerManagement.Team;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Random;

public class Endergames implements Listener {
    public static void endergames() {
        if (Data.estate == EventState.Endergames) {
            if (Data.nextEventTime - Data.timer % 20 == 0) {
                ArrayList<Team> toTeleport = new ArrayList<>();
                Data.teams.forEach(t -> {
                    toTeleport.add(t);
                });
                while (toTeleport.size() > 1) {
                    Team t1 = randomTeam(toTeleport);
                    Team t2 = randomTeam(toTeleport);
                    while (t1.equals(t2)) {
                        t2 = randomTeam(toTeleport);
                    }
                    Location loc = t1.getAlives().get(0).getPlayer().getLocation();
                    Team finalT = t2;
                    t1.getAlives().forEach(entry -> {
                        entry.getPlayer().teleport(finalT.getAlives().get(0).getPlayer().getLocation());
                        entry.getPlayer().playSound(entry.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                    });
                    t2.getAlives().forEach(entry -> {
                        entry.getPlayer().teleport(loc);
                        entry.getPlayer().playSound(entry.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                    });
                    toTeleport.remove(t1);
                    toTeleport.remove(t2);
                }
            }
        }
    }

    private static Team randomTeam(ArrayList<Team> list) {
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }
}
