/*
 * Copyright (c) 2020.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.commands;

import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.main.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMDpasswort implements CommandExecutor {

    private final String[] commands = {"lp user %s promote spieler", "warp kutschenplatz %s"};

    private final FtsSystem plugin;

    public CMDpasswort(FtsSystem plugin) {
        this.plugin = plugin;
        plugin.getCommand("passwort").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(commandSender instanceof Player p)) {
            return true;
        }

        if (args.length == 1) {

            String password = "kiwi";
            if (args[0].equalsIgnoreCase(password)) {

                User user = plugin.getUser(p);

                boolean neuling = !user.isApproved();

                if (neuling) {

                    for (String s : commands) {

                        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), s.replace("%s", p.getName()));

                    }

                    user.setApproved(true);

                    p.sendMessage("§cDu hast dich erfolgreich freigeschaltet!");

                } else {
                    p.sendMessage("§cDu hast dich bereits freigeschaltet!");
                }

            } else {

                p.sendMessage("§cDas war nicht das richtige Passwort! Bitte versuchen Sie es erneut");

            }

        } else
            p.sendMessage("§cBitte benutze den Befehl so: /passwort [Passwort]");

        return false;
    }

}
