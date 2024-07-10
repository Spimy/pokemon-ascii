package dev.spimy.pokemon.player;

import java.util.HashMap;

public class Inventory {
    private int exp;
    private HashMap<Pokeball, Integer> pokeballs;

    public Inventory(int exp, HashMap<Pokeball, Integer> pokeballs) {
        this.exp = exp;
        this.pokeballs = pokeballs;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(final int exp) {
        this.exp = exp;
    }

    public HashMap<Pokeball, Integer> getPokeballs() {
        return pokeballs;
    }

    /**
     * Level is scaled exponentially based on the exp of the player.
     * For each level, the amount of exp required to level up again is 1.5x the previous amount of exp.
     *
     * @return level of the player
     */
    public int getLevel() {
        final int baseExp = 500;
        final double growthRate = 1.5;

        final double level = Math.floor((Math.log((double) this.exp / baseExp) / Math.log(growthRate)) + 1);
        return (int) level;
    }
}