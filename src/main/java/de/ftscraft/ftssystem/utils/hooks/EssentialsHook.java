package de.ftscraft.ftssystem.utils.hooks;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.commands.WarpNotFoundException;
import net.ess3.api.InvalidWorldException;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Collection;

public class EssentialsHook {

    private static Essentials essentials = null;

    public static Location getWarpLocation(String warp) {
        try {
            return essentials.getWarps().getWarp(warp);
        } catch (WarpNotFoundException | InvalidWorldException e) {
            return null;
        }
    }

    public static Collection<String> getWarps() {
        return essentials.getWarps().getList();
    }

    public static void hook() {
        essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
    }

}
