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

    private static Component NOT_ENOUGH_PP_MSG = Component.text("Dafür hast du nicht genug Spielerpunkte!").color(NamedTextColor.RED);

    public static final int PRICE_ARMOR_STAND_EDIT = 100,
            PRICE_OWN_HEAD = 50,
            PRICE_FRAME_INVIS = 25,
            PRICE_FRAME_UNBREAKABLE = 20,
            PRICE_FRAME_BOTH = 30;

    private static final PlayerPointsAPI playerPoints = PlayerPoints.getInstance().getAPI();

    public static void handleArmorStandBuy(HumanEntity humanEntity) {
        if (playerPoints.look(humanEntity.getUniqueId()) >= PRICE_ARMOR_STAND_EDIT) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    "lp user " + humanEntity.getName() + " permission settemp asedit.* true 4h");
            playerPoints.take(humanEntity.getUniqueId(), PRICE_ARMOR_STAND_EDIT);
            humanEntity.sendMessage(Component.text("Du hast jetzt für 4 Stunden die Rechte Armor-Stands zu bearbeiten!").color(NamedTextColor.RED));
        } else {
            humanEntity.sendMessage(NOT_ENOUGH_PP_MSG);
        }
    }

    public static void handleOwnHeadBuy(HumanEntity humanEntity) {
        if (playerPoints.look(humanEntity.getUniqueId()) >= PRICE_OWN_HEAD) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:give %s player_head[profile={name:\"%s\"}] 1".formatted(humanEntity.getName(), humanEntity.getName()));
            playerPoints.take(humanEntity.getUniqueId(), PRICE_OWN_HEAD);
            humanEntity.sendMessage(Component.text("Du hast deinen eigenen Kopf erhalten.").color(NamedTextColor.RED));
        } else {
            humanEntity.sendMessage(NOT_ENOUGH_PP_MSG);
        }
    }

    /**
     *
     * @param humanEntity
     * @param type        0: Invis, 1: Unbreakable, 2: Both
     */
    public static void handleItemFrameBuy(HumanEntity humanEntity, int type) {
        String cmd;
        int price;
        switch (type) {
            case 0:
                price = PRICE_FRAME_INVIS;
                cmd = "minecraft:give %s item_frame[entity_data={id:\"minecraft:item_frame\",Invisible:1b}] 1";
                break;
            case 1:
                price = PRICE_FRAME_UNBREAKABLE;
                cmd = "minecraft:give %s item_frame[entity_data={id:\"minecraft:item_frame\",Invulnerable:1b}] 1";
                break;
            case 2:
                price = PRICE_FRAME_BOTH;
                cmd = "minecraft:give %s item_frame[entity_data={id:\"minecraft:item_frame\",Invisible:1b,Invulnerable:1b}] 1";
                break;
            default:
                throw new IllegalArgumentException("only accept 0, 1, 2 as arguments for type");
        }
        if (playerPoints.look(humanEntity.getUniqueId()) >= price) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.formatted(humanEntity.getName()));
            playerPoints.take(humanEntity.getUniqueId(), price);
            humanEntity.sendMessage(Component.text("Du hast den Rahmen erhalten.").color(NamedTextColor.RED));
        } else {
            humanEntity.sendMessage(NOT_ENOUGH_PP_MSG);
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
        inventory.setItem(12, FtsSystem.Instance().getMenuItems().getOwnHead());
        inventory.setItem(14, FtsSystem.Instance().getMenuItems().getItemFrameInvis());
        inventory.setItem(15, FtsSystem.Instance().getMenuItems().getItemFrameUnbreakable());
        inventory.setItem(16, FtsSystem.Instance().getMenuItems().getItemFrameInvisUnbreakable());

        return inventory;
    }

}
