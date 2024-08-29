package de.ftscraft.ftssystem.listeners;

import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.utils.Utils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;

import java.util.HashMap;
import java.util.Map;

public class EnchantListener implements Listener {

    public EnchantListener(FtsSystem plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEnchantPrepare(PrepareItemEnchantEvent event) {
        for (EnchantmentOffer offer : event.getOffers()) {
            if (offer == null)
                continue;
            Enchantment enchantment = offer.getEnchantment();

            var replacement = Utils.enchantmentReplacements.get(new Utils.EnchantmentWithLevel(enchantment, offer.getEnchantmentLevel()));

            if (replacement != null) {
                offer.setEnchantment(replacement.enchantment());
                offer.setEnchantmentLevel(replacement.level());
            }
        }
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        Map<Enchantment, Integer> enchantsToAdd = event.getEnchantsToAdd();
        HashMap<Enchantment, Utils.EnchantmentWithLevel> replacements = new HashMap<>();
        enchantsToAdd.forEach((enchantment, lvl) -> {
            var replacement = Utils.enchantmentReplacements.get(new Utils.EnchantmentWithLevel(enchantment, lvl));
            if (replacement != null) {
                replacements.put(enchantment, replacement);
            }
        });
        replacements.forEach(((enchantment, replacement) -> {
            enchantsToAdd.remove(enchantment);
            enchantsToAdd.put(replacement.enchantment(), replacement.level());
        }));
    }

}
