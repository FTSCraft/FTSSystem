package de.ftscraft.ftssystem.commands;

import de.ftscraft.ftssystem.configs.Messages;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftsutils.misc.MiniMsg;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class CMDftssystem implements CommandExecutor {

    private final FtsSystem plugin;

    public CMDftssystem(FtsSystem plugin) {
        this.plugin = plugin;
        plugin.getCommand("ftssystem").setExecutor(this);
    }

    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args) {

        Player p = (Player) cs;

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("fake")) {
                if (p.hasPermission("ftssystem.fake")) {
                    if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("join")) {
                            for (Player a : Bukkit.getOnlinePlayers()) {
                                a.sendMessage("§b" + p.getName() + " §rhat Eldoria betreten!");
                            }
                        } else if (args[1].equalsIgnoreCase("leave")) {
                            for (Player a : Bukkit.getOnlinePlayers()) {
                                a.sendMessage("§b" + p.getName() + " §rhat Eldoria verlassen!");
                            }
                        }
                    } else p.sendMessage(Messages.HELP_FTSSYSTEM);
                }
            } else if (args[0].equalsIgnoreCase("playtime")) {

                if (p.hasPermission("ftssystem.admin")) {

                    p.sendMessage("§cFolgende online Spieler haben bereits über 50h Spielzeit aber noch nicht den Bürgerrang: ");

                    boolean someone = false;

                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (onlinePlayer.hasPermission("group.burger")) {
                            continue;
                        }

                        double ticks = onlinePlayer.getStatistic(Statistic.PLAY_ONE_MINUTE);
                        double seconds = ticks / 20;
                        double minutes = seconds / 60;
                        double hours = minutes / 60;


                        if (hours > 50.0) {
                            someone = true;
                            p.sendMessage("§e- " + onlinePlayer.getName() + " §c(" + hours + "h)");
                        }
                    }

                    if (!someone)
                        p.sendMessage("§eNiemand");

                } else {
                    double ticks = p.getStatistic(Statistic.PLAY_ONE_MINUTE);
                    double seconds = ticks / 20;
                    double minutes = seconds / 60;
                    double hours = minutes / 60;

                    hours = Math.round(hours * 100d) / 100d;

                    p.sendMessage("§cDu hast " + hours + " Stunden Spielzeit");
                }


            } else if(args[0].equalsIgnoreCase("reload")) {

                if(!p.hasPermission("ftssystem.reload")) {
                    p.sendMessage(Messages.NO_PERM);
                    return true;
                }

                plugin.onDisable();
                plugin.onEnable();

                MiniMsg.msg(p, Messages.MINI_PREFIX + "Das Plugin wurde erfolgreich neugeladen.");

            }
        } else p.sendMessage(Messages.HELP_FTSSYSTEM);
        return false;
    }

}
