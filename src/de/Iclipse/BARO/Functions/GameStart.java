package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class GameStart {
    public void startGame() {
        Data.state = GameState.Running;

        Bukkit.getOnlinePlayers().forEach(entry -> {
            entry.setLevel(0);
            entry.setExp(0);
            entry.setInvulnerable(false);
            entry.getInventory().clear();
            entry.setFoodLevel(20);
            entry.setHealth(20);
            entry.getInventory().setItem(8, getMap(entry));
            renderMap(entry, 0, 0);
        });
    }

    public void renderMap(Player p, int x, int z) {
        p.getInventory().setHeldItemSlot(8);
        int[] arrayX = {x};
        int[] arrayZ = {z};
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000, 100, true, false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1000, 100, true, false));
        int loaddistance = 8;
        int tpdistance = 16 * loaddistance;
        int worldsize = 16 * 64;
        Bukkit.getScheduler().runTaskLater(Data.instance, () -> {
            if (arrayX[0] < worldsize / tpdistance) {
                if (arrayZ[0] < worldsize / tpdistance) {
                    p.teleport(new Location(p.getWorld(), arrayX[0] * tpdistance - (0.5 * worldsize - tpdistance + 50), 250, arrayZ[0] * tpdistance - (0.5 * worldsize - tpdistance + 50), 0, -90));
                    p.setGravity(false);
                    p.damage(0.0);
                    arrayZ[0]++;
                } else {
                    arrayZ[0] = 0;
                    arrayX[0]++;
                }
                renderMap(p, arrayX[0], arrayZ[0]);
            } else {
                p.removePotionEffect(PotionEffectType.BLINDNESS);
                p.removePotionEffect(PotionEffectType.CONFUSION);
                p.setGravity(true);
                Data.spawningPlayers.add(p);
            }
        }, 15);
    }

    public static ItemStack getMap(Player p) {
        ItemStack item = new ItemStack(Material.FILLED_MAP);
        MapMeta meta = (MapMeta) item.getItemMeta();
        meta.setMapView(getMapView(p.getWorld()));
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        return item;
    }

    public static MapView getMapView(World w) {
        MapView view = Bukkit.createMap(w);
        view.setUnlimitedTracking(true);
        view.setTrackingPosition(false);
        view.setCenterX(0);
        view.setCenterZ(0);
        view.setTrackingPosition(true);
        view.setScale(MapView.Scale.FAR);
        view.setLocked(false);
        return view;
    }
}
