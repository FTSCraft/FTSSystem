package de.ftscraft.ftssystem.utils.hooks;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;

import java.util.UUID;

public class LuckPermsHook {

    private static LuckPerms luckPerms = null;

    /**
     *
     * @param player UUID of player
     * @param group Name of the group
     * @return -1 when (temporary) group node is not found
     *         else, the seconds from the epoch of 1970-01-01T00:00:00Z
     */
    public static long getParentDuration(UUID player, String group) {
        for (Node node : luckPerms.getUserManager().getUser(player).getNodes()) {
            if (node instanceof InheritanceNode inheritanceNode) {
                if (inheritanceNode.getGroupName().equalsIgnoreCase(group)) {
                    if (inheritanceNode.getExpiry() != null)
                        return inheritanceNode.getExpiry().getEpochSecond();
                }
            }
        }
        return -1;
    }

    public static void hook() {
        luckPerms = LuckPermsProvider.get();
    }

}
