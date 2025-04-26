package de.ftscraft.ftssystem.commands;

import de.ftscraft.ftssystem.configs.Messages;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftsutils.misc.MiniMsg;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.jetbrains.annotations.NotNull;

public class AnimalOwnershipCMD implements CommandExecutor {

    public AnimalOwnershipCMD(FtsSystem plugin) {
        //noinspection DataFlowIssue
        plugin.getCommand("animalownership").setExecutor(this);
    }

    final private String HELP = "%sBenutze den Command so: /animalownership <red>Spieler</red> und schaue dabei auf ein Tier welches du besitzt.".formatted(Messages.MINI_PREFIX);
    final private String DONT_OWN = "%sDu besitzt dieses Tier nicht".formatted(Messages.MINI_PREFIX);
    final private String NO_ENTITY = "%sDu schaust auf kein Tier (oder bist nicht nah genug)".formatted(Messages.MINI_PREFIX);
    final private String PLAYER_NOT_FOUND = "%sDer Spieler wurde nicht gefunden!".formatted(Messages.MINI_PREFIX);
    final private String SUCCESS = "%sDu hast das Eigentumsrecht erfolgreich übertragen".formatted(Messages.MINI_PREFIX);

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(cs instanceof Player p)) {
            return false;
        }

        if(args.length != 1) {
            MiniMsg.msg(p, HELP);
            return true;
        }

        Entity targetEntity = p.getTargetEntity(8);
        if (targetEntity == null) {
            MiniMsg.msg(p, NO_ENTITY);
            return true;
        }

        if (!(targetEntity instanceof Tameable tameable)) {
            MiniMsg.msg(p, DONT_OWN);
            return true;
        }

        if (tameable.getOwner() != p) {
            MiniMsg.msg(p, DONT_OWN);
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if (!target.hasPlayedBefore()) {
            MiniMsg.msg(p, PLAYER_NOT_FOUND);
            return true;
        }

        tameable.setOwner(target);
        MiniMsg.msg(p, SUCCESS);

        return true;
    }

}
