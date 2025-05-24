package de.ftscraft.ftssystem.commands;

import de.ftscraft.ftssystem.configs.Messages;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.utils.Utils;
import de.ftscraft.ftsutils.items.ItemReader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CMDrepair implements CommandExecutor {

    private final FtsSystem plugin;
    private final static int PRICE = 50;
    private final String PERMISSION;
    private final String USAGE;
    private final Map<String, Material> materialMap;
    private final Map<String, String> specialItemMap;

    public CMDrepair(FtsSystem plugin) {
        this.plugin = plugin;
        PluginCommand command = plugin.getCommand("repair");
        command.setExecutor(this);
        PERMISSION = command.getPermission();
        USAGE = command.getUsage();

        materialMap = new HashMap<>();
        specialItemMap = new HashMap<>();
        initMaterialMap();
        initSpecialItemMap();
    }

    private void initMaterialMap() {
        // Map material name prefixes to actual materials
        materialMap.put("DIAMOND", Material.DIAMOND);
        materialMap.put("IRON", Material.IRON_INGOT);
        materialMap.put("GOLD", Material.GOLD_INGOT);
        materialMap.put("STONE", Material.STONE);
        materialMap.put("NETHERITE", Material.NETHERITE_INGOT);
        materialMap.put("EMERALD", Material.EMERALD);
        materialMap.put("COPPER", Material.COPPER_INGOT);
    }

    private void initSpecialItemMap() {
        // Custom Items with Sign handling
        specialItemMap.put("EMERALDPICKAXE", "EMERALD");
        specialItemMap.put("SENSE", "EMERALD");
        specialItemMap.put("FALLAXT", "EMERALD");
        specialItemMap.put("KUPFERAXT", "COPPER");
        specialItemMap.put("KUPFERSPITZHACKE", "COPPER");
        specialItemMap.put("KUPFERHACKE", "COPPER");
        specialItemMap.put("KUPFERSCHAUFEL", "COPPER");
        specialItemMap.put("KUPFERSCHWERT", "COPPER");
        specialItemMap.put("LANZE", "IRON");
        specialItemMap.put("STREITKOLBEN", "NETHERITE");
        specialItemMap.put("TINY_BACKPACK", "GEHAERTETES_LEDER");
        specialItemMap.put("LARGE_BACKPACK", "GEHAERTETES_LEDER");
        specialItemMap.put("ENDER_BACKPACK", "GEHAERTETES_LEDER");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (cs instanceof Player) {
            cs.sendMessage(Messages.NO_PERM);
            return true;
        }
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            cs.sendMessage("Spieler nicht gefunden.");
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = item.getItemMeta();
    
        if (itemMeta == null || item.getType() == Material.AIR) {
            player.sendMessage(Utils.msg(Messages.MINI_PREFIX + "Du musst ein Item in der Hand halten."));
            return true;
        }
    
        String itemTag = ItemReader.getSign(item);
        if (itemTag == null) {
            itemTag = item.getType().name();
        }
    
        String repairMaterialTag = getRepairMaterialTag(itemTag);
        if (repairMaterialTag == null) {
            player.sendMessage(Utils.msg(Messages.MINI_PREFIX + "Dieses Item kann nicht repariert werden."));
            return true;
        }
    
        if (!(itemMeta instanceof Damageable damageable) || damageable.getDamage() == 0) {
            player.sendMessage(Utils.msg(Messages.MINI_PREFIX + "Dieses Item ist nicht beschädigt."));
            return true;
        }
    
        // Check if player has enough money
        if (!plugin.getEcon().has(player, PRICE)) {
            player.sendMessage(String.format(Messages.NOT_ENOUGH_MONEY, PRICE));
            return true;
        }
    
        // Check inventory for repair material and find its slot
        ItemStack repairItem = findRepairItem(player, repairMaterialTag);
        if (repairItem == null) {
            String displayName = getDisplayName(repairMaterialTag);
            player.sendMessage(Utils.msg(Messages.MINI_PREFIX + "Du benötigst ein <green>" + displayName + "</green><gray> und </gray><red>" + PRICE + " Taler</red><gray> zum Reparieren.</gray>"));
            return true;
        }
    
        // Remove repair material and money, then repair the item
        repairItem.setAmount(repairItem.getAmount() - 1);
    
        plugin.getEcon().withdrawPlayer(player, PRICE);
        damageable.setDamage(0);
        item.setItemMeta(damageable);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 0.4F, 0.8F);
    
        player.sendMessage(Utils.msg(Messages.MINI_PREFIX + "Dein Item wurde repariert."));
        cs.sendMessage("Das Item von " + player.getName() + " wurde repariert.");
    
        return true;
    }

    private ItemStack findRepairItem(Player player, String repairMaterialTag) {
        Material standardMaterial = materialMap.get(repairMaterialTag);

        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item == null) continue;

            if (standardMaterial != null && item.getType() == standardMaterial) {
                return item;
            }

            String itemTag = ItemReader.getSign(item);
            if (itemTag != null && itemTag.equalsIgnoreCase(repairMaterialTag)) {
                return item;
            }
        }
        return null;
    }

    private String getDisplayName(String materialTag) {
        Material material = materialMap.get(materialTag);
        if (material != null) {
            String materialName = material.name().toLowerCase();
            String translationKey;
            
            if (material.isBlock()) {
                translationKey = "block.minecraft." + materialName;
            } else {
                translationKey = "item.minecraft." + materialName;
            }
            
            return "<lang:" + translationKey + ">";
        }

        return formatString(materialTag);
    }

    // Formats a string to a nice display name
    public static String formatString(String input) {
        String[] words = input.toLowerCase().split("_");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }
        return result.toString().trim();
    }

    // Determine repair material or tag based on item tag
    private String getRepairMaterialTag(String itemTag) {
        String upperItemTag = itemTag.toUpperCase();

        if (specialItemMap.containsKey(upperItemTag)) {
            return specialItemMap.get(upperItemTag);
        }

        String[] parts = upperItemTag.split("_");
        if (parts.length > 1) {
            String materialPart = parts[0];
            if (materialMap.containsKey(materialPart)) {
                return materialPart;
            }
        }

        for (String key : materialMap.keySet()) {
            if (upperItemTag.startsWith(key)) {
                return key;
            }
        }

        return null;
    }
}