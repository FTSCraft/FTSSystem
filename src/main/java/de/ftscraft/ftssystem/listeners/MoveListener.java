package de.ftscraft.ftssystem.listeners;

import de.ftscraft.ftssystem.features.TeleportWarmupManager;
import de.ftscraft.ftssystem.main.FtsSystem;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    private FtsSystem plugin;

    public MoveListener(FtsSystem plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        handleTeleportWarmup(event);
    }

    private void handleTeleportWarmup(PlayerMoveEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (event.getTo().distance(event.getFrom()) > .01)
                TeleportWarmupManager.playerMoved(event.getPlayer());
        });
    }

}
