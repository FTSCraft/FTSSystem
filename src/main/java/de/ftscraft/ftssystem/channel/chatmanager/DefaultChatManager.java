package de.ftscraft.ftssystem.channel.chatmanager;

import de.ftscraft.ftssystem.channel.Channel;
import de.ftscraft.ftssystem.main.FtsSystem;
import de.ftscraft.ftssystem.main.User;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

public class DefaultChatManager extends ChatManager {

    public DefaultChatManager(FtsSystem plugin) {
        super(plugin);
    }

    @Override
    public void chat(User u, String msg, Channel channel) {
        chat(u, msg);
    }

    @Override
    public void chat(User u, String msg) {
        Bukkit.broadcast(Component.text(u.getPlayer().getName() + " " + msg));
    }
}
