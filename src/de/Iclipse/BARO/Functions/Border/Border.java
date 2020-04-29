package de.Iclipse.BARO.Functions.Border;

import org.bukkit.Location;

public class Border {
    private Location middleOld;
    private Location middleNew;
    private Integer radiusOld;
    private Integer radiusNew;
    private Double progress;

    public Border(Location middle, Integer radius) {
        this.middleOld = middle;
        this.radiusOld = radius;
        this.middleNew = middle;
        this.radiusNew = radius;
        this.progress = 1.0;

    }

    public Location getMiddleOld() {
        return middleOld;
    }

    public void setMiddleOld(Location middleOld) {
        this.middleOld = middleOld;
    }

    public Location getMiddleNew() {
        return middleNew;
    }

    public void setMiddleNew(Location middleNew) {
        this.middleNew = middleNew;
    }

    public int getRadiusOld() {
        return radiusOld;
    }

    public void setRadiusOld(int radiusOld) {
        this.radiusOld = radiusOld;
    }

    public int getRadiusNew() {
        return radiusNew;
    }

    public void setRadiusNew(int radiusNew) {
        this.radiusNew = radiusNew;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public Location getCurrentMiddle() {
        if (middleNew != null) {
            if (progress < 1) {
                return new Location(middleOld.getWorld(), middleOld.getX() + (middleNew.getX() - middleOld.getX()) * progress, middleOld.getY() + (middleNew.getY() - middleOld.getY()) * progress, middleOld.getZ() + (middleNew.getZ() - middleOld.getZ()) * progress);
            } else {
                return middleNew;
            }
        } else {
            return middleOld;
        }
    }


    public double getCurrentRadius() {
        if (radiusNew != null) {
            if (progress < 1) {
                return radiusOld + (radiusNew - radiusOld) * progress;
            } else {
                return radiusNew;
            }
        } else {
            return radiusOld;
        }
    }
}
