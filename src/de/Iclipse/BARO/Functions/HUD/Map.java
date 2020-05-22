package de.Iclipse.BARO.Functions.HUD;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.Border.BorderManager;
import de.Iclipse.BARO.Functions.Chests.LootDrops;
import de.Iclipse.BARO.Functions.Events.EventState;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import de.Iclipse.BARO.Functions.States.GameState;
import net.minecraft.server.v1_15_R1.MapIcon;
import net.minecraft.server.v1_15_R1.PacketPlayOutMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import java.util.Collection;
import java.util.HashMap;

import static de.Iclipse.BARO.Data.dsp;
import static de.Iclipse.BARO.Data.timer;

public class Map implements Listener {

    private static byte[] map;
    private static byte[] lostMap;
    private static HashMap<Integer, Byte> changed = new HashMap<>();

    public static void loadMap() {
        map = loadData(new File(Bukkit.getWorld("world").getWorldFolder().getPath() + "/maps/map_0_0_3.txt"));
        lostMap = new byte[128 * 128];
        byte c = 0;
        for (int i = 0; i < 128 * 128; i++) {
            lostMap[i] = c;
            c = (byte) (i % 127);
        }
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
        if (isPlayerLost(p)) {
            ((MapMeta) item.getItemMeta()).getMapView().setTrackingPosition(false);
        } else {
            ((MapMeta) item.getItemMeta()).getMapView().setTrackingPosition(true);
        }
        int id = ((MapMeta) item.getItemMeta()).getMapView().getId();
        /*
        List<MapRenderer> removing = new ArrayList<>(((MapMeta) item.getItemMeta()).getMapView().getRenderers());
        removing.forEach(((MapMeta) item.getItemMeta()).getMapView()::removeRenderer);
        */


        Collection<MapIcon> list = new ArrayList<>();
        list.add(new MapIcon(MapIcon.Type.PLAYER, (byte) Math.round(p.getLocation().getBlockX() / 4), (byte) Math.round(p.getLocation().getBlockZ() / 4), yawToDirection(p.getLocation().getYaw()), null));
        LootDrops.drops.forEach((loc, looted) -> {
            if (!looted) {
                list.add(new MapIcon(MapIcon.Type.RED_X, (byte) (loc.getBlockX() / 4), (byte) (loc.getBlockZ() / 4), (byte) 0, null));
            }
        });
        if (User.getUser(p) != null) {
            User u = User.getUser(p);
            if (u.getTeam() != null) {
                if (u.isAlive() && u.getTeam().getAlives().size() > 1 || !u.isAlive()) {
                    for (User alive : u.getTeam().getAlives()) {
                        if (!u.equals(alive)) {
                            list.add(new MapIcon(colorToIconType(u.getTeam().getColor()), (byte) (alive.getPlayer().getLocation().getBlockX() / 4), (byte) (alive.getPlayer().getLocation().getBlockZ() / 4), yawToDirection(alive.getPlayer().getLocation().getYaw()), null));
                        }
                    }
                }
            } else {
                Data.teams.forEach(t -> {
                    t.getAlives().forEach(a -> {
                        list.add(new MapIcon(colorToIconType(t.getColor()), (byte) (a.getPlayer().getLocation().getBlockX() / 4), (byte) (a.getPlayer().getLocation().getBlockZ() / 4), yawToDirection(a.getPlayer().getLocation().getYaw()), null));
                    });
                });
            }
        } else {
            Data.teams.forEach(t -> {
                t.getAlives().forEach(a -> {
                    list.add(new MapIcon(colorToIconType(t.getColor()), (byte) (a.getPlayer().getLocation().getBlockX() / 4), (byte) (a.getPlayer().getLocation().getBlockZ() / 4), yawToDirection(a.getPlayer().getLocation().getYaw()), null));
                });
            });
        }


        /*
        MapRender render = new MapRender(p.getLocation(), scale);
        render.run();
        while(!render.isFinished()){
        }
         */
        PacketPlayOutMap packet;
        if (!isPlayerLost(p)) {
            packet = new PacketPlayOutMap(id, (byte) 3, true, true, list, map, 0, 0, 128, 128);
        } else {
            packet = new PacketPlayOutMap(id, (byte) 3, false, true, new ArrayList<>(), lostMap, 0, 0, 128, 128);
        }
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
            byte mapRadius = (byte) Math.ceil(BorderManager.border.getRadiusNew() / 8 + 1);
            double radius = BorderManager.border.getRadiusNew();
            for (byte z = 0; z <= mapRadius; z++) {
                byte mapZ = (byte) (mapMiddleZ - mapRadius + z);
                for (byte x = 0; x < mapRadius; x++) {
                    byte mapX = (byte) (mapMiddleX - mapRadius + x);
                    Location loc1 = new Location(Bukkit.getWorld("world"), (mapX * 8) - 512, middle.getY(), (mapZ * 8) - 512);
                    Location loc2 = new Location(Bukkit.getWorld("world"), (mapX * 8 + 7) - 512, middle.getY(), (mapZ * 8 + 7) - 512);
                    if (loc1.distance(middle) + 1 >= radius && loc2.distance(middle) - 1 <= radius) {
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

    private static byte yawToDirection(float yaw) {
        byte direction;
        if (yaw >= -8) {
            direction = (byte) ((yaw + 8) / 22.5);
        } else {
            direction = (byte) ((360 + (yaw + 8)) / 22.5);
        }
        return direction;
    }

    public static MapIcon.Type colorToIconType(ChatColor c) {
        if (c.equals(ChatColor.BLACK)) {
            return MapIcon.Type.BANNER_BLACK;
        } else if (c.equals(ChatColor.DARK_GRAY)) {
            return MapIcon.Type.BANNER_GRAY;
        } else if (c.equals(ChatColor.WHITE)) {
            return MapIcon.Type.BANNER_WHITE;
        } else if (c.equals(ChatColor.AQUA)) {
            return MapIcon.Type.MONUMENT;
        } else if (c.equals(ChatColor.DARK_AQUA)) {
            return MapIcon.Type.BANNER_CYAN;
        } else if (c.equals(ChatColor.BLUE)) {
            return MapIcon.Type.BANNER_LIGHT_BLUE;
        } else if (c.equals(ChatColor.DARK_BLUE)) {
            return MapIcon.Type.BANNER_BLUE;
        } else if (c.equals(ChatColor.DARK_PURPLE)) {
            return MapIcon.Type.BANNER_PURPLE;
        } else if (c.equals(ChatColor.LIGHT_PURPLE)) {
            return MapIcon.Type.BANNER_MAGENTA;
        } else if (c.equals(ChatColor.RED)) {
            return MapIcon.Type.TARGET_POINT;
        } else if (c.equals(ChatColor.DARK_RED)) {
            return MapIcon.Type.BANNER_RED;
        } else if (c.equals(ChatColor.GOLD)) {
            return MapIcon.Type.BANNER_ORANGE;
        } else if (c.equals(ChatColor.YELLOW)) {
            return MapIcon.Type.BANNER_YELLOW;
        } else if (c.equals(ChatColor.GREEN)) {
            return MapIcon.Type.BANNER_LIME;
        } else if (c.equals(ChatColor.DARK_GREEN)) {
            return MapIcon.Type.BANNER_GREEN;
        } else {
            return MapIcon.Type.BANNER_LIGHT_GRAY;
        }
    }

    public static boolean isPlayerLost(Player p) {
        if (User.getUser(p) != null) {
            User u = User.getUser(p);
            if (u.isAlive()) {
                if (Data.estate == EventState.Lostness) {
                    return true;
                }
            }
        }
        return false;
    }


}
