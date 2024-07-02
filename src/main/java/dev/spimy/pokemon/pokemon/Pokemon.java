package dev.spimy.pokemon.pokemon;

public class Pokemon {

    // Name of the Pokémon
    private String name;

    // Type of the Pokémon (e.g., fire, water, grass)
    private PokemonType type;

    // Max HP (hit points/health) of the Pokémon
    private int maxHp;

    // Current HP (hit points/health) of the Pokémon
    private int currentHp;

    // Attack power of the Pokémon
    private double attackPower;

    // Speed of the Pokémon
    private double speed;

    // Current status of the Pokémon (e.g., healthy, poisoned, paralyzed)
    private String status;

    private float critRate;

    private int exp;

    // Default constructor
    public Pokemon(
            final String name,
            final PokemonType type,
            final int maxHp,
            final int currentHp,
            final double attackPower,
            final double speed,
            final String status,
            final float critRate,
            final int exp
    ) {
        this.name = name;
        this.type = type;
        this.maxHp = maxHp;
        this.currentHp = currentHp;
        this.attackPower = attackPower;
        this.speed = speed;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public float getCritRate() {
        return critRate;
    }

    public void setCritRate(float critRate) {
        this.critRate = critRate;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    /**
     * Level is scaled exponentially based on the exp of the Pokémon.
     * For each level, the amount of exp required to level up again is 1.5x the previous amount of exp.
     *
     * @return level of the Pokémon
     */
    public int getLevel() {
        final int baseExp = 500;
        final double growthRate = 1.5;

        final double level = Math.floor((Math.log((double) this.exp / baseExp) / Math.log(growthRate)) + 1);
        return (int) level;
    }
}

