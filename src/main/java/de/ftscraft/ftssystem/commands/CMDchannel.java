/*
 * Copyright (c) 2018.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.commands;

import de.ftscraft.ftssystem.channel.Channel;
import de.ftscraft.ftssystem.configs.Messages;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.main.User;
import de.ftscraft.ftssystem.utils.FTSCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMDchannel implements FTSCommand {

    private final FtsSystem plugin;

    public CMDchannel(FtsSystem plugin) {
        this.plugin = plugin;
        plugin.getCommand("channel").setExecutor(this);
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player p)) {
            cs.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("list")) {
                p.sendMessage(Messages.LIST_CHANNEL);
                for (Channel a : plugin.getChatManager().channels) {
                    if (p.hasPermission(a.getPermission()))
                        p.sendMessage("- " + ChatColor.AQUA + ChatColor.BOLD + a.getName());
                }


            } else if (args[0].equalsIgnoreCase("join")) {
                p.sendMessage(Messages.PREFIX + "Bitte benutze /fts um Channel zu verlassen oder zu joinen.");
            } else if (args[0].equalsIgnoreCase("leave")) {
                p.sendMessage(Messages.PREFIX + "Bitte benutze /fts um Channel zu verlassen.");
            } else if (args[0].equalsIgnoreCase("aktiv")) {
                if (args.length == 2) {
                    User u = plugin.getUser(p);
                    Channel channel = plugin.getChatManager().getChannel(args[1]);
                    if (channel == null) {
                        p.sendMessage(Messages.NO_CHANNEL);
                        return true;
                    }
                    if (!p.hasPermission(channel.getPermission())) {
                        cs.sendMessage(Messages.NO_PERM);
                        return true;
                    }

                    u.setActiveChannel(channel);
                    String str = channel.getName();
                    String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
                    p.sendMessage(Messages.NOW_ACTIVE_CHANNEL.replace("%s", cap));

                } else p.sendMessage(Messages.HELP_CHANNEL);
            } else if (args[0].equalsIgnoreCase("toggle")) {
                p.sendMessage(Messages.PREFIX + "Bitte benutze /fts um Channel zu verlassen oder zu joinen.");
            } else p.sendMessage(Messages.HELP_CHANNEL);
        } else p.sendMessage(Messages.HELP_CHANNEL);

        return false;
    }
}
