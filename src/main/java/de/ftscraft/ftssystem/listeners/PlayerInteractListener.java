package de.ftscraft.ftssystem.listeners;

import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.menus.scroll.ScrollGUI;
import de.ftscraft.ftsutils.items.ItemReader;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    public PlayerInteractListener(FtsSystem plugin) {
        ScrollGUI.init();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        if (event.getItem() != null)
            if (event.getItem().getType() == Material.BONE_MEAL)
                if(!ItemReader.getSign(event.getItem()).equals("FTS_FERTILIZER"))
                    event.setCancelled(true);

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack itemInHand = event.getItem();
        if (itemInHand == null || itemInHand.getItemMeta() == null) return;
        String sign = ItemReader.getSign(itemInHand);
        if (sign == null) return;
        if (sign.equals("TP_SCROLL")) {

            event.getPlayer().openInventory(ScrollGUI.scrollGUI.getInventory());

        }

    }

}
