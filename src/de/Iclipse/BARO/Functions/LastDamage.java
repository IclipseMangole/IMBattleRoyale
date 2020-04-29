package de.Iclipse.BARO.Functions;

import de.Iclipse.BARO.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class LastDamage implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            if (Data.lastDamager.containsKey(e.getEntity())) {
                Data.lastDamager.replace((Player) e.getEntity(), new LastDamager((Player) e.getDamager(), System.currentTimeMillis()));
            } else {
                Data.lastDamager.put((Player) e.getEntity(), new LastDamager((Player) e.getDamager(), System.currentTimeMillis()));
            }
        }
    }


    public class LastDamager {

        private Player lastDamager;
        private long lastDamageTime;

        public LastDamager(Player lastDamager, long lastDamageTime) {
            this.lastDamager = lastDamager;
            this.lastDamageTime = lastDamageTime;
        }

        public Player getLastDamager() {
            return lastDamager;
        }

        public void setLastDamager(Player lastDamager) {
            this.lastDamager = lastDamager;
        }

        public long getLastDamageTime() {
            return lastDamageTime;
        }

        public void setLastDamageTime(long lastDamage) {
            this.lastDamageTime = lastDamage;
        }
    }
}
