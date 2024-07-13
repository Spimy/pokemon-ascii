package dev.spimy.pokemon.screen.map;

import dev.spimy.pokemon.screen.Theme;
import org.jline.jansi.Ansi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GameMap {
    private MapLayer currentMap;
    private final HashMap<Character, MapLayer> doors = new HashMap<>();
    private final HashMap<String, ArrayList<String>> loadedMaps = new HashMap<>();
    private final List<Character> walls = List.of('_', '/', '\\', '|');

    public GameMap() {
        this.currentMap = MapLayer.HOUSE;
        this.loadMaps();
    }

    private void loadMaps() {
        for (final MapLayer mapLayer : MapLayer.values()) {
            this.doors.put(mapLayer.doorChar, mapLayer);

            try (final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(mapLayer.resourcePath)) {
                if (inputStream == null) continue;
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                final ArrayList<String> mapRows = new ArrayList<>();

                while ((line = bufferedReader.readLine()) != null) {
                    mapRows.add(line);
                }

                final int length = mapRows.stream().map(String::length).max(Integer::compareTo).orElse(0);
                final ArrayList<String> formattedMapRows = mapRows
                    .stream()
                    .map(row -> row + " ".repeat(length - row.length()))
                    .collect(Collectors.toCollection(ArrayList::new));

                this.loadedMaps.put(mapLayer.resourcePath, formattedMapRows);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean setCurrentMap(final MapLayer newMapLayer) {
        if (newMapLayer.doorChar == this.currentMap.doorChar) return false;
        this.currentMap = newMapLayer;
        return true;
    }

    public MapLayer getCurrentMap() {
        return this.currentMap;
    }

    public ArrayList<String> getCurrentMapData() {
        return this.loadedMaps.get(this.getCurrentMap().resourcePath);
    }

    public HashMap<Character, MapLayer> getDoors() {
        return this.doors;
    }

    public List<Character> getWalls() {
        return this.walls;
    }

    public HashMap<Character, Integer[]> getDoorCoordinates(final String[][] buffer) {
        final HashMap<Character, Integer[]> doorCoordinates = new HashMap<>();

        for (int i = 0; i < buffer.length; i++) {
            for (int j = 0; j < buffer[i].length; j++) {
                if (this.getDoors().containsKey(buffer[i][j].charAt(0))) {
                    doorCoordinates.put(buffer[i][j].charAt(0), new Integer[]{i, j});
                }
            }
        }

        return doorCoordinates;
    }

    public int[] getLobbyCoordinates(final String[][] buffer) {
        for (int i = 0; i < buffer.length; i++) {
            for (int j = 0; j < buffer[i].length; j++) {
                if (buffer[i][j].charAt(0) == '=') {
                    return new int[]{i, j};
                }
            }
        }

        return null;
    }

    public String getGrass() {
        return Ansi.ansi()
                .bg(Theme.BACKGROUND_COLOR)
                .fg(Theme.GRASS_COLOR)
                .a(';')
                .reset()
                .toString();
    }
}
