/*
 * Copyright (c) 2018.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.listeners;

import de.ftscraft.ftssystem.main.FtsSystem;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class EntityDeathListener implements Listener {

    public EntityDeathListener(FtsSystem plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {

        if (event.getEntity().getType() == EntityType.PLAYER)
            return;

        if (event.getEntity().getType() == EntityType.ZOMBIFIED_PIGLIN) {
            event.getDrops().clear();
        } else if(event.getEntity().getType() == EntityType.WITHER_SKELETON) {
            event.getDrops().removeIf(drop -> drop.getType() == Material.COAL);
        } else if(event.getEntity().getType() == EntityType.DROWNED) {
            event.getDrops().removeIf(drop -> drop.getType() == Material.COPPER_INGOT);
        }

    }

}
