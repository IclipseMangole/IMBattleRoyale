package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import net.minecraft.server.v1_15_R1.PacketPlayOutMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.map.CraftMapView;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static de.Iclipse.BARO.Data.dsp;
import static de.Iclipse.BARO.Data.timer;

public class Map implements Listener {

    private static byte[] map;
    private static HashMap<Integer, Byte> changed = new HashMap<>();

    public static void loadMap() {
        map = loadData(new File(Bukkit.getWorld("world").getWorldFolder().getPath() + "/maps/map_0_0_3.txt"));
    }

    public static void map() {
        if (map == null) {
            loadMap();
        }
        if (timer % 5 == 1) {
            showBoarder();
        }

        Data.users.forEach(u -> {
            if (!Data.spawningPlayers.contains(u)) {
                Player p = u.getPlayer();
                if (!p.getInventory().getItemInOffHand().getType().equals(Material.FILLED_MAP)) {
                    p.getInventory().setItemInOffHand(getMap(p));
                }
                sendMapView(p, p.getInventory().getItemInOffHand());
            }
        });
    }

    @EventHandler
    public void swapItem(PlayerSwapHandItemsEvent e) {
        if (Data.state == GameState.Running) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void invClick(InventoryClickEvent e) {
        if (Data.state == GameState.Running) {
            if (e.getSlot() == 40) {
                e.setCancelled(true);
            }
        }
    }

    private static void sendMapView(Player p, ItemStack item) {
        int id = ((MapMeta) item.getItemMeta()).getMapView().getId();

        /*
        List<MapRenderer> removing = new ArrayList<>(((MapMeta) item.getItemMeta()).getMapView().getRenderers());
        removing.forEach(((MapMeta) item.getItemMeta()).getMapView()::removeRenderer);
        */

        /*
        Collection<MapIcon> list = new ArrayList<>();
        list.add(new MapIcon(MapIcon.Type.PLAYER, (byte) p.getLocation().getBlockX(), (byte) p.getLocation().getBlockZ(), (byte) 0, null));
         */

        /*
        MapRender render = new MapRender(p.getLocation(), scale);
        render.run();
        while(!render.isFinished()){
        }
         */

        PacketPlayOutMap packet = new PacketPlayOutMap(id, (byte) 3, true, true, new ArrayList<>(), map, 0, 0, 128, 128);
        ((CraftPlayer) p).getHandle().playerConnection.networkManager.sendPacket(packet);
    }

    private static void showBoarder() {
        resetMap();
        showNextBoarder();
        showCurrentBorder();
    }

    private static void showNextBoarder() {
        Location middle = BorderManager.border.getMiddleNew();
        if (!middle.equals(BorderManager.border.getCurrentMiddle()) && BorderManager.border.getRadiusNew() != BorderManager.border.getCurrentRadius()) {
            byte mapMiddleX = (byte) Math.floor((middle.getX() + 512) / 8);
            byte mapMiddleZ = (byte) Math.floor((middle.getZ() + 512) / 8);
            byte mapRadius = (byte) Math.ceil(BorderManager.border.getRadiusNew() / 8);
            double radius = BorderManager.border.getRadiusNew();
            for (byte z = 0; z <= mapRadius; z++) {
                byte mapZ = (byte) (mapMiddleZ - mapRadius + z);
                for (byte x = 0; x <= mapRadius; x++) {
                    byte mapX = (byte) (mapMiddleX - mapRadius + x);
                    Location loc1 = new Location(Bukkit.getWorld("world"), (mapX * 8) - 512, middle.getY(), (mapZ * 8) - 512);
                    Location loc2 = new Location(Bukkit.getWorld("world"), (mapX * 8 + 7) - 512, middle.getY(), (mapZ * 8 + 7) - 512);
                    if (loc1.distance(middle) + 0.5 >= radius && loc2.distance(middle) - 0.5 <= radius) {
                        setColors(mapX, mapZ, mapMiddleX, mapMiddleZ, (byte) (14 * 4));
                    }
                }
            }
        }
    }

    private static void showCurrentBorder() {
        Location middle = BorderManager.border.getCurrentMiddle();
        byte mapMiddleX = (byte) Math.floor((middle.getX() + 512) / 8);
        byte mapMiddleZ = (byte) Math.floor((middle.getZ() + 512) / 8);
        byte mapRadius = (byte) Math.ceil(BorderManager.border.getCurrentRadius() / 8);
        double radius = BorderManager.border.getCurrentRadius();
        for (byte z = 0; z <= mapRadius; z++) {
            byte mapZ = (byte) (mapMiddleZ - mapRadius + z);
            for (byte x = 0; x < mapRadius; x++) {
                byte mapX = (byte) (mapMiddleX - mapRadius + x);
                Location loc1 = new Location(Bukkit.getWorld("world"), (mapX * 8) - 512, middle.getY(), (mapZ * 8) - 512);
                Location loc2 = new Location(Bukkit.getWorld("world"), (mapX * 8 + 7) - 512, middle.getY(), (mapZ * 8 + 7) - 512);
                if (loc1.distance(middle) + 0.5 >= radius && loc2.distance(middle) - 0.5 <= radius) {
                    setColors(mapX, mapZ, mapMiddleX, mapMiddleZ, (byte) (24 * 4));
                }
            }
        }
    }

    private static void resetMap() {
        changed.forEach((key, value) -> {
            setColor(key % 128, key / 128, value);
        });
    }

    private static void setColors(byte pointX, byte pointZ, byte midX, byte midZ, byte color) {
        //System.out.println("PointX: " + pointX + ", PointZ: " + pointZ);
        int pointX1 = midX + (midX - pointX);
        int pointZ1 = midZ + (midZ - pointZ);

        setColor(pointX, pointZ, color);
        setColor(pointX1, pointZ, color);
        setColor(pointX, pointZ1, color);
        setColor(pointX1, pointZ1, color);


    }

    private static void setColor(int x, int z, byte color) {
        if (color == 24 * 4 || color == 14 * 4) {
            if (!changed.containsKey(x + z * 128)) {
                changed.put(x + z * 128, map[x + z * 128]);
            }
        }

        map[x + z * 128] = color;
    }


    public static byte[] loadData(File f) {
        BufferedReader reader = null;
        byte[] data;
        try {
            reader = new BufferedReader(new FileReader(f));
            String[] line = reader.readLine().split(",");
            data = new byte[line.length];
            for (int i = 0; i < line.length; i++) {
                data[i] = Byte.parseByte(line[i]);
            }
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ItemStack getMap(Player p) {
        ItemStack item = new ItemStack(Material.FILLED_MAP);
        MapMeta meta = (MapMeta) item.getItemMeta();
        meta.setMapId(1);
        meta.setMapView(getMapView(p));
        meta.setDisplayName(dsp.get("map.title", p));
        item.setItemMeta(meta);
        return item;
    }

    public static MapView getMapView(Player p) {
        CraftMapView view = (CraftMapView) Bukkit.createMap(p.getWorld());
        view.setUnlimitedTracking(true);
        view.setTrackingPosition(true);
        view.setCenterX(0);
        view.setCenterZ(0);
        view.setScale(MapView.Scale.FAR);
        view.setLocked(true);
        return view;
    }


}
