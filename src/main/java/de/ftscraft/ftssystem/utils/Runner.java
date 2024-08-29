/*
 * Copyright (c) 2018.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.utils;

import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.main.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Random;

public class Runner implements Runnable {

    private final FtsSystem plugin;

    private int time_to_message = Variables.AUTO_MESSAGE_COOLDOWN;

    private int time_to_update = Variables.SCOREBOARD_UPDATE_COOLDOWN;

    public Runner(FtsSystem plugin) {
        this.plugin = plugin;
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 20, 20);
    }

    @Override
    public void run() {

        time_to_message--;
        time_to_update--;

        if (time_to_message == 0) {

            if (plugin.getConfigManager().getAutoMessages().size() > 1) {

                time_to_message = Variables.AUTO_MESSAGE_COOLDOWN;
                int index = new Random().nextInt(plugin.getConfigManager().getAutoMessages().size());
                String msg = plugin.getConfigManager().getAutoMessages().get(index);
                for (Player a : Bukkit.getOnlinePlayers()) {
                    //If player turned the messages off and do have the permission, go to next player
                    if (!plugin.getUser(a).turnedServerMessagesOn() && a.hasPermission("ftssystem.toggleservermessages"))
                        continue;
                    a.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                }

            }

        }

        if (time_to_update == 0) {
            if (plugin.getScoreboardManager() != null)
                plugin.getScoreboardManager().sendToAllScoreboard();
            time_to_update = Variables.SCOREBOARD_UPDATE_COOLDOWN;
        }


        for (User all : plugin.getUser().values()) {

            //Combat

            if (!all.getFights().isEmpty()) {

                for (Player fights : all.getFights().keySet()) {
                    if (all.getFights().get(fights) == 0) {
                        all.getFights().remove(fights);
                        if (all.getFights().isEmpty()) {
                            all.getPlayer().sendMessage("§cDu bist nicht mehr im Kampf");
                        }
                        continue;
                    }
                    all.getFights().put(fights, all.getFights().get(fights) - 1);
                }

            }

        }


    }

}
