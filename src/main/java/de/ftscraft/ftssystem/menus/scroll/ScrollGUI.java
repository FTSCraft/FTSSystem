package de.ftscraft.ftssystem.menus.scroll;

import de.ftscraft.ftssystem.utils.hooks.EssentialsHook;
import de.ftscraft.ftssystem.utils.hooks.HookManager;
import de.ftscraft.ftsutils.items.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;
import java.util.LinkedList;

public class ScrollGUI {

    private final Inventory inventory;

    public static ScrollGUI scrollGUI = null;

    private ScrollGUI() {
        inventory = Bukkit.createInventory(null, 9, Component.text("Schriftrolle").color(NamedTextColor.DARK_BLUE));

        Collection<String> warps;
        if (!HookManager.ESSENTIALS_ENABLED)
            warps = new LinkedList<>();
        else warps = EssentialsHook.getWarps();

        for (String s : warps) {
            if (s.startsWith("SCROLL_POINT")) {
                inventory.addItem(new ItemBuilder(Material.PAPER)
                        .name(s.substring(12))
                        .addPDC("SCROLL", s, PersistentDataType.STRING)
                        .lore("§7Nutze deine Schriftrolle und teleportiere dich hierhin")
                        .build());
            }
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public static void init() {
        if (scrollGUI == null)
            scrollGUI = new ScrollGUI();
    }

}
