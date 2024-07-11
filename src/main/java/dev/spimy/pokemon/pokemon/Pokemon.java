package dev.spimy.pokemon.pokemon;

public class Pokemon {

    // Name of the Pokémon
    private final String name;

    // Type of the Pokémon (e.g., fire, water, grass)
    private final PokemonType type;

    // Max HP (hit points/health) of the Pokémon
    private int maxHp;

    // Current HP (hit points/health) of the Pokémon
    private int currentHp;

    // Attack power of the Pokémon
    private double attackPower;

    // Speed of the Pokémon
    private double speed;

    private double critRate;

    private int exp;

    // Default constructor
    public Pokemon(
            final String name,
            final PokemonType type,
            final int maxHp,
            final int currentHp,
            final double attackPower,
            final double speed,
            final double critRate,
            final int exp
    ) {
        this.name = name;
        this.type = type;
        this.maxHp = maxHp;
        this.currentHp = currentHp;
        this.attackPower = attackPower;
        this.speed = speed;
        this.critRate = critRate;
        this.exp = exp;
    }

    // Getters and Setters
    public String getName() {
        return this.name;
    }

    public PokemonType getType() {
        return type;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(final int maxHp) {
        this.maxHp = maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(final int currentHp) {
        this.currentHp = currentHp;
    }

    public double getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(final double attackPower) {
        this.attackPower = attackPower;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(final double speed) {
        this.speed = speed;
    }

    public double getCritRate() {
        return critRate;
    }

    public void setCritRate(final double critRate) {
        this.critRate = critRate;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(final int exp) {
        this.exp = exp;
    }

    /**
     * The total exp required for the next level is calculated through current exp + current level * base exp
     * In the case of 500 base exp, total exp for level 1 to level 5 are as such: 0, 500, 1500, 3000, 5000
     *
     * @return level of the Pokémon
     */
    public int getLevel() {
        final int baseExp = 500;
        return (int) Math.floor((1 + Math.sqrt(1 + 8 * (double) this.exp / baseExp)) / 2);
    }

    @Override
    public String toString() {
        return String.format(
            "%s,%s,%s,%s,%s,%s,%s,%s",
            this.name,
            this.type,
            this.maxHp,
            this.currentHp,
            this.attackPower,
            this.speed,
            this.critRate,
            this.exp
        );
    }
}

