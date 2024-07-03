package dev.spimy.pokemon.player;

public enum Pokeball {
    NORMAL("Poké Ball", 15),
    ULTRA("Ultra Ball", 30),
    MASTER("Master Ball", 100);

    public final String name;
    public final int successRate;

    Pokeball(String name, int successRate) {
        this.name = name;
        this.successRate = successRate;
    }
}
