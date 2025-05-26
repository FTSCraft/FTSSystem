package de.ftscraft.ftssystem.listeners;

import de.ftscraft.ftssystem.features.TeleportWarmupManager;
import de.ftscraft.ftssystem.main.FtsSystem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener implements Listener {

    private FtsSystem plugin;

    public PlayerDamageListener(FtsSystem plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        handleTeleportWarmup(event);

    }

    private static void handleTeleportWarmup(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player p) {
            TeleportWarmupManager.playerReceivedDamage(p);
        }
    }

}
