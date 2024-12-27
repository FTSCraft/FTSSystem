package de.ftscraft.ftssystem.commands;

import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftssystem.configs.Messages;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.main.User;
import de.ftscraft.ftssystem.utils.hooks.EngineHook;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DecknameCMD implements CommandExecutor {

    private final FtsSystem plugin;

    public DecknameCMD(FtsSystem plugin) {
        this.plugin = plugin;

        PluginCommand command = plugin.getCommand("deckname");
        if (command != null)
            command.setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(cs instanceof Player p)) {
            cs.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        Ausweis ausweis = EngineHook.getEngine().getAusweis(p);
        String deckname = ausweis.getSpitzname();
        if (deckname == null) {
            cs.sendMessage(Messages.PREFIX + "Bitte setze dir zuvor einen Decknamen.");
            return true;
        }

        User user = plugin.getUser(p);
        boolean nowUsingDeckname = !user.isUsingDeckname();
        user.setUseDeckname(nowUsingDeckname);

        p.sendMessage(nowUsingDeckname ?
                Messages.PREFIX + "Du benutzt jetzt einen Decknamen" :
                Messages.PREFIX + "Du benutzt jetzt keinen Decknamen mehr");

        return true;
    }
}
