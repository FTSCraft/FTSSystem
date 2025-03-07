package de.ftscraft.ftssystem.listeners;

import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftsutils.items.ItemReader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.inventory.ItemStack;

public class CrafterListener implements Listener {

    public CrafterListener(FtsSystem plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onAutoCraft(CrafterCraftEvent event) {
        ItemStack result = event.getResult();
        if (ItemReader.getSign(result) != null) {
            event.setCancelled(true);
        }
    }


}
