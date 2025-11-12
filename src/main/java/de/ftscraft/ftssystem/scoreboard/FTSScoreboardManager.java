/*
 * Copyright (c) 2020.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.scoreboard;

import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.utils.hooks.EngineHook;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static de.ftscraft.ftsengine.utils.Ausweis.Gender;

public class FTSScoreboardManager {

    private final List<Player> playerInRpMode = new ArrayList<>();

    private final List<Player> blinkingPlayers = new ArrayList<>();

    private final List<Player> sneakingPlayers = new ArrayList<>();

    private final List<Player> afkPlayers = new ArrayList<>();

    private final FtsSystem plugin;

    public FTSScoreboardManager(FtsSystem plugin) {
        this.plugin = plugin;
    }

    public void sendToAllScoreboard() {

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            sendScoreboardToPlayer(onlinePlayer, true);
        }

    }

    public void sendScoreboardToPlayer(Player p, boolean all) {

        ScoreboardManager sm = Bukkit.getScoreboardManager();

        setPlayerPrefix(p);

        Scoreboard s = sm.getNewScoreboard();

        Thread thread = new Thread(() -> {

            Team rpTeam = s.registerNewTeam("100rpmode");
            if (!sneakingPlayers.contains(p))
                rpTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);

            if (plugin.getUser(p).isScoreboardEnabled()) {

                Objective objective;
                if (s.getObjective(p.getName()) == null)
                    objective = s.registerNewObjective(p.getName(), "dummy");
                else {
                    objective = s.getObjective(p.getName());
                    objective.unregister();
                    objective = s.registerNewObjective(p.getName(), "dummy");
                }
                objective.setDisplayName("§lGesundheit");
                int kh = Integer.parseInt(PlaceholderAPI.setPlaceholders(p, "%survival_kh%"));
                int proteine = Integer.parseInt(PlaceholderAPI.setPlaceholders(p, "%survival_p%"));
                int durst = Integer.parseInt(PlaceholderAPI.setPlaceholders(p, "%survival_d%"));
                int vitamine = Integer.parseInt(PlaceholderAPI.setPlaceholders(p, "%survival_v%"));
                if (kh <= 5 || proteine <= 5 || durst <= 5 || vitamine <= 5) {
                    if (blinkingPlayers.contains(p)) {
                        blinkingPlayers.remove(p);
                        objective.setDisplayName("§4§lGesundheit");
                    } else blinkingPlayers.add(p);
                } else {
                    blinkingPlayers.remove(p);
                }
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                Score s1 = objective.getScore("§6Kohlenhydrate: §c" + kh);
                Score s2 = objective.getScore("§6Durst: §c" + durst);
                Score s3 = objective.getScore("§6Proteine: §c" + proteine);
                Score s4 = objective.getScore("§6Vitamine: §c" + vitamine);
                Score s6 = objective.getScore(PlaceholderAPI.setPlaceholders(p, "§6Krankheit: §c%pest_disease%"));
                Score s7 = objective.getScore(PlaceholderAPI.setPlaceholders(p, "§6Fortschritt: §c%pest_level%"));
                s6.setScore(3);
                s7.setScore(2);

                Score s8 = objective.getScore("§4--------");

                Score s9 = objective.getScore("§6Taler: §c %.2f".formatted(plugin.getEcon().getBalance(p)));
                s9.setScore(1);
                s8.setScore(4);
                s4.setScore(5);
                s3.setScore(6);
                s2.setScore(7);
                s1.setScore(8);
            }

            ArrayList<Player> isRanked = new ArrayList<>();

            for (Player player : playerInRpMode) {
                if (player != null) {
                    rpTeam.addEntry(player.getName());
                    isRanked.add(player);
                }
            }

            if (all) {
                for (TeamPrefixs value : TeamPrefixs.values()) {
                    Team team = s.registerNewTeam(value.getTeamName());
                    Team teamf = s.registerNewTeam(value.getTeamName() + "F");

                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (isRanked.contains(onlinePlayer))
                            continue;
                        if (onlinePlayer.hasPermission(value.getPermission())) {
                            Ausweis ausweis = EngineHook.getEngine().getAusweis(onlinePlayer);
                            if (ausweis == null || ausweis.getGender() == null || ausweis.getGender() == Ausweis.Gender.MALE || ausweis.getGender() == Ausweis.Gender.DIVERS)
                                team.addEntry(onlinePlayer.getName());
                            else if (ausweis.getGender() == Ausweis.Gender.FEMALE)
                                teamf.addEntry(onlinePlayer.getName());
                            isRanked.add(onlinePlayer);
                        }
                    }

                }
            }


            p.setScoreboard(s);

        });

        thread.start();

    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void switchToRoleplayMode(Player p) {
        if (!playerInRpMode.contains(p)) {
            playerInRpMode.add(p);
            p.sendMessage("§7Du bist nun im Rollenspiel-Modus!");
        } else {
            playerInRpMode.remove(p);
            p.sendMessage("§7Du bist nun nicht mehr im Rollenspiel-Modus!");
        }

    }

    public void changeAfkStatus(Player p, boolean afk) {

        if (afk) {
            if (!afkPlayers.contains(p))
                afkPlayers.add(p);
        } else {
            afkPlayers.remove(p);
        }

        setPlayerPrefix(p);

    }

    public void setPlayerPrefix(Player p) {
        Ausweis ausweis = EngineHook.getEngine().getAusweis(p);
        Gender gender;
        if (ausweis != null)
            gender = ausweis.getGender();
        else gender = Gender.MALE;

        TeamPrefixs playerRank = TeamPrefixs.NEULING;
        for (TeamPrefixs a : TeamPrefixs.values()) {
            if (p.hasPermission(a.getPermission())) {
                playerRank = a;
                break;
            }
        }

        String displayName = playerRank.getPrefix(gender) + " §7| §r" + p.getName();
        if (playerInRpMode.contains(p)) {
            displayName += " §7[RP]";
        }
        p.setPlayerListName(displayName);

        if (afkPlayers.contains(p)) {
            p.setPlayerListName(p.getPlayerListName().replaceAll("§.", "§7§m"));
        }
    }

    public boolean isInRoleplayMode(Player p) {

        return playerInRpMode.contains(p);

    }

    public void toggleSneakingMode(Player p) {
        if (!sneakingPlayers.contains(p))
            sneakingPlayers.add(p);
        else sneakingPlayers.remove(p);

        sendScoreboardToPlayer(p, false);
    }


}
