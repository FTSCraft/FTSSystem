package de.ftscraft.ftssystem.commands;

import de.ftscraft.ftssystem.configs.Messages;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.utils.FTSCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMDftssystem implements FTSCommand {

    private FtsSystem plugin;

    public CMDftssystem(FtsSystem plugin) {
        this.plugin = plugin;
        plugin.getCommand("ftssystem").setExecutor(this);
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        Player p = (Player) cs;

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("fake")) {
                if (p.hasPermission("ftssystem.fake")) {
                    if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("join")) {
                            for (Player a : Bukkit.getOnlinePlayers()) {
                                a.sendMessage("§6§l" + p.getName() + " §r§lhat das Kaiserreich betreten");
                            }
                        } else if (args[1].equalsIgnoreCase("leave")) {
                            for (Player a : Bukkit.getOnlinePlayers()) {
                                a.sendMessage("§6§l" + p.getName() + " §r§lhat das Kaiserreich verlassen");
                            }
                        }
                    } else p.sendMessage(Messages.HELP_FTSSYSTEM);
                }
            } else if (args[0].equalsIgnoreCase("test")) {
                p.kickPlayer(
                        "§4Du wurdest gebannt! \n" +
                                "§eGebannt von: §b" + "Dir" + "\n" +
                                "§eBis: §bPERMANENT \n" +
                                "§eGrund: §b" + "test" + "\n" +
                                " \n" +
                                "§6Du kannst ein Entbannungsbeitrag im Forum schreiben");
            } else p.sendMessage(Messages.HELP_FTSSYSTEM);
        } else p.sendMessage(Messages.HELP_FTSSYSTEM);

        return false;
    }

}
