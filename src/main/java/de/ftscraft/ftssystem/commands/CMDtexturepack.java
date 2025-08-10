package de.ftscraft.ftssystem.commands;

import de.ftscraft.ftssystem.configs.Messages;
import de.ftscraft.ftssystem.features.ResourcePackManager;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftsutils.misc.MiniMsg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMDtexturepack implements CommandExecutor {

    public CMDtexturepack(FtsSystem plugin) {
        //noinspection DataFlowIssue
        plugin.getCommand("texturepack").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) {
            return true;
        }

        if (args.length == 0) {
            MiniMsg.msg(player, "<red>Bitte drücke bei dem Bildschirm auf ja.</red>");
            ResourcePackManager.sendResourcePack(player);
        }

        if (args.length == 3) {
            if (args[0].equals("set")) {
                if (!player.hasPermission("ftssystem.settexturepack")) {
                    MiniMsg.msg(player, "<red>Dafür hast du keine Rechte</red>");
                    return true;
                }
                String link = args[1];
                String hash = args[2];
                ResourcePackManager.setResourcePack(link, hash);
                ResourcePackManager.saveResourcePackInfo();
                MiniMsg.msg(player, Messages.MINI_PREFIX + "Du hast das neue Resource Pack gesetzt.");
            }
        }

        return false;
    }

}
