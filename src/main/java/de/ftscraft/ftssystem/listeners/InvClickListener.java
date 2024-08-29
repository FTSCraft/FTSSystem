/*
 * Copyright (c) 2018.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.listeners;

import de.ftscraft.ftssystem.configs.Messages;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.main.User;
import de.ftscraft.ftssystem.punishment.*;
import de.ftscraft.ftssystem.utils.UUIDFetcher;
import de.ftscraft.ftssystem.utils.hooks.EssentialsHook;
import de.ftscraft.ftsutils.items.ItemReader;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class InvClickListener implements Listener {

    private final FtsSystem plugin;

    private final ArrayList<OfflinePlayer> printed = new ArrayList<>();

    public InvClickListener(FtsSystem plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (event.getView().getTitle().startsWith("§cPunishment")) {
            event.setCancelled(true);

            ItemStack item = event.getCurrentItem();
            if (item == null) {
                return;
            }
            ItemMeta itemMeta = item.getItemMeta();

            ItemStack skull = event.getInventory().getItem(4);
            ItemMeta skullMeta = skull.getItemMeta();

            String target = skullMeta.getDisplayName().replace("§c", "");

            if (item.getType() == Material.BLACK_STAINED_GLASS_PANE) {
                return;
            }

            Player p = (Player) event.getWhoClicked();
            if (p.hasPermission("ftssystem.punish"))
                if (itemMeta.getDisplayName().equalsIgnoreCase("§4Ban")) {
                    PunishmentBuilder prog = new PunishmentBuilder(plugin, PunishmentType.BAN, p, target);
                    prog.setChatProgress(PunishmentManager.ChatProgress.REASON);
                    p.closeInventory();
                    if (plugin.getPunishmentManager().isBanned(UUIDFetcher.getUUID(target))) {
                        p.sendMessage("§cDieser Spieler ist schon gebannt!");
                        return;
                    }
                    p.sendMessage("§cBitte schreibe den Grund");
                } else if (itemMeta.getDisplayName().equalsIgnoreCase("§4Tempban")) {
                    PunishmentBuilder prog = new PunishmentBuilder(plugin, PunishmentType.TEMP_BAN, p, target);
                    prog.setChatProgress(PunishmentManager.ChatProgress.REASON);
                    p.closeInventory();
                    if (plugin.getPunishmentManager().isBanned(UUIDFetcher.getUUID(target))) {
                        p.sendMessage("§cDieser Spieler ist schon gebannt!");
                        return;
                    }
                    p.sendMessage("§cBitte schreibe den Grund");
                } else if (itemMeta.getDisplayName().equalsIgnoreCase("§cTempmute")) {
                    PunishmentBuilder prog = new PunishmentBuilder(plugin, PunishmentType.TEMP_MUTE, p, target);
                    prog.setChatProgress(PunishmentManager.ChatProgress.REASON);
                    p.closeInventory();
                    if (plugin.getPunishmentManager().isMuted(UUIDFetcher.getUUID(target))) {
                        p.sendMessage("§cDieser Spieler ist schon gemutet!");
                        return;
                    }
                    p.sendMessage("§cBitte schreibe den Grund");
                } else if (itemMeta.getDisplayName().equalsIgnoreCase("§6Warn")) {
                    PunishmentBuilder prog = new PunishmentBuilder(plugin, PunishmentType.WARN, p, target);
                    prog.setChatProgress(PunishmentManager.ChatProgress.REASON);
                    p.closeInventory();
                    p.sendMessage("§cBitte schreibe den Grund");
                } else if (itemMeta.getDisplayName().equalsIgnoreCase("§6Tempwarn")) {
                    PunishmentBuilder prog = new PunishmentBuilder(plugin, PunishmentType.TEMP_WARN, p, target);
                    prog.setChatProgress(PunishmentManager.ChatProgress.REASON);
                    p.closeInventory();
                    p.sendMessage("§cBitte schreibe den Grund");
                } else if (itemMeta.getDisplayName().equalsIgnoreCase("§7Notiz")) {
                    PunishmentBuilder prog = new PunishmentBuilder(plugin, PunishmentType.NOTE, p, target);
                    prog.setChatProgress(PunishmentManager.ChatProgress.REASON);
                    p.closeInventory();
                    p.sendMessage("§cBitte schreibe den Grund");
                } else if (itemMeta.getDisplayName().equalsIgnoreCase("§5Akte")) {
                    p.closeInventory();
                    p.openInventory(new PunishmentInventory(plugin, target).getInv(PunishmentInventory.PunishmentInvType.AKTE));
                }

        } else if (event.getView().getTitle().startsWith("§2Akte")) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();
            if (item == null)
                return;
            ItemMeta meta = item.getItemMeta();
            if (!meta.getDisplayName().equalsIgnoreCase(" ")) {

                if (meta.getDisplayName().equalsIgnoreCase("§6Druck mir das aus!")) {
                    if (printed.contains(event.getWhoClicked())) {
                        if (!event.getWhoClicked().hasPermission("ftssystem.punish")) {
                            event.getWhoClicked().sendMessage("§cDu kannst das nächste mal diese Funktion benutzen nach einem Server-Neustart!");
                            return;
                        }
                    }
                    ItemStack firstPunishment = event.getInventory().getItem(0);
                    UUID p;
                    if (!firstPunishment.getItemMeta().getDisplayName().equalsIgnoreCase(" ")) {
                        int id = Integer.parseInt(firstPunishment.getItemMeta().getLore().get(3));
                        Punishment pun = plugin.getPunishmentManager().getPunishmentById(id);
                        p = pun.getPlayer();
                    } else {
                        event.getWhoClicked().sendMessage("§cDu hast noch keine Strafen!");
                        return;
                    }
                    event.getWhoClicked().sendMessage(Messages.PREFIX + "Hier ist dein Link: §c" + printAkte(p));

                    return;
                }

                int id = Integer.parseInt(meta.getLore().get(3));

                Punishment pun = plugin.getPunishmentManager().getPunishmentById(id);

                Player p = (Player) event.getWhoClicked();

                p.closeInventory();

                p.sendMessage("§5----------------");
                p.sendMessage("§cSpieler: §e" + UUIDFetcher.getName(pun.getPlayer()));
                p.sendMessage("§cAutor: §e" + pun.getAuthorName());
                p.sendMessage("§cGrund: §e" + pun.getReason());
                p.sendMessage("§cTyp: §e" + pun.getType().toString());
                p.sendMessage("§cWeitere Informationen: §e" + pun.getMoreInformation());
                p.sendMessage("§cErstellt am: §e" + pun.createdOn());
                if (pun instanceof TemporaryPunishment temp) {
                    p.sendMessage("§cBis: §e" + temp.untilAsCalString());
                }

                if (p.hasPermission("ftssystem.punish")) {
                    Component management = Component.empty();
                    if (pun.isActive()) {
                        Component deactivate = Component.text("Deaktivieren ")
                                .color(NamedTextColor.DARK_GREEN)
                                .decorate(TextDecoration.BOLD)
                                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/pu remove " + id));
                        management = management.append(deactivate);
                    }
                    if (pun instanceof TemporaryPunishment) {
                        Component retime = Component.text("Laufzeit ändern ")
                                .color(NamedTextColor.DARK_GREEN)
                                .decorate(TextDecoration.BOLD)
                                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pu bis " + id + " LAUFZEIT"))
                                .hoverEvent(HoverEvent.showText(Component.text("LAUFZEIT durch die neue Laufzeit ersetzen. " +
                                        "Wichtig: Die neue Laufzeit geht von dem Zeitpunkt JETZT aus. " +
                                        "Also wenn du 5d angibst ist die neue Laufzeit bis Jetzt in 5 Tagen.")));
                        management = management.append(retime);
                    }

                    if (p.hasPermission("ftssystem.admin")) {
                        Component delete = Component.text("Löschen ")
                                .color(NamedTextColor.RED)
                                .decorate(TextDecoration.BOLD)
                                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/pu tilgen " + id));
                        management = management.append(delete);
                    }
                    p.sendMessage(management);
                }
            }
        } else if (event.getView().getTitle().endsWith("Dein Menü")) {

            User u = plugin.getUser((Player) event.getWhoClicked());

            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();
            if (item == null)
                return;

            ItemMeta meta = item.getItemMeta();

            if (meta.getDisplayName().equalsIgnoreCase("§cNoobschutz")) {
                if (u.hasNoobProtection()) {
                    u.setNoobProtection(false);
                    event.getWhoClicked().sendMessage(Messages.PREFIX + "Du hast deinen Noobschutz erfolgreich aufgegeben. Viel Spaß!");
                    event.getWhoClicked().closeInventory();
                }
            }

            if (!meta.hasLore())
                return;

            String id = meta.getLore().get(meta.getLore().size() - 1).replace("§", "");
            if (id.equalsIgnoreCase("1")) {
                u.setMsgSound(!u.isMsgSoundEnabled());
            } else if (id.equalsIgnoreCase("2")) {
                switch (u.getDisturbStatus()) {
                    case OFF -> u.setDisturbStatus(User.ChannelStatusSwitch.RP);
                    case RP -> u.setDisturbStatus(User.ChannelStatusSwitch.ON);
                    case ON -> u.setDisturbStatus(User.ChannelStatusSwitch.OFF);
                }
            } else if (id.equalsIgnoreCase("3")) {
                u.setScoreboardEnabled(!u.isScoreboardEnabled());
            } else if (id.equalsIgnoreCase("5")) {
                switch (u.getOocChannelStatus()) {
                    case OFF -> u.setOocChannelStatus(User.ChannelStatusSwitch.RP);
                    case RP -> u.setOocChannelStatus(User.ChannelStatusSwitch.ON);
                    case ON -> u.setOocChannelStatus(User.ChannelStatusSwitch.OFF);
                }
            } else if (id.equalsIgnoreCase("6")) {
                switch (u.getFactionChannelStatus()) {
                    case OFF -> u.setFactionChannelStatus(User.ChannelStatusSwitch.RP);
                    case RP -> u.setFactionChannelStatus(User.ChannelStatusSwitch.ON);
                    case ON -> u.setFactionChannelStatus(User.ChannelStatusSwitch.OFF);
                }
            } else if (id.equalsIgnoreCase("7")) {
                switch (u.getGlobalChannelStatus()) {
                    case OFF -> u.setGlobalChannelStatus(User.ChannelStatusSwitch.RP);
                    case RP -> u.setGlobalChannelStatus(User.ChannelStatusSwitch.ON);
                    case ON -> u.setGlobalChannelStatus(User.ChannelStatusSwitch.OFF);
                }
            } else if (id.equalsIgnoreCase("8")) {
                if (plugin.getScoreboardManager() != null)
                    plugin.getScoreboardManager().switchToRoleplayMode(u.getPlayer());
            }

            u.refreshMenu();

        } else if (event.getView().getTitle().equals("§1Schriftrolle")) {
            event.setCancelled(true);
            ItemStack is = event.getCurrentItem();
            if (is == null) return;
            String warp = ItemReader.getPDC(is, "SCROLL", PersistentDataType.STRING);
            if (warp == null) return;
            Location warpLoc = EssentialsHook.getWarpLocation(warp);
            if (warpLoc == null)
                return;
            event.getWhoClicked().closeInventory();
            event.getWhoClicked().teleport(warpLoc);
            event.getWhoClicked().getInventory().getItemInMainHand().setAmount(event.getWhoClicked().getInventory().getItemInMainHand().getAmount() - 1);
        }


    }

    /**
     * @param p UUID of the player which akte should be printed
     * @return The URL of the telegraph document
     */
    private String printAkte(UUID p) {
        String content = "[{\"tag\":\"p\",\"children\":[\"REPLACE\"]}]";
        //content = "[{\"tag\":\"p\",\"children\":[\"Strafe+1:+Griefing+\\nStrafe+2:+Trolling\"]}]";

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < plugin.getPunishmentManager().getPlayers().get(p).size(); i++) {
            Punishment pun = plugin.getPunishmentManager().getPlayers().get(p).get(i);
            stringBuilder.append("Strafe ").append(i + 1).append(": ").append(pun.getReason()).append("%5Cn");
            stringBuilder.append("  - Weitere Infos: ").append(pun.getMoreInformation()).append("%5Cn");
            stringBuilder.append("  - Typ: ").append(pun.getType()).append("%5Cn");
            if (pun instanceof TemporaryPunishment) {
                stringBuilder.append("  - Bis: ").append(((TemporaryPunishment) pun).untilAsCalString()).append("%5Cn");
            }
            stringBuilder.append("  - Autor: ").append(pun.getAuthorName()).append("%5Cn");
            stringBuilder.append("  - Deaktiviert: ").append(!pun.isActive()).append("%5Cn %5Cn");
        }

        content = content.replace("REPLACE", stringBuilder.toString().replace(" ", "+"));

        URL url = null;
        try {
            url = new URL("https://api.telegra.ph/createPage?access_token=6cf9217c73e4da3913dc2d9f878423ebd713ff7fd4d9ab6d087b16f48f9b&title=Strafen:+" + UUIDFetcher.getName(p) + "&content=" + content + "&author_name=FTS-System");
        } catch (MalformedURLException e) {
            plugin.getLogger().severe("While creating url for telegraph api");
        }

        Scanner sc = null;
        try {
            sc = new Scanner(url.openStream());
        } catch (IOException e) {
            plugin.getLogger().severe("IO Exception while creating scanner for telegraph url");
        }

        StringBuilder sb = new StringBuilder();
        while (sc.hasNext()) {
            sb.append(sc.next());
        }

        String result = sb.toString();

        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(result);
            JSONObject jobj = ((JSONObject) obj);
            JSONObject obj2 = (JSONObject) jobj.get("result");
            return obj2.get("url").toString();
        } catch (ParseException e) {
            plugin.getLogger().severe("Parse exception when trying to receive url");
        }
        return null;
    }

}
