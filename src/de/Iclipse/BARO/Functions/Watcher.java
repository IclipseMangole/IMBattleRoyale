package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import de.Iclipse.BARO.Functions.PlayerManagement.User;
import de.Iclipse.IMAPI.Util.SkullUtils;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.PacketPlayOutCamera;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;


public class Watcher {

    public static void watcher() {
        Data.watchers.forEach(u -> {
            setWatcherInventory(u);
        });
    }

    public static void setWatcher(User u) {
        ArrayList<User> alives = u.getTeam().getAlives();
        PacketPlayOutCamera packet = new PacketPlayOutCamera((Entity) alives.get(0).getPlayer());
        ((CraftPlayer) u.getPlayer()).getHandle().playerConnection.sendPacket(packet);
        Data.cameras.put(u.getPlayer(), alives.get(0).getPlayer());
        Data.watchers.add(u);
        setWatcherInventory(u);
    }

    private static void setWatcherInventory(User u) {
        ArrayList<User> alives = u.getTeam().getAlives();
        for (int i = 0; i < alives.size() || i < 9; i++) {
            u.getPlayer().getInventory().setItem(i, getPlayerHead(u.getPlayer(), alives.get(i).getPlayer()));
        }
    }

    private static ItemStack getPlayerHead(Player watcher, Player p) {
        ItemStack item = SkullUtils.getPlayerSkull(p);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(p.getDisplayName());
        if (Data.cameras.get(watcher.getPlayer()) == p) {
            meta.addEnchant(Enchantment.LUCK, 1, false);
        }
        item.setItemMeta(meta);
        return item;
    }
}
