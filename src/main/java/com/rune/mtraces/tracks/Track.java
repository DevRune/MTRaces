package com.rune.mtraces.tracks;

import com.rune.mtraces.managers.LightManager;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import java.util.ArrayList;
import java.util.List;

public class Track {

    private final String name;
    private final List<String> checkpoints = new ArrayList<>();
    private final List<Location> lightLocations = new ArrayList<>();
    private final List<Location> barrierLocations = new ArrayList<>();
    private BlockFace lightRotation = BlockFace.NORTH; // Default rotation
    private final  LightManager lightManager;

    public Track(String name) {
        this.name = name;
       lightManager = new LightManager(this);
    }

    public String getName() {
        return name;
    }

    public void addCheckpoint(String regionName) {
        checkpoints.add(regionName);
    }

    public void addLightLocation(Location location) {
        lightLocations.add(location);
        lightManager.updateLightRotation(lightRotation);
    }

    public void addBarrierLocation(Location location) {
        barrierLocations.add(location);
    }

    public List<String> getCheckpointRegionNames() {
        return checkpoints;
    }

    public List<Location> getLightLocations() {
        return lightLocations;
    }

    public List<Location> getBarrierLocations() {
        return barrierLocations;
    }

    public BlockFace getLightRotation() {
        return lightRotation;
    }

    public void setLightRotation(BlockFace rotation) {
        this.lightRotation = rotation;
        lightManager.updateLightRotation(rotation);
    }

    public List<String> getCheckpoints() {
        return checkpoints;
    }

    public LightManager getLightManager() {
        return lightManager;
    }
}
