/*
 * Copyright (c) 2018.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.features.poll;

import de.ftscraft.ftssystem.configs.Messages;
import de.ftscraft.ftssystem.main.FtsSystem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

import static de.ftscraft.ftssystem.utils.Utils.sendMessageToAllExceptDisturb;

public class Umfrage {

    private final String frage;
    private final ArrayList<String> antwortmoglichkeiten;
    private final HashMap<String, Integer> antworten;
    private final ArrayList<Player> teilnehmer;
    private boolean started = false;
    private final FtsSystem plugin;

    public Umfrage(String frage, FtsSystem plugin) {
        this.frage = frage;
        this.plugin = plugin;
        antworten = new HashMap<>();
        teilnehmer = new ArrayList<>();
        antwortmoglichkeiten = new ArrayList<>();
    }

    public void addAntwort(String antwort) {
        antworten.put(antwort, 0);
        antwortmoglichkeiten.add(antwort);
    }

    public void addVote(Player p, int antwort) {
        if (teilnehmer.contains(p)) {
            p.sendMessage(Messages.PREFIX + "Du hast schon gevotet!");
            return;
        }
        teilnehmer.add(p);
        String a = antwortmoglichkeiten.get(antwort);
        antworten.put(a, antworten.get(a) + 1);
        p.sendMessage("§cDu hast erfolgreich für §7" + a + " §cgestimmt");
    }

    public void start() {
        started = true;
        sendPollMessage(false);
    }

    public void sendPollMessage(boolean resend) {

        for (Player all : Bukkit.getOnlinePlayers()) {

            if (plugin.getUser(all).isDisturbable()) {

                if (!resend) {
                    all.sendMessage(Messages.PREFIX + "Es wurde eine Umfrage gestartet:");
                } else
                    all.sendMessage(Messages.PREFIX + "Es läuft derzeit eine Umfrage");
                all.sendMessage("§7Frage: §c" + frage);
                for (int i1 = 0; i1 < antwortmoglichkeiten.size(); i1++) {
                    String i = antwortmoglichkeiten.get(i1);
                    TextComponent a = Component.text(i)
                            .color(NamedTextColor.BLUE)
                            .clickEvent(
                                    ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/umfrage vote " + i1)
                            );

                    all.sendMessage(a);
                    if (i1 == antwortmoglichkeiten.size() - 1) {
                        if (teilnehmer.contains(all)) {
                            all.sendMessage("§c(Keine Sorge, du hast schon abgestimmt)");
                        } else {
                            all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        }
                    }
                }

            }

        }


    }

    public void end() {
        started = false;
        sendMessageToAllExceptDisturb(Messages.PREFIX + "Die Umfrage wurde beendet!", plugin);
        sendResultMessage();
    }

    private void sendResultMessage() {
        int teilnehmerzahl = teilnehmer.size();
        sendMessageToAllExceptDisturb(Messages.PREFIX + "Es haben " + teilnehmerzahl + " Leute abgestimmt", plugin);
        for (String a : antwortmoglichkeiten) {
            int w = antworten.get(a);
            String p = ((double) w / (double) teilnehmerzahl * (double) 100) + "%";
            Bukkit.broadcast(LegacyComponentSerializer.legacyAmpersand().deserialize(Messages.PREFIX + "Es haben §c" + w + "(" + p + ") §7für §c" + a + " §7gestimmt!"));
        }
    }

    public boolean isStarted() {
        return started;
    }

    public ArrayList<Player> getTeilnehmer() {
        return teilnehmer;
    }

    public void sendToPlayer(Player player) {
        player.sendMessage(Messages.PREFIX + "Es läuft derzeit eine Umfrage");
        player.sendMessage("§7Frage: §c" + frage);
        for (int i1 = 0; i1 < antwortmoglichkeiten.size(); i1++) {
            String i = antwortmoglichkeiten.get(i1);
            TextComponent a = Component.text(i).color(NamedTextColor.BLUE).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/umfrage vote " + i1));
            player.sendMessage(a);
            if (i1 == antwortmoglichkeiten.size() - 1) {
                if (teilnehmer.contains(player)) {
                    player.sendMessage("§c(Keine Sorge, du hast schon abgestimmt)");
                } else {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                }
            }
        }
    }
}
