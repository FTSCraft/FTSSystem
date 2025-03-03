package de.ftscraft.ftssystem.menus.fts;

import de.ftscraft.ftssystem.main.FtsSystem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

public class ShopMenu {

    private static Inventory inventory = null;
    public static final Component invName = Component.text("Shop").color(NamedTextColor.RED);

    public static final int PRICE_ARMOR_STAND_EDIT = 100;
    private static final PlayerPointsAPI playerPoints = PlayerPoints.getInstance().getAPI();

    public static void handleArmorStandBuy(HumanEntity humanEntity) {
        if (playerPoints.look(humanEntity.getUniqueId()) >= PRICE_ARMOR_STAND_EDIT) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    "lp user " + humanEntity.getName() + " permission settemp asedit.* true 4h");
            playerPoints.take(humanEntity.getUniqueId(), PRICE_ARMOR_STAND_EDIT);
            humanEntity.sendMessage(Component.text("Du hast jetzt für 4 Stunden die Rechte Armor-Stands zu bearbeiten!").color(NamedTextColor.RED));
        } else {
            humanEntity.sendMessage(Component.text("Dafür hast du nicht genug Spielerpunkte!").color(NamedTextColor.RED));
        }
    }

    public static Inventory getInventory() {
        if (inventory != null)
            return inventory;

        inventory = Bukkit.createInventory(null, 9 * 6, invName);

        for (int i = 0; i < 9 * 6; i++) {
            inventory.setItem(i, FtsSystem.Instance().getMenuItems().getFiller());
        }
        inventory.setItem(10, FtsSystem.Instance().getMenuItems().getArmorStand());

        return inventory;
    }

}
