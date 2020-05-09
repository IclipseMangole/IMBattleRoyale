package de.Iclipse.BARO.Functions.Chests;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;

public class Item {


    private static ArrayList<Item> chestItems = new ArrayList<>();
    private static ArrayList<Item> dropItems = new ArrayList<>();
    private final ItemStack item;
    private final int chestMaxAmount;
    private final int chestMinAmount;
    private final int dropMaxAmount;
    private final int dropMinAmount;

    public Item(ItemStack item, int chestMaxAmount, int chestMinAmount, int dropMaxAmount, int dropMinAmount) {
        this.item = item;
        this.chestMaxAmount = chestMaxAmount;
        this.chestMinAmount = chestMinAmount;
        this.dropMaxAmount = dropMaxAmount;
        this.dropMinAmount = dropMinAmount;
        if (chestMaxAmount > 0) {
            chestItems.add(this);
        }
        if (dropMaxAmount > 0) {
            dropItems.add(this);
        }
    }

    public static ArrayList<Item> getChestItems() {
        return chestItems;
    }

    public static ArrayList<Item> getDropItems() {
        return dropItems;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getChestMaxAmount() {
        return chestMaxAmount;
    }

    public int getChestMinAmount() {
        return chestMinAmount;
    }

    public int getDropMaxAmount() {
        return dropMaxAmount;
    }

    public int getDropMinAmount() {
        return dropMinAmount;
    }


    public static void loadItems() {
        new Item(new ItemStack(Material.IRON_SWORD), 0, 0, 1, 1);
        new Item(new ItemStack(Material.COOKED_BEEF), 10, 5, 0, 0);
        new Item(new ItemStack(Material.BAKED_POTATO), 10, 5, 0, 0);
        new Item(new ItemStack(Material.DIAMOND_SWORD), 0, 0, 1, 1);
        new Item(new ItemStack(Material.WOODEN_SWORD), 1, 1, 0, 0);
        new Item(new ItemStack(Material.WOODEN_AXE), 1, 1, 0, 0);
        new Item(new ItemStack(Material.LEATHER_CHESTPLATE), 1, 1, 0, 0);
        new Item(new ItemStack(Material.LEATHER_BOOTS), 1, 1, 0, 0);
        new Item(new ItemStack(Material.LEATHER_LEGGINGS), 1, 1, 0, 0);
        new Item(new ItemStack(Material.LEATHER_HELMET), 1, 1, 0, 0);
        new Item(new ItemStack(Material.IRON_INGOT), 5, 2, 8, 6);
        new Item(new ItemStack(Material.STONE_PICKAXE), 1, 1, 0, 0);
        new Item(new ItemStack(Material.IRON_PICKAXE), 0, 0, 1, 1);
        new Item(new ItemStack(Material.STONE_AXE), 1, 1, 0, 0);
        new Item(new ItemStack(Material.DIRT), 32, 16, 0, 0);
        new Item(new ItemStack(Material.OAK_WOOD), 16, 16, 0, 0);
        new Item(new ItemStack(Material.GLOWSTONE), 10, 10, 0, 0);
        new Item(new ItemStack(Material.OAK_LOG), 5, 2, 0, 0);
        new Item(new ItemStack(Material.CRAFTING_TABLE), 5, 1, 0, 0);
        new Item(new ItemStack(Material.FURNACE), 5, 1, 0, 0);
        new Item(new ItemStack(Material.COBBLESTONE), 32, 16, 0, 0);
        new Item(new ItemStack(Material.TNT), 0, 0, 5, 1);
        new Item(new ItemStack(Material.SADDLE), 0, 0, 1, 1);
        new Item(new ItemStack(Material.COAL), 5, 1, 10, 5);
        new Item(new ItemStack(Material.DIAMOND), 0, 0, 3, 1);
        new Item(new ItemStack(Material.STICK), 10, 10, 0, 0);
        new Item(new ItemStack(Material.STRING), 5, 1, 0, 0);
        new Item(new ItemStack(Material.FEATHER), 5, 1, 0, 0);
        new Item(new ItemStack(Material.FLINT), 5, 1, 0, 0);
        new Item(new ItemStack(Material.WATER_BUCKET), 0, 0, 1, 1);
        new Item(new ItemStack(Material.LAVA_BUCKET), 0, 0, 1, 1);
        new Item(new ItemStack(Material.ENDER_PEARL), 0, 0, 3, 1);
        new Item(new ItemStack(Material.HORSE_SPAWN_EGG), 0, 0, 1, 1);
        new Item(new ItemStack(Material.VILLAGER_SPAWN_EGG), 0, 0, 1, 1);
        new Item(new ItemStack(Material.EXPERIENCE_BOTTLE), 5, 1, 10, 5);
        new Item(new ItemStack(Material.EMERALD), 5, 1, 10, 5);
        new Item(new ItemStack(Material.APPLE), 10, 5, 0, 0);
        new Item(new ItemStack(Material.BREAD), 10, 5, 0, 0);
        new Item(new ItemStack(Material.GOLDEN_APPLE), 0, 0, 2, 2);
        new Item(new ItemStack(Material.MELON_SLICE), 20, 10, 0, 0);
        new Item(new ItemStack(Material.WOODEN_SHOVEL), 1, 1, 0, 0);
        new Item(new ItemStack(Material.WOODEN_PICKAXE), 1, 1, 0, 0);
        new Item(new ItemStack(Material.STONE_SHOVEL), 1, 1, 0, 0);
        new Item(new ItemStack(Material.DIAMOND_PICKAXE), 0, 0, 1, 1);
        new Item(new ItemStack(Material.DIAMOND_AXE), 0, 0, 1, 1);
        new Item(new ItemStack(Material.BOW), 1, 1, 0, 0);
        new Item(getEnderdragonKiller(), 0, 0, 1, 1);
        new Item(new ItemStack(Material.ARROW), 5, 5, 10, 10);
        new Item(new ItemStack(Material.DIAMOND_SWORD), 0, 0, 1, 1);
        new Item(getUltimativerAbschlachter(), 0, 0, 1, 1);
        new Item(new ItemStack(Material.CHAINMAIL_BOOTS), 1, 1, 0, 0);
        new Item(new ItemStack(Material.CHAINMAIL_LEGGINGS), 1, 1, 0, 0);
        new Item(new ItemStack(Material.CHAINMAIL_CHESTPLATE), 1, 1, 0, 0);
        new Item(new ItemStack(Material.CHAINMAIL_HELMET), 1, 1, 0, 0);
        new Item(new ItemStack(Material.IRON_BOOTS), 0, 0, 1, 1);
        new Item(new ItemStack(Material.IRON_LEGGINGS), 0, 0, 1, 1);
        new Item(new ItemStack(Material.IRON_CHESTPLATE), 0, 0, 1, 1);
        new Item(new ItemStack(Material.IRON_HELMET), 0, 0, 1, 1);
        new Item(new ItemStack(Material.DIAMOND_CHESTPLATE), 0, 0, 1, 1);
        new Item(getPuhlisPulli(), 0, 0, 1, 1);
        new Item(getPoisonArrow(), 0, 0, 3, 3);
        new Item(new ItemStack(Material.SHIELD), 0, 0, 1, 1);
        new Item(new ItemStack(Material.CROSSBOW), 1, 1, 0, 0);
        new Item(getFranksBier(), 0, 0, 1, 1);
        new Item(getHealPotion(), 1, 1, 1, 1);
        new Item(getWeaknessPotion(), 0, 0, 1, 1);
        new Item(getSlownessPotion(), 0, 0, 1, 1);
        new Item(new ItemStack(Material.MILK_BUCKET), 1, 1, 0, 0);


    }

    public static ItemStack getEnderdragonKiller() {
        ItemStack item = new ItemStack(Material.BOW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§4§lEnderDragon-Killer");
        meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
        meta.addEnchant(Enchantment.ARROW_KNOCKBACK, 0, false);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getUltimativerAbschlachter() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§4§lUltimativer-Abschlachter");
        meta.addEnchant(Enchantment.FIRE_ASPECT, 0, false);
        meta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getPuhlisPulli() {
        ItemStack item = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§4§lPuhli's Pulli");
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getFranksBier() {
        ItemStack item = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        PotionEffect potionEffect = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 1);
        meta.addCustomEffect(potionEffect, false);
        meta.setColor(Color.MAROON);
        meta.setDisplayName("§4§lFrank's Bier");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getPoisonArrow(){
        ItemStack item = new ItemStack(Material.TIPPED_ARROW);
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        meta.setBasePotionData(new PotionData(PotionType.POISON));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getHealPotion() {
        ItemStack item = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        PotionEffect potionEffect = new PotionEffect(PotionEffectType.HEAL, 0, 0);
        meta.addCustomEffect(potionEffect, false);
        meta.setDisplayName("§fPotion of Healing");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getSlownessPotion() {
        ItemStack item = new ItemStack(Material.SPLASH_POTION);
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        PotionEffect potionEffect = new PotionEffect(PotionEffectType.SLOW, 600, 0);
        meta.addCustomEffect(potionEffect, false);
        meta.setDisplayName("§fPotion of Slowness");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getWeaknessPotion() {
        ItemStack item = new ItemStack(Material.SPLASH_POTION);
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        PotionEffect potionEffect = new PotionEffect(PotionEffectType.WEAKNESS, 600, 0);
        meta.addCustomEffect(potionEffect, false);
        meta.setDisplayName("§fPotion of Weakness");
        item.setItemMeta(meta);
        return item;
    }

}
