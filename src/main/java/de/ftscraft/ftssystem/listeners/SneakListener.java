/*
 * Copyright (c) 2020.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.listeners;

import de.ftscraft.ftssystem.main.FtsSystem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class SneakListener implements Listener {

    private final FtsSystem plugin;

    public SneakListener(FtsSystem plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {

        Player p = event.getPlayer();

        if (plugin.getScoreboardManager() != null)
            plugin.getScoreboardManager().toggleSneakingMode(p);

    }

}
