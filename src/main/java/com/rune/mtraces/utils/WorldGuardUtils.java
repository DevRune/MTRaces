package com.rune.mtraces.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldGuardUtils {

    public boolean isInRegion(Location location, String regionName) {
        // Implement logic to check if a location is within a WorldGuard region
        return WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(location.getWorld())).getRegion(regionName).contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}
