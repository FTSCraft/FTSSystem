package de.ftscraft.ftssystem.listeners;

import de.ftscraft.ftssystem.configs.Messages;
import de.ftscraft.ftssystem.features.TeleportWarmupManager;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftsutils.misc.MiniMsg;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class TeleportListener implements Listener {

    private final FtsSystem plugin;

    public TeleportListener(FtsSystem plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTeleport(PlayerTeleportEvent event) {
        handleTeleportWarmup(event);
    }

    private void handleTeleportWarmup(PlayerTeleportEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.COMMAND
                && event.getCause() != PlayerTeleportEvent.TeleportCause.PLUGIN
                && event.getCause() != PlayerTeleportEvent.TeleportCause.UNKNOWN) {
            return;
        }

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        //noinspection deprecation - if someone spoofs it, idc
        if (!player.isOnGround() && !player.isFlying() && !player.isInsideVehicle()) {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "Du kannst dich nicht teleportieren wenn du nicht auf dem Boden bist.");
            event.setCancelled(true);
            return;
        }

        if (!TeleportWarmupManager.isPlayerInFight(uuid)) {
            return;
        }

        MiniMsg.msg(player, Messages.MINI_PREFIX + "Dein TP wurde verzögert weil du vor kurzem Schaden erlitten hast. Bitte stehe 5 Sekunden still.");
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "Du wurdest nun teleportiert.");
            TeleportWarmupManager.playerSucceededWarmup(player, event.getTo());
        }, 20 * 5);

        event.setCancelled(true);
        TeleportWarmupManager.addPlayerToWarmup(uuid, bukkitTask);
    }

}
