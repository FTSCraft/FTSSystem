package de.ftscraft.ftssystem.listeners;

import de.ftscraft.ftssystem.main.FtsSystem;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;

public class EntityDropItemListener implements Listener {

    public EntityDropItemListener(FtsSystem plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityDropItem(EntityDropItemEvent event) {

        if (event.getEntity().getType() == EntityType.ARMADILLO) {
            event.setCancelled(true);
        }

    }

}
