package de.ftscraft.ftssystem.commands;

import de.ftscraft.ftssystem.configs.Messages;
import de.ftscraft.ftssystem.features.voterewards.VoteRewardManager;
import de.ftscraft.ftssystem.features.voterewards.storage.VoteRewardStorage;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftsutils.misc.MiniMsg;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VoteRewardCMD implements CommandExecutor {

    private final VoteRewardManager rewardManager;

    public VoteRewardCMD(FtsSystem instance, VoteRewardManager voteRewardManager) {
        instance.getCommand("votereward").setExecutor(this);
        this.rewardManager = voteRewardManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!cs.hasPermission("ftssystem.votereward")) {
            cs.sendMessage(MiniMsg.c("Du hast nicht die Rechte für diesen Befehl."));
            return true;
        }

        if (args.length != 2) {
            cs.sendMessage(MiniMsg.c("Falsche Syntax! /votereward <Reward> <Spieler>"));
            return true;
        }

        String rewardName = args[0];
        String tName = args[1];

        Player player = Bukkit.getPlayer(tName);
        VoteRewardStorage reward = rewardManager.getRewardByName(rewardName);

        if (player == null) {
            cs.sendMessage(MiniMsg.c("Der Spieler ist nicht online"));
            return true;
        }

        if (reward == null) {
            cs.sendMessage(MiniMsg.c("Der Reward ist nicht bekannt"));
            return true;
        }


        rewardManager.giveReward(player, reward);

        MiniMsg.msg(player, Messages.MINI_PREFIX + "Du hast deinen Reward erhalten. Danke für deine Unterstützung!");

        return false;
    }

}
