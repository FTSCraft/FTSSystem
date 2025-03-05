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

        if (plugin.getScoreboardManager() != null)
            plugin.getScoreboardManager().setPlayerPrefix(p);

        event.joinMessage(generateJoinMessage(p));

        User u = new User(plugin, event.getPlayer());

        checkForPoll(event, u);
        checkNoobProtection(u, p);
        checkPremium(p);

    }

    /**
     * If a poll is active and user does not have 'Do Not Disturb' enabled, show it to them
     */
    private void checkForPoll(PlayerJoinEvent event, User u) {
        if (u.getDisturbStatus().equals(User.ChannelStatusSwitch.ON)) {
            return;
        }
        Umfrage umfrage = plugin.getUmfrage();
        if (umfrage == null || !umfrage.isStarted()) {
            return;
        }
        if (umfrage.getTeilnehmer().contains(event.getPlayer())) {
            return;
        }
        Bukkit.getScheduler().runTaskLater(plugin, () -> umfrage.sendToPlayer(event.getPlayer()), 20 * 2);

    }

    /**
     * If user has Noob Protection and more than 50h playtime, remove noob protection
     */
    private void checkNoobProtection(User u, Player p) {
        if (u.hasNoobProtection() && p.getStatistic(Statistic.PLAY_ONE_MINUTE) >= 20 * 60 * 60 * 50) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                u.setNoobProtection(false);
                p.sendMessage(Messages.PREFIX + "Da du jetzt 50h Spielstunden hast und du immer noch die Noobprotection an hattest, wurde sie jetzt entfernt");
            }, 20 * 4);
        }
    }

    /**
     * If Luckperms is available, look up if player has premium and if, for how long
     */
    private void checkPremium(Player p) {

        if (!HookManager.LUCK_PERMS_ENABLED) {
            return;
        }

        PremiumManager premiumManager = plugin.getPremiumManager();
        if (p.hasPermission("group.premium")) {
            premiumManager.addPremiumPlayer(p.getUniqueId(), LuckPermsHook.getParentDuration(p.getUniqueId(), "Premium"));
        } else {
            premiumManager.removePremiumPlayer(p.getUniqueId());
        }

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
