/*
 * Copyright (c) 2018.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.listeners;

import de.ftscraft.ftssystem.configs.Messages;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.main.User;
import de.ftscraft.ftssystem.poll.Umfrage;
import de.ftscraft.ftssystem.utils.PremiumManager;
import de.ftscraft.ftssystem.utils.hooks.HookManager;
import de.ftscraft.ftssystem.utils.hooks.LuckPermsHook;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final FtsSystem plugin;

    public JoinListener(FtsSystem plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {

        Player p = event.getPlayer();

        event.joinMessage(generateJoinMessage(p));

        User u = new User(plugin, event.getPlayer());
        u.userStartup();
    }

    private TextComponent generateJoinMessage(Player p) {
        TextComponent joinMessage = Component.text(p.getName());

        if (p.hasPermission("ftssystem.join.lightblue")) {
            joinMessage = joinMessage.color(NamedTextColor.AQUA);
        } else if (p.hasPermission("ftssystem.join.darkred")) {
            joinMessage = joinMessage.color(NamedTextColor.DARK_RED);
        } else if (p.hasPermission("ftssystem.join.darkgreen")) {
            joinMessage = joinMessage.color(NamedTextColor.DARK_GREEN);
        } else if (p.hasPermission("ftssystem.join.red")) {
            joinMessage = joinMessage.color(NamedTextColor.RED);
        } else if (p.hasPermission("ftssystem.join.blue")) {
            joinMessage = joinMessage.color(NamedTextColor.BLUE);
        } else {
            joinMessage = joinMessage.color(NamedTextColor.GOLD);
        }
        return joinMessage.append(Component.text(" hat Eldoria betreten").color(NamedTextColor.WHITE));
    }

}
