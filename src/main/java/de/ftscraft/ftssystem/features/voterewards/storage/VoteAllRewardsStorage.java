package de.ftscraft.ftssystem.features.voterewards.storage;

import de.ftscraft.ftsutils.storage.Storage;
import de.ftscraft.ftsutils.storage.StorageType;

import java.util.ArrayList;
import java.util.List;

@Storage(name = "rewards", type = StorageType.JSON, config = true, editable = false)
public class VoteAllRewardsStorage {

    private List<VoteRewardStorage> rewards = new ArrayList<>();

    public VoteAllRewardsStorage() {
    }

    public List<VoteRewardStorage> getRewards() {
        return rewards;
    }
}
