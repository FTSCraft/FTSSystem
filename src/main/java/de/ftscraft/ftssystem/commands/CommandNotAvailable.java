package de.ftscraft.ftssystem.commands;

import de.ftscraft.ftssystem.main.FtsSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandNotAvailable implements CommandExecutor {

    private final String message;

    public CommandNotAvailable(FtsSystem plugin, String command, String message) {
        plugin.getCommand(command).setExecutor(this);
        this.message = message;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage("Dieser Command ist nicht verfügbar weil: " + message);
        return false;
    }
}
