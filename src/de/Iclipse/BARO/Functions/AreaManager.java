package de.Iclipse.BARO.Functions;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;

public class AreaManager{

    public static ArrayList<Area> areas;

    public static ArrayList<Area> getAreas(Location loc){
        return areas;
    }

    public boolean isInArea(Location loc, Area area){
        Location loc1 = area.getLoc1();
        Location loc2 = area.getLoc2();
        if (loc1.getBlockX() <= loc.getBlockX() && loc.getBlockX() <= loc2.getBlockX() || loc2.getBlockX() <= loc.getBlockX() && loc.getBlockX() <= loc1.getBlockX()) {
            if (loc1.getBlockZ() <= loc.getBlockZ() && loc.getBlockZ() <= loc2.getBlockX() || loc2.getBlockZ() <= loc.getBlockZ() && loc.getBlockZ() <= loc1.getBlockZ()) {
                return true;
            }
        }
        return false;
    }

    public static Area getArea(Location loc){
        for (Area entry : areas) {
            if (entry.contains(loc)) {
                return entry;
            }
        }
        return null;
    }


    public class Area {
        private World world;
        private Location loc1;
        private Location loc2;

        public Area(Location loc1, Location loc2) {
            this.loc1 = loc1;
            this.loc2 = loc2;
            world = loc1.getWorld();
        }

        public World getWorld() {
            return world;
        }

        public void setWorld(World world) {
            this.world = world;
        }

        public Location getLoc1() {
            return loc1;
        }

        public void setLoc1(Location loc1) {
            this.loc1 = loc1;
        }

        public Location getLoc2() {
            return loc2;
        }

        public void setLoc2(Location loc2) {
            this.loc2 = loc2;
        }

        public boolean contains(Location loc){
            if (loc1.getBlockX() <= loc.getBlockX() && loc.getBlockX() <= loc2.getBlockX() || loc2.getBlockX() <= loc.getBlockX() && loc.getBlockX() <= loc1.getBlockX()) {
                if (loc1.getBlockZ() <= loc.getBlockZ() && loc.getBlockZ() <= loc2.getBlockX() || loc2.getBlockZ() <= loc.getBlockZ() && loc.getBlockZ() <= loc1.getBlockZ()) {
                    return true;
                }
            }
            return false;
        }

    }
}

