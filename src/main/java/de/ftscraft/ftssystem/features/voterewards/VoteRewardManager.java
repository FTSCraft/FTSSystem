package de.ftscraft.ftssystem.features.voterewards;

import de.ftscraft.ftssystem.features.voterewards.storage.VoteRewardStorage;
import de.ftscraft.ftssystem.features.voterewards.storage.VoteAllRewardsStorage;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftsutils.items.FTSItem;
import de.ftscraft.ftsutils.storage.DataHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class VoteRewardManager {

    private final Map<String, VoteRewardStorage> rewards = new HashMap<>();

    public VoteRewardManager(@NotNull DataHandler dataHandler) {
        getRewards(dataHandler.get(VoteAllRewardsStorage.class));
        FtsSystem.getChatLogger().warning("Loaded " + rewards.size() + " vote rewards.");
        // printAllRewards();
    }

    private void getRewards(VoteAllRewardsStorage rewardsStorage) {
        for (VoteRewardStorage reward : rewardsStorage.getRewards()) {
            this.rewards.put(reward.getName(), reward);
        }
    }

    public void giveReward(@NotNull Player player, @NotNull VoteRewardStorage reward) {

        Inventory inv = player.getInventory();

        List<ItemStack> failedItems = new ArrayList<>();
        reward.getGuaranteedItems().forEach(item -> failedItems.addAll(inv.addItem(generateItemStack(item)).values()));

        reward.getRandomItems().stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        l -> {
                            Collections.shuffle(l);
                            return l.stream().limit(reward.getAmountRandom()).toList();
                        }
                )).forEach(item -> failedItems.addAll(inv.addItem(generateItemStack(item)).values()));

        failedItems.forEach(itemStack -> player.getWorld().dropItem(player.getLocation(), itemStack));

    }

    public boolean checkInvSpace(@NotNull Player player, @NotNull VoteRewardStorage reward) {
        Inventory inv = player.getInventory();
        int emptySlots = 0;
        for (ItemStack stack : inv.getContents()) {
            if (stack == null || stack.getType() == Material.AIR) emptySlots++;
        }
        return emptySlots >= reward.getAmountRandom() + reward.getGuaranteedItems().size();
    }

    public @Nullable VoteRewardStorage getRewardByName(String name) {
        return rewards.get(name);
    }

    public void printAllRewards() {
        FtsSystem.getChatLogger().warning("=== All Vote Rewards ===");
        for (Map.Entry<String, VoteRewardStorage> entry : rewards.entrySet()) {
            VoteRewardStorage reward = entry.getValue();
            FtsSystem.getChatLogger().warning("Reward: " + reward.getName());
            FtsSystem.getChatLogger().warning("  Amount Random: " + reward.getAmountRandom());
            FtsSystem.getChatLogger().warning("  Guaranteed Items (" + reward.getGuaranteedItems().size() + "):");
            for (FTSItem item : reward.getGuaranteedItems()) {
                FtsSystem.getChatLogger().warning("    - " + item.getMaterial() + " x" + item.getAmount());
            }
            FtsSystem.getChatLogger().warning("  Random Items (" + reward.getRandomItems().size() + "):");
            for (FTSItem item : reward.getRandomItems()) {
                FtsSystem.getChatLogger().warning("    - " + item.getMaterial() + " x" + item.getAmount());
            }
            FtsSystem.getChatLogger().warning("------------------------");
        }
    }

    private @NotNull ItemStack generateItemStack(@NotNull FTSItem rewardItem) {
        return rewardItem.toItemStack();
    }

}
