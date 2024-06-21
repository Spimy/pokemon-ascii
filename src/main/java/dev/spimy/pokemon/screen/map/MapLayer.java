package dev.spimy.pokemon.screen.map;

public enum MapLayer {
    OVERWORLD("maps/overworld.txt", "X"),
    HOUSE("maps/house.txt", "D"),
    CENTER("maps/center.txt", "C"),
    STORE("maps/store.txt", "S");

    public final String resourcePath;
    public final String doorChar;

    MapLayer(String resourcePath, String doorChar) {
        this.resourcePath = resourcePath;
        this.doorChar = doorChar;
    }
}
