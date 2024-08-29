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


public class TownyChatManager extends ChatManager {

    final TownyAPI api;

    public TownyChatManager(FtsSystem plugin) {
        super(plugin);
        api = TownyAPI.getInstance();
    }

    public void chat(User u, String msg) {
        Channel a = u.getActiveChannel();
        if (a == null) {
            u.getPlayer().sendMessage(Messages.CHOOSE_CHANNEL);
            return;
        }
        if (u.getActiveChannel() == null) {
            u.getPlayer().sendMessage(Messages.NO_ACTIVE_CHANNEL);
        }

        chat(u, msg, a);

    }

    private TextComponent buildComponent(String text, String user) {

        TextComponent component = Component.text("");
        String code = "";
        for (String s : text.split(" ")) {
            if (s.startsWith("§")) {
                code = s.substring(1, 2);
            }
            if (s.length() > 4 && (s.startsWith("https://") || s.startsWith("http://") || s.substring(2).startsWith("https://") || s.substring(2).startsWith("http://"))) {
                if (s.substring(2).startsWith("https://") || s.substring(2).startsWith("http://")) {
                    s = s.substring(2);
                }
                TextComponent textComponent = Component.text("§b[LINK]§r")
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, s))
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text(Utils.getTitleFromWebsite(s) + "\n" + "§7" + s)));
                component = component.append(textComponent).append(Component.text(" "));
            } else if (s.startsWith("§r§a")) {
                TextComponent textComponent = Component.text(s.replace("_", " "))
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text(user)));
                component = component.append(textComponent).append(Component.text(" "));
            } else {
                if (!code.isEmpty())
                    component = component.append(Component.text("§" + code + s + " "));
                else component = component.append(Component.text(s + " "));
            }

            if (s.contains("§"))
                code = String.valueOf(s.charAt(s.lastIndexOf("§") + 1));

        }
        return component;
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

    private String format(User u, Channel c, String msg) {
        String f = c.format();
        String faction = "";

        Ausweis ausweis = EngineHook.getEngine().getAusweis(u.getPlayer());
        Gender gender;
        if (ausweis == null)
            gender = Gender.MALE;
        else
            gender = ausweis.getGender();
        if (gender == null)
            gender = Gender.MALE;

        String prefix = TeamPrefixs.getPrefix(u.getPlayer(), gender);
        String name = u.getPlayer().getName();
        String channelName = c.name();
        if (api.getResident(u.getPlayer()).hasTown()) {
            try {
                faction = api.getResident(u.getPlayer()).getTown().getName();
            } catch (NotRegisteredException e) {
                throw new RuntimeException(e);
            }
        }

        f = f.replace("%fa", faction);
        f = f.replace("%pr", prefix);
        f = f.replace("%ch", channelName);
        f = f.replace("%cp", c.prefix());
        f = f.replace("&", "§");
        //Wenn der Spieler im RP Modus ist, wird der eigentliche Name mit dem Namen ausgetauscht der im Ausweis angegeben ist, wenn ein Ausweis vorhanden ist
        if (plugin.getScoreboardManager().isInRoleplayMode(u.getPlayer())) {
            //Wenn der Ausweis nicht existiert, die Variable mit dem normalen Spielernamen ergenzen
            if (ausweis == null || c.name().equalsIgnoreCase("Global") || c.name().equalsIgnoreCase("OOC")) {
                f = f.replace("%na", name);
            } else {
                f = f.replace("%na", "§a" + ausweis.getFirstName().replace(" ", "_") + "_" + ausweis.getLastName().replace(" ", "_") + "§r");
                if (u.getPlayer().hasPermission("ftssystem.chat.color")) {
                    f = f.replace("&", "§");
                }
            }
        } else
            f = f.replace("%na", name);

        f = f.replace("%msg", (u.getPlayer().hasPermission("ftssystem.chat.color") ? ChatColor.translateAlternateColorCodes('&', msg) : msg));
        f = f.replace("((", "§7((");
        f = f.replace("))", "§7))§r");

        return f;
    }


}
