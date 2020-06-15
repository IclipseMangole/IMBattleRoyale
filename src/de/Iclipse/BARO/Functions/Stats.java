package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Database.BAROStats;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import static de.Iclipse.BARO.Data.dsp;

public class Stats {

    private EntityArmorStand[] armorStands;
    private int count;
    private int stands;


    public Stats(Location location) {
        armorStands = new EntityArmorStand[11];
        count = 0;
        stands = 11;

        Location change = location.clone();
        change.add(0, 1, 0);
        for (int i = 0; i < stands; i++) {
            armorStands[(stands - 1) - i] = createArmorStand(change);
            change.add(0, 0.25, 0);
        }
    }

    public void showArmorStands(Player player) {
        for (int i = 0; i < stands; i++) {
            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(armorStands[i]);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }

        boolean all = count < 20;
        boolean page1 = count % 20 < 10;

        showStats(player, all, page1);

    }

    public void update() {
        if (count % 10 == 0) {
            boolean all = count < 20;
            boolean page1 = count % 20 < 10;

            Bukkit.getOnlinePlayers().forEach(player -> {
                showStats(player, all, page1);

            });
        }

        Bukkit.getOnlinePlayers().forEach(player -> {
            armorStands[10].setCustomName(new ChatComponentText(getStringCounter()));
            PacketPlayOutEntityMetadata packetPlayOutEntityMetadata = new PacketPlayOutEntityMetadata(armorStands[10].getId(), armorStands[10].getDataWatcher(), true);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutEntityMetadata);
        });
        count = (count + 1) % 40;
    }


    public void showStats(Player player, boolean all, boolean page1) {

        armorStands[0].setCustomName(new ChatComponentText(dsp.get("stats.stats", player)));
        if (all) {
            armorStands[1].setCustomName(new ChatComponentText(dsp.get("stats.all", player, BAROStats.getAveragePlace(UUIDFetcher.getUUID(player.getName()), all))));
        } else {
            armorStands[1].setCustomName(new ChatComponentText(dsp.get("stats.month", player, BAROStats.getAveragePlace(UUIDFetcher.getUUID(player.getName()), all))));
        }

        if (page1) {
            armorStands[2].setCustomName(new ChatComponentText(dsp.get("stats.position", player, BAROStats.getPosition(UUIDFetcher.getUUID(player.getName()), all) + "")));
            armorStands[3].setCustomName(new ChatComponentText(dsp.get("stats.kills", player, BAROStats.getKills(UUIDFetcher.getUUID(player.getName()), all) + "")));
            armorStands[4].setCustomName(new ChatComponentText(dsp.get("stats.deaths", player, BAROStats.getDeaths(UUIDFetcher.getUUID(player.getName()), all) + "")));
            armorStands[5].setCustomName(new ChatComponentText(dsp.get("stats.killsdeaths", player, BAROStats.getKD(UUIDFetcher.getUUID(player.getName()), all) + "")));
            armorStands[6].setCustomName(new ChatComponentText(dsp.get("stats.games", player, BAROStats.getPlayedGames(UUIDFetcher.getUUID(player.getName()), all) + "")));
            armorStands[7].setCustomName(new ChatComponentText(dsp.get("stats.gameswon", player, BAROStats.getWonGames(UUIDFetcher.getUUID(player.getName()), all) + "")));
            armorStands[8].setCustomName(new ChatComponentText(dsp.get("stats.winprobability", player, BAROStats.getWinProbability(UUIDFetcher.getUUID(player.getName()), all) + "")));
            armorStands[9].setCustomName(new ChatComponentText(dsp.get("stats.averagePlace", player, BAROStats.getAveragePlace(UUIDFetcher.getUUID(player.getName()), all))));
        } else {
            armorStands[2].setCustomName(new ChatComponentText(dsp.get("stats.damgeDealt", player, BAROStats.getDamageDealt(UUIDFetcher.getUUID(player.getName()), all) + "")));
            armorStands[3].setCustomName(new ChatComponentText(dsp.get("stats.damageReceived", player, BAROStats.getDamageReceived(UUIDFetcher.getUUID(player.getName()), all) + "")));
            armorStands[4].setCustomName(new ChatComponentText(dsp.get("stats.blocksPlaced", player, BAROStats.getBlocksPlaced(UUIDFetcher.getUUID(player.getName()), all) + "")));
            armorStands[5].setCustomName(new ChatComponentText(dsp.get("stats.blocksDestroyed", player, BAROStats.getBlocksDestroyed(UUIDFetcher.getUUID(player.getName()), all) + "")));
            armorStands[6].setCustomName(new ChatComponentText(dsp.get("stats.itemsCrafted", player, BAROStats.getItemsCrafted(UUIDFetcher.getUUID(player.getName()), all) + "")));
            armorStands[7].setCustomName(new ChatComponentText(dsp.get("stats.lootedChests", player, BAROStats.getLootedChests(UUIDFetcher.getUUID(player.getName()), all) + "")));
            armorStands[8].setCustomName(new ChatComponentText(dsp.get("stats.lootedDrops", player, BAROStats.getLootedDrops(UUIDFetcher.getUUID(player.getName()), all) + "")));
            armorStands[9].setCustomName(new ChatComponentText(dsp.get("stats.averagelifespan", player, BAROStats.getAverageLifespan(UUIDFetcher.getUUID(player.getName()), all) + "")));
        }

        for (int i = 0; i < armorStands.length - 1; i++) {
            PacketPlayOutEntityMetadata packetPlayOutEntityMetadata = new PacketPlayOutEntityMetadata(armorStands[i].getId(), armorStands[i].getDataWatcher(), true);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutEntityMetadata);
        }

    }

    private EntityArmorStand createArmorStand(Location location) {
        WorldServer s = ((CraftWorld) location.getWorld()).getHandle();
        EntityArmorStand stand = new EntityArmorStand(s, location.getX(), location.getY(), location.getZ());

        stand.setCustomNameVisible(true);
        //Gravity false
        stand.noclip = true;
        stand.setNoGravity(true);

        stand.setInvisible(true);

        return stand;
    }

    private String getStringCounter() {
        int pageCount = count % 20;
        String stringCounter = dsp.get("stats.counter", Bukkit.getConsoleSender());
        ;
        if (pageCount < 10) {
            stringCounter = stringCounter.substring(0, pageCount) + "§7" + stringCounter.substring(pageCount);
            stringCounter = "§e" + stringCounter;
        } else {
            stringCounter = stringCounter.substring(0, pageCount - 10) + "§e" + stringCounter.substring(pageCount - 10);
            stringCounter = "§7" + stringCounter;
        }
        return stringCounter;
    }


}
