package dev.spimy.pokemon.player;

import java.util.HashMap;

public class Inventory {
    private int exp;
    private int money;
    private final HashMap<Pokeball, Integer> pokeballs;

    public Inventory(final int exp, final int money, final HashMap<Pokeball, Integer> pokeballs) {
        this.exp = exp;
        this.money = money;
        this.pokeballs = pokeballs;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(final int exp) {
        this.exp = exp;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(final int money) {
    this.money = money;
    }

    public HashMap<Pokeball, Integer> getPokeballs() {
        return pokeballs;
    }

    /**
     * The total exp required for the next level is calculated through current exp + current level * base exp
     * In the case of 500 base exp, total exp for level 1 to level 5 are as such: 0, 500, 1500, 3000, 5000
     *
     * @return level of the player
     */
    public int getLevel() {
        final int baseExp = 500;
        return (int) Math.floor((1 + Math.sqrt(1 + 8 * (double) this.exp / baseExp)) / 2);
    }
}
