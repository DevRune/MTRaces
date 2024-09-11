package com.rune.mtraces.managers;

import com.rune.mtraces.MTRaces;
import com.rune.mtraces.tracks.Track;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackManager {
    private static TrackManager instance;
    private final Map<String, Track> tracks = new HashMap<>();
    private Track selectedTrack;
    private final File file;

    private TrackManager() {
        file = new File(MTRaces.getInstance().getDataFolder(), "tracks.yml");
    }

    public static TrackManager getInstance() {
        if (instance == null) {
            instance = new TrackManager();
        }
        return instance;
    }

    public void addTrack(String name, Track track) {
        tracks.put(name, track);
        saveTracks();
    }

    public Track getTrack(String name) {
        return tracks.get(name);
    }

    public void removeTrack(String name) {
        tracks.remove(name);
        saveTracks(); // Save changes
    }

    public boolean trackExists(String name) {
        return tracks.containsKey(name);
    }

    public void selectTrack(String name) {
        selectedTrack = tracks.get(name);
    }

    public Track getSelectedTrack() {
        return selectedTrack;
    }

    public void loadTracks() {
        if (!file.exists()) {
            return; // No tracks to load
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (String key : config.getKeys(false)) {
            String name = key;
            Track track = new Track(name);

            // Load checkpoints
            List<String> checkpoints = config.getStringList(name + ".checkpoints");
            for (String checkpoint : checkpoints) {
                track.addCheckpoint(checkpoint);
            }

            // Load light locations
            List<Map<?, ?>> lightLocationsData = config.getMapList(name + ".lightLocations");
            for (Map<?, ?> data : lightLocationsData) {
                Location location = Location.deserialize((Map<String, Object>) data.get("location"));
                track.addLightLocation(location);
            }

            // Load light rotation
            String rotationStr = config.getString(name + ".lightRotation", "NORTH");
            BlockFace rotation = BlockFace.valueOf(rotationStr);
            track.setLightRotation(rotation);

            // Load barrier locations
            List<Map<?, ?>> barrierLocationsData = config.getMapList(name + ".barrierLocations");
            for (Map<?, ?> data : barrierLocationsData) {
                Location location = Location.deserialize((Map<String, Object>) data.get("location"));
                track.addBarrierLocation(location);
            }

            // Add track to manager
            tracks.put(name, track);
        }
    }


    public void saveTracks() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (Map.Entry<String, Track> entry : tracks.entrySet()) {
            String name = entry.getKey();
            Track track = entry.getValue();

            config.set(name + ".checkpoints", track.getCheckpointRegionNames());

            List<Map<String, Object>> lightLocationsData = new ArrayList<>();
            for (Location location : track.getLightLocations()) {
                Map<String, Object> data = new HashMap<>();
                data.put("location", location.serialize()); // Serialize Location
                lightLocationsData.add(data);
            }
            config.set(name + ".lightLocations", lightLocationsData);

            config.set(name + ".lightRotation", track.getLightRotation().toString());

            List<Map<String, Object>> barrierLocationsData = new ArrayList<>();
            for (Location location : track.getBarrierLocations()) {
                Map<String, Object> data = new HashMap<>();
                data.put("location", location.serialize()); // Serialize Location
                barrierLocationsData.add(data);
            }
            config.set(name + ".barrierLocations", barrierLocationsData);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
