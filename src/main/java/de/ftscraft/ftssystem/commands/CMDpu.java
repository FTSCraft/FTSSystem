/*
 * Copyright (c) 2018.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.commands;

import de.ftscraft.ftssystem.configs.Messages;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.punishment.Punishment;
import de.ftscraft.ftssystem.punishment.PunishmentInventory;
import de.ftscraft.ftssystem.punishment.TemporaryPunishment;
import de.ftscraft.ftssystem.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static de.ftscraft.ftssystem.utils.Utils.msg;

public class CMDpu implements CommandExecutor {

    private final FtsSystem plugin;

    public CMDpu(FtsSystem plugin) {
        this.plugin = plugin;
        plugin.getCommand("pu").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (!(cs instanceof Player p)) {
            cs.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        if (!p.hasPermission("ftssystem.punish")) {
            p.sendMessage(Messages.NO_PERM);
            return true;
        }

        switch (args.length) {
            case 1 -> {
                OfflinePlayer t = Bukkit.getOfflinePlayer(args[0]);
                p.openInventory(new PunishmentInventory(plugin, t.getName())
                        .getInv(PunishmentInventory.PunishmentInvType.MAIN));
            }
            case 2 -> {
                if (args[0].equalsIgnoreCase("remove")) {
                    msg(p, "<red>Bist du sicher? " +
                            "Das kann nicht rückgängig gemacht werden! Wenn ja, klicke: </red>");
                    Component component = Component.text("Hier")
                            .color(NamedTextColor.RED)
                            .clickEvent(ClickEvent.clickEvent(
                                    ClickEvent.Action.RUN_COMMAND, "/pu remove " + args[1] + " confirm"
                            ))
                            .decorate(TextDecoration.BOLD);
                    p.sendMessage(component);
                } else if (args[0].equalsIgnoreCase("tilgen")) {
                    if (!p.hasPermission("ftssystem.admin")) {
                        p.sendMessage("Dieser Befehl ist nur für Admins.");
                        return true;
                    }
                    msg(p, "<red>Bist du sicher? " +
                            "Das <dark_red>komplette Löschen aus der Akte</dark_red> " +
                            "kann nicht rückgängig gemacht werden! Wenn ja, klicke:");
                    Component component = Component.text("Hier").color(NamedTextColor.RED)
                            .clickEvent(ClickEvent.clickEvent(
                                    ClickEvent.Action.RUN_COMMAND, "/pu tilgen " + args[1] + " confirm"
                            ))
                            .decorate(TextDecoration.BOLD);
                    p.sendMessage(component);
                }
            }
            case 3 -> {
                if (args[0].equalsIgnoreCase("remove") && args[2].equalsIgnoreCase("confirm")) {

                    int id = Integer.parseInt(args[1]);

                    Punishment pu = plugin.getPunishmentManager().getPunishmentById(id);

                    if (pu == null) {
                        p.sendMessage("§cIrgendwas ist schief gelaufen. Probier es nochmal");
                        return true;
                    }

                    pu.setActive(false);
                    plugin.getPunishmentManager().savePunishment(pu);

                    p.sendMessage("§cOkay. Die Strafe wurde Deaktiviert");


                } else if (args[0].equalsIgnoreCase("tilgen") && args[2].equalsIgnoreCase("confirm")) {
                    if (!p.hasPermission("ftssystem.admin")) {
                        p.sendMessage("Dieser Befehl ist nur für Admins.");
                        return true;
                    }
                    int id = Integer.parseInt(args[1]);

                    Punishment pu = plugin.getPunishmentManager().getPunishmentById(id);

                    if (pu == null) {
                        p.sendMessage("§cIrgendwas ist schief gelaufen. Probier es nochmal");
                        return true;
                    }

                    if (plugin.getPunishmentManager().deletePunishment(id)) {
                        p.sendMessage(Component.text("Okay. Die Strafe wurde gelöscht.")
                                .color(NamedTextColor.RED));
                    } else {
                        p.sendMessage(Component.text("Da ist was schief gelaufen. " +
                                "Entweder gibt es die Strafe nicht oder etwas läuft ganz falsch.")
                                .color(NamedTextColor.RED));
                    }


                } else if(args[0].equalsIgnoreCase("bis")) {
                    int id = Integer.parseInt(args[1]);
                    Punishment pu = plugin.getPunishmentManager().getPunishmentById(id);
                    if (pu == null) {
                        p.sendMessage("§cIrgendwas ist schief gelaufen. Lade einmal die Akte des Spielers neu.");
                        return true;
                    }
                    if (!(pu instanceof TemporaryPunishment temporaryPunishment)) {
                        p.sendMessage("§cDort kannst du nicht die Laufzeit ändern. Es ist nicht temporär.");
                        return true;
                    }
                    long until = Utils.calculateUntil(args[2]);
                    if (until == -1) {
                        p.sendMessage("§cDu hast keinen gültigen Zeitraum angegeben.");
                        return true;
                    }
                    temporaryPunishment.setUntil(until);
                    temporaryPunishment.setMoreInfo(temporaryPunishment.getMoreInfo() + " Laufzeit auf " + temporaryPunishment.untilAsString() + " geändert von " + p.getName());
                    plugin.getPunishmentManager().savePunishment(temporaryPunishment);
                    p.sendMessage(Component.text("Die Laufzeit wurde auf " + temporaryPunishment.untilAsString() + " geändert.").color(NamedTextColor.RED));
                }
            }
        }

        return false;
    }
}
