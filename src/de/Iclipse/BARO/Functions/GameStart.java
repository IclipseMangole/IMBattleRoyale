package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class GameStart implements Listener {
    public static void startGame() {
        Bukkit.getOnlinePlayers().forEach(entry -> {
            entry.setLevel(0);
            entry.setExp(0);
            entry.setInvulnerable(false);
            entry.getInventory().clear();
            entry.setFoodLevel(20);
            entry.setHealth(20);
            entry.getInventory().setItem(8, getMap(entry));
            entry.setPlayerTime(15000, false);
            entry.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000, 100, false, false));
            entry.setAllowFlight(true);
            Data.state = GameState.Rendering;
            renderMap(entry, 0, 0);
        });
    }

    public static void renderMap(Player p, int x, int z) {
        p.getInventory().setHeldItemSlot(8);
        int[] arrayX = {x};
        int[] arrayZ = {z};
        int loaddistance = 8;
        int tpdistance = 16 * loaddistance;
        int worldsize = 16 * 64;
        Bukkit.getScheduler().runTaskLater(Data.instance, () -> {
            if (arrayX[0] < worldsize / tpdistance) {
                if (arrayZ[0] < worldsize / tpdistance) {
                    p.teleport(new Location(p.getWorld(), arrayX[0] * tpdistance - (0.5 * worldsize - tpdistance + 50), 250, arrayZ[0] * tpdistance - (0.5 * worldsize - tpdistance + 50), 90, -90));
                    p.setGravity(false);
                    arrayZ[0]++;
                } else {
                    arrayZ[0] = 0;
                    arrayX[0]++;
                }
                renderMap(p, arrayX[0], arrayZ[0]);
            } else {
                p.removePotionEffect(PotionEffectType.BLINDNESS);
                p.setGravity(true);
                Data.spawningPlayers.add(p);
                p.resetPlayerTime();
                p.setAllowFlight(false);
                if (Data.state == GameState.Rendering) {
                    Data.state = GameState.Running;
                }
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
        MapRenderer renderer = view.getRenderers().get(0);
        renderer.initialize(view);
        return view;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (Data.state == GameState.Rendering) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSwitch(PlayerSwapHandItemsEvent e) {
        if (Data.state == GameState.Rendering) {
            e.setCancelled(true);
        }
    }
}
