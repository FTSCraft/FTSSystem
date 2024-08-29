/*
 * Copyright (c) 2021.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.listeners;

import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.main.User;
import net.ess3.api.events.AfkStatusChangeEvent;
import net.ess3.api.events.PrivateMessageSentEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EssentialsListener implements Listener {

    private final FtsSystem plugin;

    public EssentialsListener(FtsSystem plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onMessageRecive(PrivateMessageSentEvent event) {

        Player t = Bukkit.getPlayer(event.getRecipient().getName());

        if (t != null) {

            User u = plugin.getUser(t);

            if (!u.isMsgSoundEnabled())
                return;

            t.playSound(t.getLocation(), Sound.ENTITY_CHICKEN_EGG, SoundCategory.VOICE, 1, 1);

        }

    }

    @EventHandler
    public void onAfkChange(AfkStatusChangeEvent event) {

        Player t = Bukkit.getPlayer(event.getAffected().getName());

        if (t != null) {
            if (plugin.getScoreboardManager() != null)
                plugin.getScoreboardManager().changeAfkStatus(t, event.getValue());
        }

    }

}
