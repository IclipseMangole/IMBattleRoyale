package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import de.Iclipse.BARO.Functions.States.GameState;
import de.Iclipse.IMAPI.Util.Actionbar;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Map;

import static de.Iclipse.BARO.Data.dsp;


public class Knocked implements Listener {


    public static void reviving() {
        if (Data.reviving.size() > 0) {
            ArrayList<ArrayList> toRemove = new ArrayList<>();
            Data.reviving.forEach((list, cd) -> {
                if (isWatching(list.get(0).getPlayer(), list.get(1).getPlayer()) && !list.get(0).isKnocked()) {
                    if (cd < 10) {
                        cd++;
                        for (User user : list) {
                            sendActionbarReviving(user.getPlayer(), cd);
                        }
                        Data.reviving.replace(list, cd);
                    } else {
                        Bukkit.getScheduler().runTask(Data.instance, () -> revive(list.get(1)));
                        toRemove.add(list);
                    }
                } else {
                    toRemove.add(list);
                }
            });
            if (toRemove.size() > 0) {
                toRemove.forEach(entry -> {
                    Data.reviving.remove(entry);
                });
            }
        }
    }

    public static void sendActionbarReviving(Player p, int cd) {
        String s = "";
        for (int i = 0; i < 10; i++) {
            if (i < cd) {
                s = s + "§4";
            } else {
                s = s + "§7";
            }
            s = s + "||||||||||";
        }
        Actionbar.send(p, s);
    }

