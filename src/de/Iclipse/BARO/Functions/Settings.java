package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import de.Iclipse.IMAPI.Database.UserSettings;
import de.Iclipse.IMAPI.Util.UUIDFetcher;
import de.Iclipse.IMAPI.Util.menu.MenuItem;
import de.Iclipse.IMAPI.Util.menu.PopupMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import static de.Iclipse.BARO.Data.dsp;
import static de.Iclipse.BARO.Data.heads;

public class Settings {

    public static void openSettingsInventory(Player p) {
        PopupMenu settingsInv = new PopupMenu(dsp.get("settings.inventory", p), 3);
        settingsInv.addMenuItem(new MenuItem(getMaxPlayerBarsItem(p)) {
            @Override
            public void onClick(Player player) {
                openMaxPlayerBarsInventory(p);
            }
        }, 13);
        settingsInv.fill(Material.LIGHT_GRAY_STAINED_GLASS);
        settingsInv.openMenu(p);
    }


    public static void openMaxPlayerBarsInventory(Player p) {
        PopupMenu maxPlayerBarsInv = new PopupMenu(dsp.get("settings.maxPlayerBars.inventory", p), 3);
        if (UserSettings.getInt(UUIDFetcher.getUUID(p.getName()), "baro_maxPlayerBars") < 64) {
            maxPlayerBarsInv.addMenuItem(new MenuItem(getMaxPlayerBarsArrowUp(p)) {
                @Override
                public void onClick(Player player) {
                    UserSettings.setInt(UUIDFetcher.getUUID(p.getName()), "baro_maxPlayerBars", UserSettings.getInt(UUIDFetcher.getUUID(p.getName()), "baro_maxPlayerBars") + 1);
                    openMaxPlayerBarsInventory(p);
                }
            }, 4);
        }

        maxPlayerBarsInv.addMenuItem(new MenuItem(getMaxPlayerBarsItem(p)) {
            @Override
            public void onClick(Player player) {
            }
        }, 13);

        if (UserSettings.getInt(UUIDFetcher.getUUID(p.getName()), "baro_maxPlayerBars") > 0) {
            maxPlayerBarsInv.addMenuItem(new MenuItem(getMaxPlayerBarsArrowDown(p)) {
                @Override
                public void onClick(Player player) {
                    UserSettings.setInt(UUIDFetcher.getUUID(p.getName()), "baro_maxPlayerBars", UserSettings.getInt(UUIDFetcher.getUUID(p.getName()), "baro_maxPlayerBars") - 1);
                    openMaxPlayerBarsInventory(p);
                }
            }, 22);
        }

        maxPlayerBarsInv.fill(Material.LIGHT_GRAY_STAINED_GLASS);
        maxPlayerBarsInv.openMenu(p);
    }


    public static ItemStack getMaxPlayerBarsArrowUp(Player p) {
        ItemStack item = Data.heads.get("arrowUp");
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setDisplayName(dsp.get("settings.maxPlayerBars.arrowUp", p));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getMaxPlayerBarsArrowDown(Player p) {
        ItemStack item = Data.heads.get("arrowDown");
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setDisplayName(dsp.get("settings.maxPlayerBars.arrowDown", p));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getMaxPlayerBarsItem(Player p) {
        if (UserSettings.getInt(UUIDFetcher.getUUID(p.getName()), "baro_maxPlayerBars") > 0) {
            ItemStack item = heads.get("maxPlayerBars");
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setDisplayName(dsp.get("settings.maxPlayerBars.item", p));
            item.setItemMeta(meta);
            item.setAmount(UserSettings.getInt(UUIDFetcher.getUUID(p.getName()), "baro_maxPlayerBars"));
            return item;
        } else {
            ItemStack item = heads.get("barrier");
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setDisplayName(dsp.get("settings.maxPlayerBars.item", p));
            item.setItemMeta(meta);
            return item;
        }
    }
}
