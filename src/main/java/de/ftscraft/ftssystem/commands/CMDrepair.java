package de.ftscraft.ftssystem.commands;

import de.ftscraft.ftssystem.configs.Messages;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.utils.Utils;
import de.ftscraft.ftsutils.items.ItemReader;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        materialMap.put("WOODEN", Material.OAK_PLANKS);
        materialMap.put("WOOD", Material.OAK_PLANKS);
        materialMap.put("LEATHER", Material.LEATHER);
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
        if (!(cs instanceof Player player)) {
            cs.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        if (!player.hasPermission(PERMISSION)) {
            player.sendMessage(Messages.NO_PERM);
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null || item.getType() == Material.AIR) {
            player.sendMessage(Utils.msg(Messages.MINI_PREFIX + "Du musst ein Item in der Hand halten."));
            return true;
        }

        if (!(itemMeta instanceof Damageable damageable)) {
            player.sendMessage(Utils.msg(Messages.MINI_PREFIX + "Dieses Item kann keinen Schaden nehmen, daher auch <red>nicht</red> repariert werden."));
            return true;
        }

        if (damageable.getDamage() == 0) {
            player.sendMessage(Utils.msg(Messages.MINI_PREFIX + "Dieses Item ist nicht beschädigt."));
            return true;
        }

        String itemTag = ItemReader.getSign(item);
        if (itemTag == null) {
            itemTag = item.getType().name();
        }

        // Determine repair material or tag based on item tag
        String repairMaterialTag = getRepairMaterialTag(itemTag);
        if (repairMaterialTag == null) {
            return true;
        }

        // Check inventory for repair material
        ItemStack repairItem = findRepairItem(player, repairMaterialTag);
        if (repairItem == null) {
            String displayName = getDisplayName(repairMaterialTag);
            player.sendMessage(Utils.msg(Messages.MINI_PREFIX + "Du benötigst <green>1x " + displayName + "</green> zum Reparieren."));
            return true;
        }

        // Check if player has enough money
        if (!plugin.getEcon().has(player, PRICE)) {
            player.sendMessage(String.format(Messages.NOT_ENOUGH_MONEY, PRICE));
            return true;
        }

        // Remove repair material and money, then repair the item
        removeItem(player, repairItem);
        plugin.getEcon().withdrawPlayer(player, PRICE);
        damageable.setDamage(0);
        item.setItemMeta(damageable);

        String displayName = getDisplayName(repairMaterialTag);
        player.sendMessage(Utils.msg(Messages.MINI_PREFIX + "Dein Item wurde repariert. Du hast mit <red>" + PRICE +
                "</red> Taler und <green>1x " + displayName + "</green> bezahlt."));

        return true;
    }

    @Nullable
    private ItemStack findRepairItem(Player player, String repairMaterialTag) {
        if (repairMaterialTag.contains("PLANKS")) {
            return findAnyItemWithNamePart(player, "PLANKS");
        }

        Material standardMaterial = materialMap.get(repairMaterialTag);
        if (standardMaterial != null && playerHasMaterial(player, standardMaterial, 1)) {
            return new ItemStack(standardMaterial);
        }

        return findItemWithTag(player, repairMaterialTag);
    }

    private String getDisplayName(String materialTag) {
        Material material = materialMap.get(materialTag);
        if (material != null) {
            return formatString(material.name());
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

    // Find specific material in player's inventory
    private ItemStack findAnyItemWithNamePart(Player player, String namePart) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType().name().contains(namePart)) {
                return item.clone();
            }
        }
        return null;
    }

    // Find an item with a specific sign tag
    private ItemStack findItemWithTag(Player player, String tag) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                String itemTag = ItemReader.getSign(item);
                if (itemTag != null && itemTag.equalsIgnoreCase(tag)) {
                    return item.clone();
                }
            }
        }
        return null;
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

    // Check if player has a specific material in their inventory
    private boolean playerHasMaterial(Player player, Material material, int amount) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == material) {
                count += item.getAmount();
                if (count >= amount) {
                    return true;
                }
            }
        }
        return false;
    }

    // Remove a specific item from player's inventory
    private void removeItem(Player player, ItemStack itemToRemove) {
        if (itemToRemove == null) return;

        Material material = itemToRemove.getType();
        String signTag = ItemReader.getSign(itemToRemove);

        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack invItem = player.getInventory().getItem(i);
            if (invItem == null) continue;

            boolean matches = false;

            if (signTag != null) {
                String invItemTag = ItemReader.getSign(invItem);
                matches = invItemTag != null && invItemTag.equalsIgnoreCase(signTag);
            }
            else {
                matches = invItem.getType() == material;
            }

            if (matches) {
                if (invItem.getAmount() > 1) {
                    invItem.setAmount(invItem.getAmount() - 1);
                } else {
                    player.getInventory().setItem(i, null);
                }
                return;
            }
        }
    }
}