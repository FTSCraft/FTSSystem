/*
 * Copyright (c) 2021.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.menus.fts;

import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.main.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class FTSMenuInventory {

    private final Player player;
    private final User user;

    private final FtsSystem plugin;

    private Inventory inv;

    public FTSMenuInventory(Player p, FtsSystem plugin) {
        this.player = p;
        this.plugin = plugin;

        this.user = plugin.getUser(p);
        init();
    }

    private Inventory init() {

        Inventory inv = Bukkit.createInventory(null, 9 * 5, Component.text(player.getName() + ": Dein Menü").color(NamedTextColor.GOLD));

        this.inv = inv;

        load();

        return inv;

    }

    private void load() {
        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.displayName(Component.text(" "));
        filler.setItemMeta(fillerMeta);

        if (user.hasNoobProtection()) {
            ItemStack noobschutz = new ItemStack(Material.WOODEN_SWORD, 1);
            ItemMeta noobschutzMeta = noobschutz.getItemMeta();
            noobschutzMeta.displayName(Component.text("Noobschutz").color(NamedTextColor.RED));
            noobschutzMeta.setLore(Arrays.asList("§cDerzeit: §2An", "§7Klicke, um ihn auszuschalten. Danach kannst du ihn aber nicht wieder aktivieren!"));
            noobschutzMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            noobschutz.setItemMeta(noobschutzMeta);
            inv.setItem(22, noobschutz);
        }

        if (user.isMsgSoundEnabled()) {
            inv.setItem(10, plugin.getMenuItems().getMessageSoundOn());
        } else inv.setItem(10, plugin.getMenuItems().getMessageSoundOff());

        final int oocChannelSlot = 28;
        switch (user.getOocChannelStatus()) {
            case ON -> inv.setItem(oocChannelSlot, plugin.getMenuItems().getOocChannelOn());
            case RP -> inv.setItem(oocChannelSlot, plugin.getMenuItems().getOocChannelRP());
            case OFF -> inv.setItem(oocChannelSlot, plugin.getMenuItems().getOocChannelOff());
        }

        final int factionChannelSlot = 29;
        switch (user.getFactionChannelStatus()) {
            case ON -> inv.setItem(factionChannelSlot, plugin.getMenuItems().getFactionChannelOn());
            case RP -> inv.setItem(factionChannelSlot, plugin.getMenuItems().getFactionChannelRP());
            case OFF -> inv.setItem(factionChannelSlot, plugin.getMenuItems().getFactionChannelOff());
        }

        final int globalChannelSlot = 30;
        switch (user.getGlobalChannelStatus()) {
            case ON -> inv.setItem(globalChannelSlot, plugin.getMenuItems().getGlobalChannelOn());
            case RP -> inv.setItem(globalChannelSlot, plugin.getMenuItems().getGlobalChannelRP());
            case OFF -> inv.setItem(globalChannelSlot, plugin.getMenuItems().getGlobalChannelOff());
        }

        final int scoreboardSlot = 11;
        if (user.isScoreboardEnabled())
            inv.setItem(scoreboardSlot, plugin.getMenuItems().getScoreboardOn());
        else inv.setItem(scoreboardSlot, plugin.getMenuItems().getScoreboardOff());

        final int roleplayModeSlot = 12;
        if (plugin.getScoreboardManager().isInRoleplayMode(player)) {
            inv.setItem(roleplayModeSlot, plugin.getMenuItems().getRoleplayModeOn());
        } else inv.setItem(roleplayModeSlot, plugin.getMenuItems().getRoleplayModeOff());

        for (int i = 0; i < 9 * 5; i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, filler.clone());
            }
        }
    }

    public void refresh() {
        load();
    }

    public Inventory getInventory() {
        return init();
    }
}
