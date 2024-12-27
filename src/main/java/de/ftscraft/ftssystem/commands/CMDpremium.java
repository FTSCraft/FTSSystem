/*
 * Copyright (c) 2020.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.commands;

import de.ftscraft.ftssystem.configs.Messages;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.main.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMDpremium implements CommandExecutor {

    private final FtsSystem plugin;

    public CMDpremium(FtsSystem plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("premium").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (!(cs instanceof Player p)) {
            cs.sendMessage("Dieser Command ist nur für Spieler :)");
            return true;
        }

        User u = plugin.getUser(p);

        if (args.length == 0) {

            if (p.hasPermission("ftssystem.premium")) {

                p.sendMessage(help());

            } else p.sendMessage(Messages.NO_PERM);

        } else if (args.length == 1) {

            if (p.hasPermission("ftssystem.premium")) {

                if (args[0].equalsIgnoreCase("automessage")) {

                    if (p.hasPermission("ftssystem.toggleservermessages")) {

                        u.setTurnedServerMessagesOn(!u.turnedServerMessagesOn());

                        if (u.turnedServerMessagesOn())
                            p.sendMessage(Messages.PREFIX + "§7Du hast nun für dich die automatischen Servernachrichten wieder §cangestellt!");
                        if (!u.turnedServerMessagesOn())
                            p.sendMessage(Messages.PREFIX + "§7Du hast nun für dich die automatischen Servernachrichten §causgestellt");

                        return true;

                    } else p.sendMessage(Messages.NO_PERM);
                } else p.sendMessage(help());
            } else p.sendMessage(Messages.NO_PERM);
        } else p.sendMessage(help());

        return false;
    }

    private String help() {

        return
                Messages.PREFIX + "Hilfestellung für /premium: \n" +
                "§7/premium automessage §e- §7Schalte die automatischen Servernachrichten an/aus!\n" +
                "§7/item §e- §7Benenne deine Items um!\n" +
                "§7/hat §e- §7Zieh dir den Block, den du derzeit in der Hand hast auf den Kopf!\n";

    }

}
