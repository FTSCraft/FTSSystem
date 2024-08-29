/*
 * Copyright (c) 2019.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.commands;

import de.ftscraft.ftssystem.configs.Messages;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.punishment.PunishmentInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMDakte implements CommandExecutor {

    private final FtsSystem plugin;

    public CMDakte(FtsSystem plugin) {
        this.plugin = plugin;
        plugin.getCommand("akte").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (!(cs instanceof Player p)) {
            cs.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        if (args.length == 0) {

            p.openInventory(new PunishmentInventory(plugin, cs.getName()).getInv(PunishmentInventory.PunishmentInvType.AKTE));

        }

        return false;
    }


}
