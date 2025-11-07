/*
 * Copyright (c) 2021.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.menus.fts;

import de.ftscraft.ftsutils.items.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class MenuItems {

    private ItemStack messageSoundOn, messageSoundOff;
    private ItemStack scoreboardOn, scoreboardOff;
    private ItemStack oocChannelOff, oocChannelOn, oocChannelRP;
    private ItemStack globalChannelOff, globalChannelOn, globalChannelRP;
    private ItemStack factionChannelOff, factionChannelOn, factionChannelRP;
    private ItemStack roleplayModeOn, roleplayModeOff;

    private ItemStack armorStand;
    private ItemStack ownHead;
    private ItemStack itemFrameInvis, itemFrameUnbreakable, itemFrameInvisUnbreakable;
    private ItemStack debugStick, debugStickPermission;

    private ItemStack filler;

    private ItemStack shopMenu;

    public MenuItems() {
        init();
    }

    private void initMainMenuItems() {

        shopMenu = new ItemBuilder(Material.GOLD_INGOT)
                .name(Component.text("Shop").color(NamedTextColor.DARK_PURPLE).decorate(TextDecoration.BOLD))
                .lore("§7Öffne den Shop", "§9")
                .build();

        messageSoundOn = new ItemBuilder(Material.PAPER)
                .name("§5MSG-Sound: " + ChatColor.GREEN + "An")
                .shiny()
                .addFlags(ItemFlag.HIDE_ENCHANTS)
                .lore("§7Du bekommst derzeit einen Sound wenn du eine MSG erhälst", "§7Klicke, um dies zu ändern!", "§1")
                .build();
        messageSoundOff = new ItemBuilder(Material.PAPER)
                .name("§5MSG-Sound: " + ChatColor.RED + "Aus")
                .lore("§7Derzeit bekommst du keinen Sound wenn du eine MSG erhälst", "§7Klicke, um dies zu ändern!", "§1")
                .build();

        scoreboardOn = new ItemBuilder(Material.OAK_SIGN)
                .shiny()
                .addFlags(ItemFlag.HIDE_ENCHANTS)
                .lore("§7Bitte beachte dass es bis zu 5 Sekunden dauern kann", "§7bis die Einstellung übernommen wird!", "§3")
                .name("§5Scoreboard: " + ChatColor.GREEN + "An")
                .build();

        scoreboardOff = new ItemBuilder(Material.OAK_SIGN)
                .lore("§7Bitte beachte dass es bis zu 5 Sekunden dauern kann", "§7bis die Einstellung übernommen wird!", "§3")
                .name("§5Scoreboard: " + ChatColor.RED + "Aus")
                .build();

        oocChannelOn = new ItemBuilder(Material.PLAYER_HEAD)
                .skullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZThmNDc5MjM4NjFjMjc1MzExYWI2OWIwM2UyNzUxMjI0ZmYzOTRlNDRlM2IzYzhkYjUyMjVmZiJ9fX0")
                .lore("§7Wann möchtest du den OOC Channel sehen?", "§5")
                .name("§3OOC-Channel: " + ChatColor.GREEN + "Immer an")
                .build();

        oocChannelOff = new ItemBuilder(Material.PLAYER_HEAD)
                .skullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmFlY2VkZjViOGIwYWMzZjU2MTkyZmNjYjBhZGU0OGRjNWEwNDNlN2UwZWVhMmJjYzVmNTRhZDAyODFmNjdjOSJ9fX0")
                .lore("§7Wann möchtest du den OOC Channel sehen?", "§5")
                .name("§3OOC-Channel: " + ChatColor.DARK_RED + "Immer aus")
                .build();

        oocChannelRP = new ItemBuilder(Material.PLAYER_HEAD)
                .skullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjNmNDZjY2FlMmJiMTI0M2M4M2Q2Njc2YjI0OGMyYjFkNDM0MmVlYTM3OTMxNWNhZWMwMjFiN2QxZDM1NTcifX19")
                .lore("§7Wann möchtest du den OOC Channel sehen?", "§5")
                .name("§3OOC-Channel: " + ChatColor.BLUE + "Nur im RP aus")
                .build();

        factionChannelOn = new ItemBuilder(Material.PLAYER_HEAD)
                .skullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2M0YzM5OWI1ZTc1MjJhMTdkMzVlYjUzZmU1ZWI3NjdhM2IzOGVhNmU1Y2QzM2I0NDM4MDIwZmM1YTg0OGEifX19")
                .lore("§7Wann möchtest du den Faction Channel sehen?", "§6")
                .name("§3Faction-Channel: " + ChatColor.GREEN + "Immer an")
                .build();

        factionChannelOff = new ItemBuilder(Material.PLAYER_HEAD)
                .skullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODhiYmMzNTIxM2M2NTEwOWZiZDhlOTA3NzFlNGM2ZGI3ODVhOWJlOGU0MDNkMjllZDhlZDJlM2RjZmIxMjgifX19")
                .lore("§7Wann möchtest du den Faction Channel sehen?", "§6")
                .name("§3Faction-Channel: " + ChatColor.DARK_RED + "Immer aus")
                .build();

        factionChannelRP = new ItemBuilder(Material.PLAYER_HEAD)
                .skullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Y5ZmQyMWEyZTJmNTcyZTllNWJmMGI1NTJmMDljNDE2YzQ4NzZjYmVhMGQ0YWMzZWMxZTY5ZGVlYzU5YSJ9fX0")
                .lore("§7Wann möchtest du den Faction Channel sehen?", "§6")
                .name("§3Faction-Channel: " + ChatColor.BLUE + "Nur im RP aus")
                .build();

        globalChannelOn = new ItemBuilder(Material.PLAYER_HEAD)
                .skullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdjYThiOWM3MTY5YTc0M2Y4MmU4ZTlmZDY4ZjExYzBmMzEwNzBhNTNjOTI4NjM4ODRlNjczMTM3ZmJhZjEifX19")
                .lore("§7Wann möchtest du den Global Channel sehen?", "§7")
                .name("§3Global-Channel: " + ChatColor.GREEN + "Immer an")
                .build();

        globalChannelOff = new ItemBuilder(Material.PLAYER_HEAD)
                .skullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTg1MzYxMzc3MjE1ZTIxNGEzNWM5NmNiYWY5ZGI2NzIxMDVhZmFmYzk0OTRiYmY0YWU3MmFhNjczYzVmZTdjIn19fQ")
                .lore("§7Wann möchtest du den Global Channel sehen?", "§7")
                .name("§3Global-Channel: " + ChatColor.DARK_RED + "Immer aus")
                .build();

        globalChannelRP = new ItemBuilder(Material.PLAYER_HEAD)
                .skullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmM3NmI0ZTlmZDg3Yjc3YjhjOTVhOGQxODQ4NDE2OGNiZWI3OGZhYjVkNTI5ZDZiNmMxMzIyNDZjYzNjYmFjIn19fQ")
                .lore("§7Wann möchtest du den Global Channel sehen?", "§7")
                .name("§3Global-Channel: " + ChatColor.BLUE + "Nur im RP aus")
                .build();

        roleplayModeOn = new ItemBuilder(Material.FEATHER)
                .lore("§7Schalte den Rollenspielmodus um", "§7Der RP-Modus schaltet nur gewisse Funktionen ein oder aus. Er symbolisiert §cnicht §7ob du gerade im RP bist, weil du §cimmer im RP bist§7.", "§8")
                .shiny()
                .addFlags(ItemFlag.HIDE_ENCHANTS)
                .name("§3RP-Modus: " + ChatColor.GREEN + "Ein")
                .build();

        roleplayModeOff = new ItemBuilder(Material.FEATHER)
                .lore("§7Schalte den Rollenspielmodus um", "§8")
                .name("§3RP-Modus: §4Aus")
                .build();
    }

    private void initShopItems() {
        armorStand = new ItemBuilder(Material.ARMOR_STAND)
                .name(Component.text("Rechte für Armor-Stand bearbeiten").color(NamedTextColor.RED))
                .lore("§7Du bezahlst " + ShopMenu.PRICE_ARMOR_STAND_EDIT + " PP für 4 Stunden Rechte")
                .sign("ASEDIT-SHOP")
                .addFlags(ItemFlag.HIDE_ENCHANTS)
                .build();

        ownHead = new ItemBuilder(Material.PLAYER_HEAD)
                .name(Component.text("Dein eigener Kopf").color(NamedTextColor.RED))
                .lore("§7Du bezahlst " + ShopMenu.PRICE_OWN_HEAD + " PP")
                .sign("OWN_HEAD-SHOP")
                .build();

        itemFrameInvis = new ItemBuilder(Material.ITEM_FRAME)
                .name(Component.text("Unsichtbarer Itemframe").color(NamedTextColor.RED))
                .lore("§7Du bezahlst %d für einen unsichtbaren, zerstörbaren Rahmen".formatted(ShopMenu.PRICE_FRAME_INVIS))
                .sign("INVIS_FRAME-SHOP")
                .build();

        itemFrameUnbreakable = new ItemBuilder(Material.ITEM_FRAME)
                .name(Component.text("Unzerstörbarer Itemframe").color(NamedTextColor.RED))
                .lore("§7Du bezahlst %d für einen sichtbaren, unzerstörbaren Rahmen".formatted(ShopMenu.PRICE_FRAME_UNBREAKABLE))
                .sign("UNBREAKABLE_FRAME-SHOP")
                .build();

        itemFrameInvisUnbreakable = new ItemBuilder(Material.ITEM_FRAME)
                .name(Component.text("Unsichtbarer und unzerstörbarer Itemframe").color(NamedTextColor.RED))
                .lore("§7Du bezahlst %d für einen unsichtbaren, unzerstörbaren Rahmen".formatted(ShopMenu.PRICE_FRAME_BOTH))
                .sign("UNBREAKABLE_INVIS_FRAME-SHOP")
                .build();

        debugStick = new ItemBuilder(Material.STICK)
                .name(Component.text("Debug Stick erhalten").color(NamedTextColor.LIGHT_PURPLE))
                .lore("§7Du bezahlst %d für einen Debug Stick. Die Permission ihn zu nutzen musst du extra kaufen".formatted(ShopMenu.PRICE_DEBUG_STICK))
                .sign("DEBUG_STICK-SHOP")
                .build();

        debugStickPermission = new ItemBuilder(Material.STICK)
                .name(Component.text("Debug Stick Rechte").color(NamedTextColor.LIGHT_PURPLE))
                .lore("§7Du bezahlst %d um einen Debug Stick für 2 Stunden nutzen zu können. Den Stick musst du extra kaufen".formatted(ShopMenu.PRICE_DEBUG_STICK_PERMISSION))
                .sign("DEBUG_STICK_PERM-SHOP")
                .build();


    }

    private void init() {
        initMainMenuItems();
        initShopItems();
        filler = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(" ").build();
    }

    public ItemStack getMessageSoundOff() {
        return messageSoundOff;
    }

    public ItemStack getMessageSoundOn() {
        return messageSoundOn;
    }

    public ItemStack getScoreboardOn() {
        return scoreboardOn;
    }

    public ItemStack getScoreboardOff() {
        return scoreboardOff;
    }

    public ItemStack getOocChannelOff() {
        return oocChannelOff;
    }

    public ItemStack getOocChannelOn() {
        return oocChannelOn;
    }

    public ItemStack getOocChannelRP() {
        return oocChannelRP;
    }

    public ItemStack getGlobalChannelOff() {
        return globalChannelOff;
    }

    public ItemStack getGlobalChannelOn() {
        return globalChannelOn;
    }

    public ItemStack getGlobalChannelRP() {
        return globalChannelRP;
    }

    public ItemStack getFactionChannelOff() {
        return factionChannelOff;
    }

    public ItemStack getFactionChannelOn() {
        return factionChannelOn;
    }

    public ItemStack getFactionChannelRP() {
        return factionChannelRP;
    }

    public ItemStack getRoleplayModeOn() {
        return roleplayModeOn;
    }

    public ItemStack getRoleplayModeOff() {
        return roleplayModeOff;
    }

    public ItemStack getArmorStand() {
        return armorStand;
    }

    public ItemStack getShopMenu() {
        return shopMenu;
    }

    public ItemStack getFiller() {
        return filler;
    }

    public ItemStack getOwnHead() {
        return ownHead;
    }

    public ItemStack getItemFrameInvis() {
        return itemFrameInvis;
    }

    public ItemStack getItemFrameUnbreakable() {
        return itemFrameUnbreakable;
    }

    public ItemStack getItemFrameInvisUnbreakable() {
        return itemFrameInvisUnbreakable;
    }

    public ItemStack getDebugStick() {
        return debugStick;
    }

    public ItemStack getDebugStickPermission() {
        return debugStickPermission;
    }
}
