package de.ftscraft.ftssystem.utils.hooks;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Bukkit;

public class EngineHook {

    private static Engine engine;

    public static void hook() {
        if (HookManager.FTS_ENGINE_ENABLED)
            engine = (Engine) Bukkit.getServer().getPluginManager().getPlugin("FTSEngine");
    }

    public static Engine getEngine() {
        return engine;
    }
}
