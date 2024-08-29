/*
 * Copyright (c) 2018.
 * halberfan - AfGMedia / AfGeSports
 */

package de.ftscraft.ftssystem.listeners;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.punishment.Punishment;
import de.ftscraft.ftssystem.punishment.PunishmentType;
import de.ftscraft.ftssystem.punishment.TemporaryPunishment;
import de.ftscraft.ftssystem.utils.APIHandling;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class LoginListener implements Listener {

    private final FtsSystem plugin;

    public LoginListener(FtsSystem plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPing(PaperServerListPingEvent event) {
        if (plugin.isInWartung()) {
            event.setMotd("§bDer Server ist leider derzeit in§4 Wartung§r!\n§bWeitere Infos findest du im Forum oder im Discord");
            event.setMaxPlayers(0);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PlayerLoginEvent event) {

        Player p = event.getPlayer();

        if (plugin.isInWartung()) {
            if (!p.hasPermission("ftssystem.wartung.bypass")) {
                event.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);
                event.kickMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("""
                        §4Wir haben derzeit Wartungsarbeiten.
                        §bProbier es später nochmal. Im Forum oder im Discord wirst du wahrscheinlich weitere Infos erhalten!
                        §bBis später :)"""));
            }
            return;
        }

        //See if we should check if user uses VPN
        if (!p.hasPermission("ftssystem.vpnbypass")) {
            if (APIHandling.hasUserVPN(event.getAddress())) {
                event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                event.kickMessage(LegacyComponentSerializer.legacySection().deserialize("""
                        §bGrüße!
                        
                        Schön, dass du auf FTS spielen möchtest. Leider haben wir aber bei dir einen VPN festgestellt.
                        
                        Um rege Bann-Umgehungen zu verhindern, werden §cneue §bAccounts (nach der Freischaltung sind Proxys erlaubt), die einen VPN nutzen, §cblockiert§b.
                        
                        Bitte stelle dein VPN §caus§b, oder, wenn du VPN/Proxy §cnicht ausschalten kannst§b, melde dich bei uns im §cDiscord§b."""));
                return;
            }
        }

        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }

        Punishment pu = plugin.getPunishmentManager().isBanned(p);
        if (pu == null) {
            event.setResult(PlayerLoginEvent.Result.ALLOWED);
            return;
        }

        if (pu.getType() == PunishmentType.BAN) {
            event.setResult(PlayerLoginEvent.Result.KICK_BANNED);
            event.kickMessage(LegacyComponentSerializer.legacySection().deserialize("§4Du wurdest gebannt! \n" +
                                 "§eGebannt von: §b" + pu.getAuthorName() + "\n" +
                                 "§eBis: §bPERMANENT \n" +
                                 "§eGrund: §b" + pu.getReason() + "\n" +
                                 " \n" +
                                 "§6Du kannst einen Entbannungsbeitrag im Forum schreiben"));
        } else {
            event.setResult(PlayerLoginEvent.Result.KICK_BANNED);
            event.kickMessage(LegacyComponentSerializer.legacySection().deserialize("§4Du wurdest gebannt! \n" +
                                 "§eGebannt von: §b" + pu.getAuthorName() + "\n" +
                                 "§eNoch: §b" + ((TemporaryPunishment) pu).untilAsString() + "\n" +
                                 "§eGrund: §b" + pu.getReason() + "\n" +
                                 " \n" +
                                 "§6Du kannst einen Entbannungsbeitrag im Forum schreiben"));
        }

    }
}
