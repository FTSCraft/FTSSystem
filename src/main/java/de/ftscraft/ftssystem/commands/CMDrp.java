package de.ftscraft.ftssystem.commands;

import de.ftscraft.ftssystem.configs.Messages;
import de.ftscraft.ftssystem.main.FtsSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMDrp implements CommandExecutor {

    private FtsSystem plugin;

    public CMDrp(FtsSystem plugin) {
        this.plugin = plugin;
        plugin.getCommand("rp").setExecutor(this::onCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        plugin.getScoreboardManager().switchToRoleplayMode(player);

        return false;
    }
}
