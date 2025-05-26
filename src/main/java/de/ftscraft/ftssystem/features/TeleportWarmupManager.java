package de.ftscraft.ftssystem.features;

import de.ftscraft.ftssystem.configs.Messages;
import de.ftscraft.ftsutils.misc.MiniMsg;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TeleportWarmupManager {

    private static final long COOLDOWN = 1000 * 30;
    private final static Map<UUID, Long> playersInFight = new HashMap<>();
    private static final Map<UUID, BukkitTask> playersInWarmup = new ConcurrentHashMap<>();

    public static void playerReceivedDamage(Player player) {
        UUID uuid = player.getUniqueId();
        playersInFight.put(uuid, System.currentTimeMillis() + COOLDOWN);
        if (playersInWarmup.containsKey(uuid)) {
            playersInWarmup.get(uuid).cancel();
            MiniMsg.msg(player, Messages.MINI_PREFIX + "Dein Teleport wurde abgebrochen!");
        }
    }

    public static boolean isPlayerInFight(UUID uuid) {
        if (!playersInFight.containsKey(uuid))
            return false;
        return playersInFight.get(uuid) > System.currentTimeMillis();
    }

    public static void addPlayerToWarmup(UUID uuid, BukkitTask task) {
        playersInWarmup.put(uuid, task);
    }

    public static void playerSucceededWarmup(Player player, @NotNull Location eventTo) {
        UUID uuid = player.getUniqueId();
        playersInFight.remove(uuid);
        player.teleport(eventTo);
        playersInWarmup.remove(uuid);
    }

    public static void playerMoved(Player player) {
        UUID uuid = player.getUniqueId();
        if (!playersInWarmup.containsKey(uuid)) {
            return;
        }
        playersInWarmup.get(uuid).cancel();
        playersInWarmup.remove(uuid);
        MiniMsg.msg(player, Messages.MINI_PREFIX + "Dein Teleport wurde abgebrochen, weil du dich bewegt hast.");
    }

}
