package de.ftscraft.ftssystem.commands;

import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.scoreboard.TeamPrefixs;
import de.ftscraft.ftssystem.utils.hooks.EngineHook;
import de.ftscraft.ftssystem.utils.hooks.ForumHook;
import kong.unirest.UnirestException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CMDcheckcv implements CommandExecutor {

    private final FtsSystem plugin;
    private final ArrayList<OfflinePlayer> playersThatUsedCommand = new ArrayList<>();

    public CMDcheckcv(FtsSystem plugin) {
        this.plugin = plugin;
        plugin.getCommand("checkcv").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!(cs instanceof Player p)) {
            return true;
        }

        if (playersThatUsedCommand.contains(p)) {
            p.sendMessage(Messages.PREFIX + "Du hast diesen Command erst neulich benutzt und es hat nicht geklappt. Bitte behebe den Fehler den wir dir vorhin angezeigt haben" +
                          " und führe den Command nach dem nächsten Restart nochmal aus.");
            return true;
        }

        if (!TeamPrefixs.getPrefix(p, Ausweis.Gender.MALE).equalsIgnoreCase("§6Reisender")) {
            p.sendMessage(Messages.PREFIX + "Sieht so aus als wärst du schon kein Reisender mehr.");
            return true;
        }

        Ausweis ausweis = EngineHook.getEngine().getAusweis(p);
        if (ausweis == null) {
            p.sendMessage(Messages.PREFIX + "Du hast keinen Ausweis.");
            return true;
        }

        if (ausweis.getFirstName() == null || ausweis.getLastName() == null || ausweis.getDesc() == null || ausweis.getRace() == null) {
            p.sendMessage(Messages.PREFIX + "Dein Ausweis ist nicht komplett! Bitte fülle deinen Ausweis richtig aus.");
            return true;
        }

        if (ausweis.getForumLink() == null) {
            p.sendMessage(Messages.PREFIX + "Bitte gebe einen Link zu deiner CV an.");
            return true;
        }

        try {

            ForumHook.CheckCVResponse response = plugin.getForumHook().isAcceptedCV(p.getName(), ausweis.getForumLink());

            if (response == ForumHook.CheckCVResponse.WRONG_URL) {
                p.sendMessage(Messages.PREFIX + "Da lief irgendwas falsch. Überprüfe am Besten mal den Link oder melde dich bei einem Teamler");
                return true;
            }
            if (response == ForumHook.CheckCVResponse.NOT_FROM_PLAYER) {
                p.sendMessage(Messages.PREFIX + "Es sieht so aus als wäre das nicht deine CV. Falls du denkst dass das Falsch ist melde dich gerne bei einem Teamler");
                playersThatUsedCommand.add(p);
                return true;
            }
            if (response == ForumHook.CheckCVResponse.NOT_ACCEPTED) {
                p.sendMessage(Messages.PREFIX + "Es sieht so aus als wäre die CV noch nicht final akzeptiert. Falls doch, lass dir am besten von einem Teamler den Rang geben");
                playersThatUsedCommand.add(p);
                return true;
            }
            if (response == ForumHook.CheckCVResponse.IS_ACCEPTED) {
                p.sendMessage(Messages.PREFIX + "Sieht gut aus! Du solltest jetzt den Rang haben.");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user %s promote burger".replace("%s", p.getName()));
                return true;
            }

        } catch (UnirestException e) {
            p.sendMessage(Messages.PREFIX + "Da lief irgendwas falsch. Überprüfe am Besten mal den Link oder melde dich bei einem Teamler");
        }


        return false;
    }

}


