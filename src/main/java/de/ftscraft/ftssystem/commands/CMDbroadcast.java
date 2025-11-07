/*
 * Copyright (c) 2021.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.commands;

import de.ftscraft.ftssystem.configs.Messages;
import de.ftscraft.ftssystem.main.FtsSystem;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMDbroadcast implements CommandExecutor {

    private final FtsSystem plugin;
    private static final PlayerPointsAPI PLAYER_POINTS_API = PlayerPoints.getInstance().getAPI();

    public CMDbroadcast(FtsSystem plugin) {
        this.plugin = plugin;
        plugin.getCommand("broadcast").setExecutor(this);
    }

    private static int PRICE = 25;

    @Override
    public boolean onCommand(CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args) {

        boolean needsToPay = false;

        if (!(cs instanceof Player player)) {
            return true;
        }

        if (!cs.hasPermission("ftssystem.broadcast")) {
            if (PLAYER_POINTS_API.look(player.getUniqueId()) >= PRICE) {
                needsToPay = true;
            } else {
                cs.sendMessage(Messages.PREFIX + "Das kannst du dir nicht leisten. Es kostet " + PRICE + " PP.");
                return true;
            }
        }

        if (args.length >= 1) {

            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < args.length; i++) {
                if (i == args.length - 1) {
                    builder.append(args[i]);
                } else
                    builder.append(args[i]).append(" ");
            }

            for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
                onlinePlayer.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("[§cEin Bote berichtet§r] §a" + builder));
            }

            if (needsToPay)
                PLAYER_POINTS_API.take(player.getUniqueId(), 25);

        } else cs.sendMessage(Messages.PREFIX + "Bitte gebe eine Nachricht an.");

        return false;
    }
}
