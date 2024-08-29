package de.ftscraft.ftssystem.utils.hooks;

import org.bukkit.Bukkit;

public class HookManager {

    public static final boolean LUCK_PERMS_ENABLED = Bukkit.getServer().getPluginManager().isPluginEnabled("LuckPerms");
    public static final boolean FTS_ENGINE_ENABLED = Bukkit.getServer().getPluginManager().isPluginEnabled("FTSEngine");
    public static final boolean TOWNY_ENABLED = Bukkit.getServer().getPluginManager().isPluginEnabled("Towny");
    public static final boolean ESSENTIALS_ENABLED = Bukkit.getServer().getPluginManager().isPluginEnabled("Essentials");

}
