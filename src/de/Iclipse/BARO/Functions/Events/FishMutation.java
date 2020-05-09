package de.Iclipse.BARO.Functions.Events;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityAirChangeEvent;

public class FishMutation implements Listener {

    @EventHandler
    public void onFish(EventChangeEvent event) {
        if (event.getAfter() == EventState.FishMutation) {
            Data.users.forEach(user -> {
                if (user.isAlive()) {
                    Data.fishmutation.add(user);
                }
            });
        } else if (event.getBefore() == EventState.FishMutation) {
            Data.fishmutation.clear();
        }
    }

    @EventHandler
    public void onBreath(EntityAirChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            if (Data.estate == EventState.FishMutation) {
                if (User.getUser((Player) event.getEntity()) != null) {
                    if (User.getUser((Player) event.getEntity()).isAlive()) {
                        System.out.println("EntityAirChangeEvent");
                        Location playerhead = event.getEntity().getLocation();
                        playerhead.setY(playerhead.getY() + 1);
                        if (playerhead.getBlock().getType() == Material.WATER || playerhead.getBlock().getType() == Material.KELP_PLANT) {
                            System.out.println("Water");
                            System.out.println("Remaining after: " + event.getAmount() + " Remaining before: " + ((Player) event.getEntity()).getRemainingAir());
                            if (event.getAmount() <= ((Player) event.getEntity()).getRemainingAir()) {
                                event.setCancelled(true);
                            }
                        } else {
                            System.out.println("Not Water");
                            System.out.println("Remaining after: " + event.getAmount() + " Remaining before: " + ((Player) event.getEntity()).getRemainingAir());
                            if (event.getAmount() >= ((Player) event.getEntity()).getRemainingAir()) {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void fish() {
        if (Data.estate == EventState.FishMutation) {
            for (User user : Data.fishmutation) {
                if (user.isAlive()) {
                    Player player = user.getPlayer();
                    Location playerhead = player.getLocation();
                    playerhead.setY(playerhead.getY() + 1);
                    if (!playerhead.getBlock().getType().equals(Material.WATER)) {
                        if (player.getRemainingAir() > 0) {
                            player.setRemainingAir(player.getRemainingAir() - 1);
                        }
                    } else {
                        if (player.getRemainingAir() < player.getMaximumAir()) {
                            player.setRemainingAir(player.getRemainingAir() + 1);
                        }
                    }
                } else {
                    Data.fishmutation.remove(user);
                }
            }
        }
    }
}
