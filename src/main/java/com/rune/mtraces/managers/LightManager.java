package com.rune.mtraces.managers;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.rune.mtraces.MTRaces;
import com.rune.mtraces.tracks.Track;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class LightManager {
    private final Track track;
    private boolean lightsOn = false;

    // Texture URLs for the lights
    private static final String GRAY_LIGHT_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmQ1ZjE2OTBhOWJhMmIwZTliNTlkZTQxN2I4ODkzNzZiYzI3M2JjZjY5ZTBlZTIzNzUzNDc0NjAzNTlkZTg3NSJ9fX0=";
    private static final String GREEN_LIGHT_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTI5YjBiMmY3YzhhNWYwNjBmYjY3NDBjZmM0Y2I3OGVmYjYxZjlmMTZjOGU5NGYxYjc3MjU2N2ZkNDJjNjViYSJ9fX0=";
    private static final String RED_LIGHT_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWE0NGRhMGFmOTBhY2I2MDdlYWIyOGYyODc5ODUwNGE3MzE4OTM3YTE1N2ZiM2EwM2UxNDdhZTcwZTM1MzFjZSJ9fX0=";

    public LightManager(Track track) {
        this.track = track;
    }

    public void startLights() {
        lightsOn = true;

        // Set all lights to gray initially
        setLights(GRAY_LIGHT_TEXTURE);

        // Animate lights from left to right turning red
        new BukkitRunnable() {
            private int currentIndex = 0;

            @Override
            public void run() {
                if (!lightsOn) {
                    cancel();
                    return;
                }

                List<Location> lightLocations = track.getLightLocations();
                BlockFace lightRotation = track.getLightRotation();

                if (currentIndex < lightLocations.size()) {
                    setLightTexture(lightLocations.get(currentIndex), lightRotation, RED_LIGHT_TEXTURE);
                    currentIndex++;
                } else {
                    setLights(GREEN_LIGHT_TEXTURE);
                    removeBarriers();
                    cancel();
                }
            }
        }.runTaskTimer(MTRaces.getInstance(), 15L, 15L); // Adjust task parameters as needed
    }

    public void startLights(boolean drag) {
        lightsOn = true;

        // Set all lights to gray initially
        setLights(GRAY_LIGHT_TEXTURE);

        // Animate lights from left to right turning red
        new BukkitRunnable() {
            private int currentIndex = 0;

            @Override
            public void run() {
                if (!lightsOn) {
                    cancel();
                    return;
                }

                List<Location> lightLocations = track.getLightLocations();
                BlockFace lightRotation = track.getLightRotation();

                if (currentIndex < lightLocations.size()) {
                    setLightTexture(lightLocations.get(currentIndex), lightRotation, RED_LIGHT_TEXTURE);
                    currentIndex++;
                } else {
                    double time = Math.random()*10;
                    time -= 5;
                    if(time<0){
                        time *= -1;
                    }
                    if(drag){
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                setLights(GREEN_LIGHT_TEXTURE);
                                removeBarriers();
                            }
                        }.runTaskLater(MTRaces.getInstance(), (long)(time*20));
                    }
                    cancel();
                }
            }
        }.runTaskTimer(MTRaces.getInstance(), 15L, 15L);
    }

    private void setLights(String texture) {
        List<Location> lightLocations = track.getLightLocations();
        BlockFace lightRotation = track.getLightRotation();
        for (int i = 0; i < lightLocations.size(); i++) {
            setLightTexture(lightLocations.get(i), lightRotation, texture);
        }
    }

    private void setLightTexture(Location location, BlockFace rotation, String texture) {
        Block block = location.getBlock();
        block.setType(Material.PLAYER_WALL_HEAD);
        BlockState state = block.getState();

        if (state instanceof Skull) {
            Skull skull = (Skull) state;
            skull.setRotation(rotation); // Set the direction the head is facing

            // Set the custom texture using GameProfile
            GameProfile profile = new GameProfile(UUID.randomUUID(), "");
            profile.getProperties().put("textures", new Property("textures", texture));

            try {
                Field profileField = skull.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skull, profile);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            skull.update();
        }
    }

    public void updateLightRotation(BlockFace rotation) {
        // Update the rotation for the lights
        List<Location> lightLocations = track.getLightLocations();
        for (Location location : lightLocations) {
            setLightTexture(location, rotation, GRAY_LIGHT_TEXTURE); // Example with RED_LIGHT_TEXTURE
        }
    }

    public void removeBarriers() {
        List<Location> barrierLocations = track.getBarrierLocations();
        for (Location location : barrierLocations) {
            Block block = location.getBlock();
            // Set the block to AIR or another type that represents the removed barrier
            block.setType(Material.AIR);
        }
    }
}
