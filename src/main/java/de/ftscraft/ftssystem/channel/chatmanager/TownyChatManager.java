/*
 * Copyright (c) 2021.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.channel.chatmanager;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import de.ftscraft.ftsengine.utils.Ausweis;

import static de.ftscraft.ftsengine.utils.Ausweis.Gender;

import de.ftscraft.ftssystem.channel.Channel;
import de.ftscraft.ftssystem.channel.ChannelType;
import de.ftscraft.ftssystem.configs.Messages;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.main.User;
import de.ftscraft.ftssystem.scoreboard.TeamPrefixs;
import de.ftscraft.ftssystem.utils.Utils;
import de.ftscraft.ftssystem.utils.hooks.EngineHook;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;


public class TownyChatManager extends ChatManager {

    final TownyAPI api;

    public TownyChatManager(FtsSystem plugin) {
        super(plugin);
        api = TownyAPI.getInstance();
    }

    public void chat(User user, String message) {
        Channel activeChannel = user.getActiveChannel();
        if (activeChannel == null) {
            user.getPlayer().sendMessage(Messages.CHOOSE_CHANNEL);
            return;
        }
        chat(user, message, activeChannel);
    }

    private TextComponent buildComponent(String text, String username) {
        TextComponent.Builder componentBuilder = Component.text();
        String code = "";

        for (String word : text.split(" ")) {
            if (word.startsWith("§")) {
                code = word.substring(1, 2);
            }
            if (word.matches("^(https?://|§[0-9a-fk-or]{0,1}https?://).*")) {
                String url = word.replaceFirst("§[0-9a-fk-or]*", "");
                componentBuilder.append(
                        Component.text("§b[LINK]§r")
                                .clickEvent(ClickEvent.openUrl(url))
                                .hoverEvent(HoverEvent.showText(Component.text(Utils.getTitleFromWebsite(url) + "\n§7" + url)))
                ).append(Component.space());
            } else if (word.startsWith("§r§a")) {
                componentBuilder.append(
                        Component.text(word.replace("_", " "))
                                .hoverEvent(HoverEvent.showText(Component.text(username)))
                ).append(Component.space());
            } else {
                componentBuilder.append(Component.text(code.isEmpty() ? word + " " : "§" + code + word + " "));
            }

            if (word.contains("§")) {
                code = word.substring(word.lastIndexOf("§") + 1, word.lastIndexOf("§") + 2);
            }
        }

        return componentBuilder.build();
    }

    public void chat(User u, String msg, Channel channel) {
        if (channel == null) {
            u.getPlayer().sendMessage(Messages.CHOOSE_CHANNEL);
            return;
        }
        if (!u.getEnabledChannels().contains(channel)) {
            try {
                u.joinChannel(channel);
            } catch (Exception ex) {
                plugin.getLogger().severe("Exception while chatting");
            }
        }

        String formatted = format(u, channel, msg);

        TextComponent c = buildComponent(formatted, u.getPlayer().getName());

        if (channel.type() == ChannelType.NORMAL || channel.type() == null) {

            boolean anyoneRecived = false;
            for (User b : plugin.getUser().values()) {
                if (b.getEnabledChannels().contains(channel)) {
                    switch (channel.prefix().toLowerCase()) {
                        case "g" -> {
                            if (b.getGlobalChannelStatus() == User.ChannelStatusSwitch.OFF) {
                                continue;
                            } else if (b.getGlobalChannelStatus() == User.ChannelStatusSwitch.RP && plugin.getScoreboardManager().isInRoleplayMode(b.getPlayer())) {
                                continue;
                            }
                        }
                        case "ooc" -> {
                            if (b.getOocChannelStatus() == User.ChannelStatusSwitch.OFF) {
                                continue;
                            } else if (b.getOocChannelStatus() == User.ChannelStatusSwitch.RP && plugin.getScoreboardManager().isInRoleplayMode(b.getPlayer())) {
                                continue;
                            }
                        }
                    }
                    if (channel.range() != -1) {
                        if (b.getPlayer().getWorld().getName().equalsIgnoreCase(u.getPlayer().getWorld().getName())) {
                            if (b.getPlayer().getLocation().distance(u.getPlayer().getLocation()) <= channel.range()) {
                                if (b != u)
                                    anyoneRecived = true;
                                b.getPlayer().sendMessage(c);
                            }
                        }
                    } else {
                        b.getPlayer().sendMessage(c);
                        if (b != u)
                            anyoneRecived = true;
                    }
                }
            }


            if (!anyoneRecived && !u.getPlayer().hasPermission("ftssystem.chat.noinfo")) {
                u.getPlayer().sendMessage("§cNiemand hat deine Nachricht gelesen. Schreibe ein ! vor deine Nachricht um in den Globalchat zu schreiben");
            }

        } else if (channel.type() == ChannelType.FACTION_F) {

            Resident resident = api.getResident(u.getPlayer());

            if (resident == null)
                return;
            if (!resident.hasTown()) {
                u.getPlayer().sendMessage("Du bist in keiner Stadt.");
                return;
            }

            Town town;
            try {
                town = TownyAPI.getInstance().getResident(u.getPlayer()).getTown();
            } catch (NotRegisteredException e) {
                throw new RuntimeException(e);
            }

            for (Resident b : town.getResidents()) {
                if (b.getPlayer() != null) {
                    if ((plugin.getUser(b.getPlayer()).getEnabledChannels().contains(channel))) {
                        User t = plugin.getUser(b.getPlayer());
                        if (t.getFactionChannelStatus() == User.ChannelStatusSwitch.OFF) {
                            continue;
                        } else if (t.getFactionChannelStatus() == User.ChannelStatusSwitch.RP && plugin.getScoreboardManager().isInRoleplayMode(b.getPlayer())) {
                            continue;
                        }
                        b.getPlayer().sendMessage(c);
                    }
                }
            }

        }

        FtsSystem.getChatLogger().info(u.getPlayer().getName() + " [" + channel.prefix() + "] " + msg);

    }

    private String format(User user, Channel channel, String message) {
        String formatted = channel.format();

        Ausweis ausweis = EngineHook.getEngine().getAusweis(user.getPlayer());
        Gender gender = (ausweis != null && ausweis.getGender() != null) ? ausweis.getGender() : Gender.MALE;

        String prefix = TeamPrefixs.getPrefix(user.getPlayer(), gender);
        String name = user.getPlayer().getName();


        formatted = formatted.replace("%fa", getFactionName(user))
                .replace("%pr", prefix)
                .replace("%ch", channel.name())
                .replace("%cp", channel.prefix())
                .replace("%na", getUserDisplayName(user, channel))
                .replace("&", "§");

        formatted = formatted
                .replace("%msg",
                        user.getPlayer().hasPermission("ftssystem.chat.color")
                                ? ChatColor.translateAlternateColorCodes('&', message) : message)
                .replace("((", "§7((")
                .replace("))", "§7))§r");

        return formatted;
    }

    /**
     * returns faction name of faction from user
     *
     * @param user user to get faction name from
     * @return Empty string when no faction
     */
    private String getFactionName(@NotNull User user) {
        String faction = "";
        if (api.getResident(user.getPlayer()).hasTown()) {
            try {
                faction = api.getResident(user.getPlayer()).getTown().getName().replace("_", " ");
            } catch (NotRegisteredException e) {
                throw new RuntimeException(e);
            }
        }
        return faction;
    }

    private String getUserDisplayName(User user, Channel channel) {
        Ausweis ausweis = EngineHook.getEngine().getAusweis(user.getPlayer());
        String name = user.getPlayer().getName();
        if (plugin.getScoreboardManager().isInRoleplayMode(user.getPlayer())
                && ausweis != null
                && !channel.name().equalsIgnoreCase("Global")
                && !channel.name().equalsIgnoreCase("OOC")) {
            boolean deckname = user.isUsingDeckname();
            if (!deckname) {
                name = "§a" + ausweis.getFirstName()
                        .replace(" ", "_") + "_" +
                        ausweis.getLastName().replace(" ", "_");
            } else {
                name = "§a" + ausweis.getSpitzname().replace(" ", "_");
            }
        }
        return name;
    }


}
