package de.ftscraft.ftssystem.commands;

import de.ftscraft.ftssystem.main.FtsSystem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CMDxpstore implements CommandExecutor {

    private final FtsSystem plugin;

    public CMDxpstore(FtsSystem plugin) {
        this.plugin = plugin;
        plugin.getCommand("xpstore").setExecutor(this);
    }

    private static final int BOTTLES_REQUIRED = 2, LAPIS_REQUIRED = 8, REDSTONE_REQUIRED = 8;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!(sender instanceof Player)) {


            if (args.length == 1) {

                String playerName = args[0];
                Player pl = Bukkit.getServer().getPlayer(playerName);

                if (pl != null && pl.isOnline()) {
                    int totalExp = pl.calculateTotalExperiencePoints();
                    if (totalExp >= 20 && pl.getInventory().containsAtLeast(new ItemStack(Material.GLASS_BOTTLE), 2) && pl.getInventory().containsAtLeast(new ItemStack(Material.REDSTONE), 8) && pl.getInventory().containsAtLeast(new ItemStack(Material.LAPIS_LAZULI), 8)) {

                        pl.setExperienceLevelAndProgress(totalExp - 20);

                        int bottleDone = 0;
                        int redstoneDone = 0;
                        int lapisDone = 0;

                        for (ItemStack item : pl.getInventory().getContents()) {
                            if (item == null) {
                                continue;
                            }
                            if (item.getType() == Material.GLASS_BOTTLE && bottleDone != BOTTLES_REQUIRED) {
                                int removeFromStack = Math.min(item.getAmount(), BOTTLES_REQUIRED - bottleDone);
                                item.setAmount(item.getAmount() - removeFromStack);
                                bottleDone += removeFromStack;
                            } else if (item.getType() == Material.REDSTONE && bottleDone != REDSTONE_REQUIRED) {
                                int removeFromStack = Math.min(item.getAmount(), REDSTONE_REQUIRED - redstoneDone);
                                item.setAmount(item.getAmount() - removeFromStack);
                                redstoneDone += removeFromStack;
                            } else if (item.getType() == Material.LAPIS_LAZULI && bottleDone != LAPIS_REQUIRED) {
                                int removeFromStack = Math.min(item.getAmount(), LAPIS_REQUIRED - lapisDone);
                                item.setAmount(item.getAmount() - removeFromStack);
                                lapisDone += removeFromStack;
                            }
                        }

                        ItemStack isExpBottle = new ItemStack(Material.EXPERIENCE_BOTTLE, 2);
                        pl.getInventory().addItem(isExpBottle);

                    }
                } else {
                    sender.sendMessage("Der Spieler '" + args[0] + "' konnte nicht gefunden werden!");
                }

                return false;

            } else {
                sender.sendMessage("Nutze den Befehl bitte wie folgt: /xpstore [Spieler]");
            }

        } else {
            sender.sendMessage("§4Dieser Command kann nur über die Serverkonsole ausgeführt werden!");
        }
        return false;
    }
}
