/*
 * Copyright (c) 2018.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.listeners;

import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftssystem.configs.Messages;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.main.User;
import de.ftscraft.ftssystem.punishment.PunishmentBuilder;
import de.ftscraft.ftssystem.punishment.PunishmentManager;
import de.ftscraft.ftssystem.punishment.PunishmentType;
import de.ftscraft.ftssystem.punishment.TemporaryPunishment;
import de.ftscraft.ftssystem.utils.Utils;
import de.ftscraft.ftssystem.utils.hooks.EngineHook;
import de.ftscraft.ftssystem.utils.hooks.HookManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    private final FtsSystem plugin;

    public ChatListener(FtsSystem plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncChatEvent event) {
        String msg = PlainTextComponentSerializer.plainText().serialize(event.message());
        if (plugin.getPunishmentManager().getBuilders().get(event.getPlayer()) != null) {
            event.setCancelled(true);
            PunishmentBuilder prog = plugin.getPunishmentManager().getBuilders().get(event.getPlayer());
            switch (prog.getChatProgress()) {
                case REASON -> {
                    prog.setReason(msg);
                    event.getPlayer().sendMessage(Messages.PREFIX + "Okay. Bitte schreibe jetzt weitere Infos (zB Beweise, wer es Bemerkt hat). Schreibe wenn es nicht in den Chat passt ein + am Ende. Dann kannst du mit der nächsten Nachricht weiterschreiben");
                    prog.setChatProgress(PunishmentManager.ChatProgress.MOREINFO);
                }
                case MOREINFO -> {
                    if (msg.endsWith("+")) {
                        if (prog.getMoreInfo() == null)
                            prog.setMoreInfo("");
                        prog.setMoreInfo(prog.getMoreInfo() + " " + msg.substring(0, msg.length() - 1));
                        return;
                    } else {
                        if (prog.getMoreInfo() == null)
                            //Wenn es noch keine Nachricht davor gab, setzt er es zu der Nachricht die gerade kam
                            prog.setMoreInfo(msg);
                        else
                            //Sonst das was schon da ist mit nem Leerzeichen und der Nachricht die gerade kam verbinden
                            prog.setMoreInfo(prog.getMoreInfo() + " " + msg);

                        if (PunishmentType.isTemporary(prog.getType())) {
                            event.getPlayer().sendMessage(Messages.PREFIX + "Okay. Bitte schreibe jetzt wie lange es andauern soll. zB(3d; 3w)");
                            prog.setChatProgress(PunishmentManager.ChatProgress.TIME);
                            return;
                        }
                    }
                    prog.setChatProgress(PunishmentManager.ChatProgress.PROOF);
                    event.getPlayer().sendMessage(Messages.PREFIX + "Bitte überprüfe nochmal deine Daten. Du kannst diese später NICHT ändern. Schreibe dann Ja oder Nein");
                    event.getPlayer().sendMessage(Messages.PREFIX + "Target: §e" + prog.getPlayer());
                    event.getPlayer().sendMessage(Messages.PREFIX + "Grund: §e" + prog.getReason());
                    event.getPlayer().sendMessage(Messages.PREFIX + "Weitere Infos: §e" + prog.getMoreInfo());
                    if (prog.getUntil() != null)
                        event.getPlayer().sendMessage(Messages.PREFIX + "Zeitraum: §e" + Utils.convertToTime(prog.getUntilInMillis()));
                }
                case TIME -> {
                    prog.setUntil(msg);
                    prog.setChatProgress(PunishmentManager.ChatProgress.PROOF);
                    event.getPlayer().sendMessage(Messages.PREFIX + "Bitte überprüfe nochmal deine Daten. Du kannst diese später NICHT ändern. Schreibe dann Ja oder Nein");
                    event.getPlayer().sendMessage(Messages.PREFIX + "Target: §e" + prog.getPlayer());
                    event.getPlayer().sendMessage(Messages.PREFIX + "Grund: §e" + prog.getReason());
                    event.getPlayer().sendMessage(Messages.PREFIX + "Weitere Infos: §e" + prog.getMoreInfo());
                    if (prog.getUntil() != null)
                        event.getPlayer().sendMessage(Messages.PREFIX + "Zeitraum: §e" + Utils.convertToTime(prog.getUntilInMillis()));
                }
                case PROOF -> {
                    if (msg.equalsIgnoreCase("Ja")) {

                        prog.setProofed(true);
                        plugin.getPunishmentManager().getBuilders().remove(event.getPlayer());

                        event.getPlayer().sendMessage(Messages.PREFIX + "§cOkay. Deine Anfrage wird bearbeitet");
                        prog.build();
                        event.getPlayer().sendMessage(Messages.PREFIX + "§cFertig. §eDer Spieler hat seine Strafe erhalten.");

                    } else if (msg.equalsIgnoreCase("Nein")) {
                        prog.abort();
                        event.getPlayer().sendMessage(Messages.PREFIX + "Okay. Das Setup wurde abgebrochen. Wenn du es erneut versuchen willst, gebe wieder /pu SPIELER ein");
                    }
                }
            }
        }

        if (plugin.getPunishmentManager().isMuted(event.getPlayer()) != null) {
            TemporaryPunishment mute = (TemporaryPunishment) plugin.getPunishmentManager().isMuted(event.getPlayer());
            event.getPlayer().sendMessage(Messages.PREFIX + "Du bist noch " + mute.untilAsString() + " lang gemuted aufgrund von: §e" + mute.getReason());
            event.setCancelled(true);
        }

        if (event.isCancelled()) {
            return;
        }

        User u = plugin.getUser(event.getPlayer());
        if (u == null) {
            event.getPlayer().sendMessage("§cIrgendwas ist schief gelaufen. Probier mal zu reconnecten!");
            return;
        }
        event.setCancelled(true);
        if (HookManager.ESSENTIALS_ENABLED) {
            if (msg.startsWith("*")) {
                if (msg.startsWith(String.valueOf('*'))) {

                    String[] msgs = msg.split(" ");

                    for (int i = 0; i < msgs.length; i++) {
                        for (Player a : Bukkit.getOnlinePlayers()) {
                            if (EngineHook.getEngine().hasAusweis(a)) {
                                if (a.getName().equalsIgnoreCase(msgs[i])) {
                                    msgs[i] = EngineHook.getEngine().getAusweis(a).getFirstName() + " " + EngineHook.getEngine().getAusweis(a).getLastName();
                                }
                            }
                        }
                    }

                    StringBuilder newMsg;

                    Ausweis a = EngineHook.getEngine().getAusweis(event.getPlayer());

                    if (a == null) {
                        event.getPlayer().sendPlainMessage("§cBitte erstell dir erst einen Ausweis");
                        return;
                    }

                    msgs[0] = msgs[0].substring(1);

                    if (!msg.startsWith("**")) {

                        newMsg = new StringBuilder("§e" + a.getFirstName() + " " + a.getLastName() + " (" + event.getPlayer().getName() + ") ");

                    } else {
                        msgs[0] = msgs[0].substring(1);
                        newMsg = new StringBuilder("§e");
                    }

                    //msgs[0].replace("*", "");

                    for (String m : msgs) {
                        newMsg.append(m).append(" ");
                    }

                    newMsg = new StringBuilder(newMsg.toString().replace("((", "§7(("));
                    newMsg = new StringBuilder(newMsg.toString().replace("))", "§7))§e"));

                    newMsg = new StringBuilder(turnEverythingInQuotationsWhite(newMsg.toString()));

                    event.setCancelled(true);

                    for (User b : plugin.getUser().values()) {
                        if (b.getPlayer().getWorld().getName().equalsIgnoreCase(u.getPlayer().getWorld().getName())) {
                            if (b.getPlayer().getLocation().distance(u.getPlayer().getLocation()) <= u.getActiveChannel().range()) {
                                b.getPlayer().sendMessage(newMsg.toString());
                            }
                        }
                    }

                    FtsSystem.getChatLogger().info(event.getPlayer().getName() + " [RP] " + msg);
                }
                return;
            }
        }
        if (msg.startsWith("!")) {
            plugin.getChatManager().chat(u, msg.replaceFirst("!", ""), plugin.getChatManager().getChannel("Global"));
            return;
        }

        plugin.getChatManager().chat(u, msg);

    }

    private String turnEverythingInQuotationsWhite(String text) {
        StringBuilder n = new StringBuilder();
        boolean waitingForNextQuotation = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '"') {
                n.append("§r\"");
                if (waitingForNextQuotation) {
                    n.append("§e");
                    waitingForNextQuotation = false;
                } else {
                    waitingForNextQuotation = true;
                }
            } else n.append(c);
        }
        return n.toString();
    }


}
