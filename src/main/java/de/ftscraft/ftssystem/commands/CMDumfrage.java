/*
 * Copyright (c) 2018.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.commands;

import de.ftscraft.ftssystem.configs.Messages;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.poll.Umfrage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMDumfrage implements CommandExecutor {

    private final FtsSystem plugin;

    public CMDumfrage(FtsSystem plugin) {
        this.plugin = plugin;
        plugin.getCommand("umfrage").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(cs instanceof Player p)) {
            cs.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("create")) {
                if (!cs.hasPermission("ftssystem.umfrage")) {
                    return true;
                }
                if (plugin.getUmfrage() != null) {
                    cs.sendMessage(Messages.UMFRAGE_ALREADY_CONFIG);
                    return true;
                }
                if (args.length > 2) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        stringBuilder.append(args[i]);
                        stringBuilder.append(" ");
                    }
                    plugin.setUmfrage(new Umfrage(stringBuilder.toString(), plugin));
                    p.sendMessage(Messages.UMFRAGE_CREATED.replace("%s", stringBuilder.toString()));
                }
            } else if (args[0].equalsIgnoreCase("addoption")) {
                if (!cs.hasPermission("ftssystem.umfrage")) {
                    return true;
                }
                if (args.length >= 2) {
                    Umfrage umfrage = plugin.getUmfrage();
                    if (umfrage == null) {
                        cs.sendMessage(Messages.NO_UMFRAGE_FOUND);
                        return true;
                    }
                    if (umfrage.isStarted()) {
                        cs.sendMessage(Messages.UMFRAGE_ALREADY_STARTED);
                        return true;
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        stringBuilder.append(args[i]);
                        stringBuilder.append(" ");
                    }
                    umfrage.addAntwort(stringBuilder.toString());
                    cs.sendMessage(Messages.UMFRAGE_ADDED_OPTION.replace("%s", stringBuilder.toString()));
                }
            } else if (args[0].equalsIgnoreCase("start")) {
                if (!cs.hasPermission("ftssystem.umfrage")) {
                    return true;
                }
                if (args.length == 1) {
                    Umfrage umfrage = plugin.getUmfrage();
                    if (umfrage == null) {
                        cs.sendMessage(Messages.NO_UMFRAGE_FOUND);
                        return true;
                    }
                    if (umfrage.isStarted()) {
                        cs.sendMessage(Messages.UMFRAGE_ALREADY_STARTED);
                        return true;
                    }
                    umfrage.start();
                    cs.sendMessage(Messages.UMFRAGE_STATED);
                }
            } else if (args[0].equalsIgnoreCase("end")) {
                if (!cs.hasPermission("ftssystem.umfrage")) {
                    return true;
                }
                if (args.length == 1) {
                    Umfrage umfrage = plugin.getUmfrage();
                    if (umfrage == null) {
                        cs.sendMessage(Messages.NO_UMFRAGE_FOUND);
                        return true;
                    }
                    if (!umfrage.isStarted()) {
                        cs.sendMessage(Messages.UMFRAGE_NOT_STARTED);
                        return true;
                    }
                    umfrage.end();
                    plugin.setUmfrage(null);
                }
            } else if (args[0].equalsIgnoreCase("vote")) {
                if (args.length == 2) {
                    //Get Umfrage
                    Umfrage umfrage = plugin.getUmfrage();
                    //Check if Umfrage is null
                    if (umfrage == null) {
                        cs.sendMessage(Messages.NO_UMFRAGE_FOUND);
                        return true;
                    }
                    //Set Variable
                    int id;
                    //Get Id from Args
                    try {
                        id = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        cs.sendMessage(Messages.NUMBER);
                        return true;
                    }
                    //Add Vote to Umfrage
                    umfrage.addVote(p, id);
                }
            } else if (args[0].equalsIgnoreCase("resend")) {

                if (!cs.hasPermission("ftssystem.umfrage")) {
                    return true;
                }

                Umfrage umfrage = plugin.getUmfrage();
                umfrage.sendPollMessage(true);

            }
        }

        return false;
    }

}
