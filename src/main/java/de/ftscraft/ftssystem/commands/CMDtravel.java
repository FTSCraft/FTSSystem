package de.ftscraft.ftssystem.commands;

import de.ftscraft.ftssystem.configs.Messages;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.travelsystem.TravelType;
import de.ftscraft.ftssystem.utils.hooks.EssentialsHook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMDtravel implements CommandExecutor {

    private final FtsSystem plugin;

    public CMDtravel(FtsSystem plugin) {
        this.plugin = plugin;
        plugin.getCommand("travel").setExecutor(this);
    }

    /**
     * /travel SPIELER ZU TYP
     */
    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (cs instanceof Player) {

            if(!cs.hasPermission("ftssystem.helfer"))
                return true;

            cs.sendMessage("So einstellen: ");
            cs.sendMessage("/svs add <server> travel <player> WARP TYP-ID");
            cs.sendMessage("Tausche WARP mit dem Namen des Warps aus und TYP-ID mit der passenden Zahl:");
            TravelType[] types = TravelType.values();
            for (int i = 0; i < types.length; i++) {
                cs.sendMessage(i + ": " + types[i].name() + " ("+types[i].getPrice()+" Taler)");
            }

            return true;
        }

        if (args.length != 3) {
            cs.sendMessage("Hier ist was falsch eingestellt!");
            return true;
        }

        Player t = Bukkit.getPlayer(args[0]);
        if(t == null) {
            cs.sendMessage("Spieler " + args[0] + " nicht gefunden!");
            return true;
        }

        Location loc = EssentialsHook.getWarpLocation(args[1]);
        if (loc == null) {
            FtsSystem.getPluginLogger().warning("Tried to warp to " + args[1] + " via Essentials, didn't work");
            return true;
        }

        int price;
        try {
             price = TravelType.values()[Integer.parseInt(args[2])].getPrice();
        } catch (NumberFormatException ex) {
            cs.sendMessage("Keine Zahl für TravelType angegeben");
            return true;
        }
        if (!plugin.getEcon().has(t, price)) {
            t.sendMessage(Messages.PREFIX + "Du hast nicht genügend Geld um hiermit zu reisen. Du brauchst §c" + price + " §7Taler!");
            return true;
        }

        t.teleport(loc);
        plugin.getEcon().withdrawPlayer(t, price);

        t.sendMessage(Messages.PREFIX + "Dir wurden §c" + price + " §7Taler abgenommen.");

        return true;
    }

}
