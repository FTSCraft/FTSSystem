package de.ftscraft.ftssystem.features.voterewards.storage;

import de.ftscraft.ftsutils.items.FTSItem;

import java.util.ArrayList;
import java.util.List;

public class VoteRewardStorage {

    private String name = "Schwamm";
    private List<FTSItem> guaranteed = new ArrayList<>();
    private List<FTSItem> random = new ArrayList<>();
    private int amountRandom = 1;

    public VoteRewardStorage() {

    }

    public int getAmountRandom() {
        return amountRandom;
    }

    public List<FTSItem> getGuaranteedItems() {
        return guaranteed;
    }

    public List<FTSItem> getRandomItems() {
        return random;
    }

    public String getName() {
        return name;
    }

}