    public static boolean isWatching(Player w, Player p) {
        Location origin = w.getEyeLocation(); // the player's eyes
        Vector direction = w.getLocation().getDirection(); // the player's direction (in which they're looking)
        int range = 3; // the range
        for (double i = 0.0; i < range + 1; i += 0.20) {
            origin.add(direction.getX() * i, direction.getY() * i, direction.getZ() * i);
            if (p.getBoundingBox().contains(origin.toVector())) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (Data.state == GameState.Running) {
            if (e.getEntity() instanceof Player) {
                if (User.getUser((Player) e.getEntity()) != null) {
                    User u = User.getUser((Player) e.getEntity());
                    if (((Player) e.getEntity()).getHealth() <= e.getDamage()) {
                        if (!u.isKnocked()) {
                            if (u.getTeam() != null) {
                                if (u.getTeam().getAlive() > 1) {
                                    boolean allKnocked = true;
                                    for (User entry : u.getTeam().getAlives()) {
                                        if (!entry.isKnocked()) {
                                            allKnocked = false;
                                        }
                                    }
                                    if (!allKnocked) {
                                        e.setCancelled(true);
                                        if (Data.lastDamager.get(e.getEntity()) != null) {
                                            if (System.currentTimeMillis() < Data.lastDamager.get(e.getEntity()).getLastDamageTime() + 10000) {
                                                setKnocked(u, Data.lastDamager.get(u.getPlayer()).getLastDamager());
                                            } else {
                                                setKnocked(u, null);
                                            }
                                        } else {
                                            setKnocked(u, null);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (u.isKnocked()) {
                        if (e.getCause() == EntityDamageEvent.DamageCause.WITHER) {
                            e.setDamage(0.67);
                            Data.reviving.forEach((list, cd) -> {
                                if (list.contains(u)) {
                                    e.setCancelled(true);
                                }
                            });
                        }
                    }
                }
            }
        }
    }


    public static void setKnocked(User u, Player knockedBy) {
        if (knockedBy == null) {
            Bukkit.getOnlinePlayers().forEach(entry -> {
                dsp.send(entry, "knocked.noplayer", u.getPlayer().getDisplayName());
            });
            dsp.send(Bukkit.getConsoleSender(), "knocked.noplayer", u.getPlayer().getDisplayName());
        } else {
            Bukkit.getOnlinePlayers().forEach(entry -> {
                dsp.send(entry, "knocked.player", u.getPlayer().getDisplayName(), knockedBy.getDisplayName());
            });
            dsp.send(Bukkit.getConsoleSender(), "knocked.player", u.getPlayer().getDisplayName(), knockedBy.getDisplayName());
        }
        u.setKnocked(true, knockedBy);
        u.getPlayer().setHealth(20.0);
        u.getPlayer().setFoodLevel(20);
        u.getPlayer().getActivePotionEffects().forEach(effect -> u.getPlayer().removePotionEffect(effect.getType()));
        u.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 99999, 1, true, true, false));
        u.getPlayer().setCanPickupItems(false);
        u.getPlayer().setSneaking(true);
        u.getTeam().getUsers().forEach(p -> setGlowing(u.getPlayer(), p.getPlayer(), true));
        u.getPlayer().setWalkSpeed(0.05f);
        if (Data.playerBossBars.containsKey(u.getPlayer())) {
            org.bukkit.boss.BossBar bar = Data.playerBossBars.get(u.getPlayer());
            if (!bar.getColor().equals(BarColor.RED)) {
                bar.setColor(BarColor.RED);
            }
            bar.setProgress(u.getPlayer().getHealth() / 20);
        }
    }

    public static void revive(User u) {
        u.setKnocked(false, null);
        u.getPlayer().setHealth(5.0);
        u.getPlayer().setFoodLevel(10);
        u.getPlayer().setCanPickupItems(true);
        u.getPlayer().setSneaking(false);
        u.getPlayer().setWalkSpeed(0.2f);
        u.getPlayer().setSneaking(false);
        u.getTeam().getUsers().forEach(p -> setGlowing(u.getPlayer(), p.getPlayer(), false));
        //Bukkit.getOnlinePlayers().forEach(o -> sendStandPacket(o, u.getPlayer()));
        u.getPlayer().getActivePotionEffects().forEach(effect -> u.getPlayer().removePotionEffect(effect.getType()));
        if (Data.playerBossBars.containsKey(u.getPlayer())) {
            org.bukkit.boss.BossBar bar = Data.playerBossBars.get(u.getPlayer());
            if (!bar.getColor().equals(BarColor.GREEN)) {
                bar.setColor(BarColor.GREEN);
            }
            bar.setProgress(u.getPlayer().getHealth() / 20);
        }
    }

    public static void setGlowing(Player glowingPlayer, Player sendPacketPlayer, boolean glow) {
        try {
            EntityPlayer entityPlayer = ((CraftPlayer) glowingPlayer).getHandle();

            DataWatcher toCloneDataWatcher = entityPlayer.getDataWatcher();
            DataWatcher newDataWatcher = new DataWatcher(entityPlayer);

            // The map that stores the DataWatcherItems is private within the DataWatcher Object.
            // We need to use Reflection to access it from Apache Commons and change it.
            Int2ObjectOpenHashMap<DataWatcher.Item<?>> currentMap = (Int2ObjectOpenHashMap<DataWatcher.Item<?>>) FieldUtils.readDeclaredField(toCloneDataWatcher, "entries", true);
            Int2ObjectOpenHashMap<DataWatcher.Item<?>> newMap = new Int2ObjectOpenHashMap<>();

            // We need to clone the DataWatcher.Items because we don't want to point to those values anymore.
            for (Integer integer : currentMap.keySet()) {
                newMap.put(integer, currentMap.get(integer).d()); // Puts a copy of the DataWatcher.Item in newMap
            }

            // Get the 0th index for the BitMask value. http://wiki.vg/Entities#Entity
            DataWatcher.Item item = newMap.get(0);

            byte initialBitMask = (Byte) item.b(); // Gets the initial bitmask/byte value so we don't overwrite anything.
            byte bitMaskIndex = (byte) 6; // The index as specified in wiki.vg/Entities
            if (glow) {
                item.a((byte) (initialBitMask | 1 << bitMaskIndex));
            } else {
                item.a((byte) (initialBitMask & ~(1 << bitMaskIndex))); // Inverts the specified bit from the index.
            }

            //item.a(glow);

            // Set the newDataWatcher's (unlinked) map data
            FieldUtils.writeDeclaredField(newDataWatcher, "entries", newMap, true);

            PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(glowingPlayer.getEntityId(), newDataWatcher, true);

            ((CraftPlayer) sendPacketPlayer).getHandle().playerConnection.sendPacket(metadataPacket);
        } catch (IllegalAccessException e) { // Catch statement necessary for FieldUtils.readDeclaredField()
            e.printStackTrace();
        }
    }

    /*
    public static void sendSwimmPacket(Player receiver, Player p){
        PacketContainer swimPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        swimPacket.getIntegers().write(0, p.getEntityId());
        WrappedDataWatcher watcher = new WrappedDataWatcher(p);
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
        watcher.setObject(0, serializer, (byte) (0x10));
        swimPacket.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        try{
            protocolManager.sendServerPacket(receiver, swimPacket);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void sendStandPacket(Player receiver, Player p){
        PacketContainer standPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        standPacket.getIntegers().write(0, p.getEntityId());
        WrappedDataWatcher watcher = new WrappedDataWatcher(p);
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
        watcher.setObject(0, serializer, (byte) (0));
        standPacket.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        try{
            protocolManager.sendServerPacket(receiver, standPacket);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        DataWatcher watcher = ((CraftPlayer) p).getHandle().getDataWatcher();
        watcher.set(DataWatcherRegistry.a.a(18), (byte) 0x10);
        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(p.getEntityId(), watcher, false);

    }
    */

    @EventHandler
    public void onToggleSwim(EntityPoseChangeEvent e) {
        if (Data.state == GameState.Running) {
            if (e.getEntity() instanceof Player) {
                if (User.getUser((Player) e.getEntity()) != null) {
                    if (User.getUser((Player) e.getEntity()).isKnocked()) {
                        User.getUser((Player) e.getEntity()).getTeam().getUsers().forEach(p -> setGlowing(User.getUser((Player) e.getEntity()).getPlayer(), p.getPlayer(), true));
                    }
                }
            }
        }
    }


    /*
    public static void onPacketReceiving(PacketEvent e) {
        if (Data.state == GameState.Running) {
            if (e.getPacketType() == PacketType.Play.Server.ENTITY_METADATA) {
                System.out.println("PacketReceiving");
                if (User.getUser(e.getPlayer()) != null) {
                    if (User.getUser(e.getPlayer()).isKnocked()) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
     */

    @EventHandler
    public void onToggleSneak(PlayerToggleSneakEvent e) {
        if (Data.state == GameState.Running) {
            if (User.getUser(e.getPlayer()) != null) {
                if (User.getUser(e.getPlayer()).isKnocked()) {
                    User.getUser(e.getPlayer()).getTeam().getUsers().forEach(p -> setGlowing(User.getUser(e.getPlayer()).getPlayer(), p.getPlayer(), true));
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onToggleSprint(PlayerToggleSprintEvent e) {
        if (Data.state == GameState.Running) {
            if (User.getUser(e.getPlayer()) != null) {
                if (User.getUser(e.getPlayer()).isKnocked()) {
                    User.getUser(e.getPlayer()).getTeam().getUsers().forEach(p -> setGlowing(User.getUser((Player) e.getPlayer()).getPlayer(), p.getPlayer(), true));
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onConsume(PlayerInteractEvent e) {
        if (Data.state == GameState.Running) {
            if (User.getUser(e.getPlayer()) != null) {
                if (User.getUser(e.getPlayer()).isKnocked()) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onGlide(EntityToggleGlideEvent e) {
        if (Data.state == GameState.Running) {
            if (e.getEntity() instanceof Player) {
                if (User.getUser((Player) e.getEntity()) != null) {
                    if (User.getUser((Player) e.getEntity()).isKnocked()) {
                        User.getUser((Player) e.getEntity()).getTeam().getUsers().forEach(p -> setGlowing(User.getUser((Player) e.getEntity()).getPlayer(), p.getPlayer(), true));
                        e.setCancelled(true);
                    }
                }
            }
        }
    }


    @EventHandler
    public void onDamageBy(EntityDamageByEntityEvent e) {
        if (Data.state == GameState.Running) {
            if (e.getDamager() instanceof Player) {
                if (User.getUser((Player) e.getDamager()) != null) {
                    if (User.getUser((Player) e.getDamager()).isKnocked()) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onRegain(EntityRegainHealthEvent e) {
        if (Data.state == GameState.Running) {
            if (e.getEntity() instanceof Player) {
                if (User.getUser((Player) e.getEntity()) != null) {
                    if (User.getUser((Player) e.getEntity()).isKnocked()) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (Data.state == GameState.Running) {
            if (User.getUser(e.getPlayer()) != null) {
                if (User.getUser(e.getPlayer()).isKnocked()) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockPlaceEvent e) {
        if (Data.state == GameState.Running) {
            if (User.getUser(e.getPlayer()) != null) {
                if (User.getUser(e.getPlayer()).isKnocked()) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(InventoryClickEvent e) {
        if (Data.state == GameState.Running) {
            if (User.getUser((Player) e.getWhoClicked()) != null) {
                if (User.getUser((Player) e.getWhoClicked()).isKnocked()) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(PlayerDropItemEvent e) {
        if (Data.state == GameState.Running) {
            if (User.getUser(e.getPlayer()) != null) {
                if (User.getUser(e.getPlayer()).isKnocked()) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e) {
        if (Data.state == GameState.Running) {
            if (e.getRightClicked() instanceof Player) {
                if (User.getUser(e.getPlayer()) != null) {
                    User reviver = User.getUser(e.getPlayer());
                    if (User.getUser((Player) e.getRightClicked()) != null) {
                        User knocked = User.getUser((Player) e.getRightClicked());
                        if (knocked.isKnocked()) {
                            if (knocked.getTeam() != null) {
                                if (knocked.getTeam().getUsers().contains(reviver)) {
                                    if (e.getPlayer().getLocation().distance(e.getRightClicked().getLocation()) < 3.5) {
                                        if (!reviver.isKnocked()) {
                                            boolean contains = false;
                                            for (Map.Entry<ArrayList<User>, Integer> map : Data.reviving.entrySet()) {
                                                ArrayList<User> list = map.getKey();
                                                if (list.contains(User.getUser((Player) e.getRightClicked()))) {
                                                    contains = true;
                                                }
                                            }
                                            if (!contains) {
                                                ArrayList<User> list = new ArrayList<>();
                                                list.add(reviver);
                                                list.add(knocked);
                                                sendActionbarReviving(e.getPlayer(), 0);
                                                Data.reviving.put(list, 0);
                                            } else {
                                                e.setCancelled(true);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
