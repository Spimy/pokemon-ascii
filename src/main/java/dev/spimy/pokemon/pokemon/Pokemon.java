package dev.spimy.pokemon.pokemon;

public class Pokemon {

    // Name of the Pokemon
    private String name;

    // Type of the Pokemon (e.g., fire, water, grass)
    private PokemonType type;

    // Max HP (hit points/health) of the Pokemon
    private double maxHp;

    // Current HP (hit points/health) of the Pokemon
    private double currentHp;

    // Attack power of the Pokemon
    private double attackPower;

    // Speed of the Pokemon
    private double speed;

    // Current status of the Pokemon (e.g., healthy, poisoned, paralyzed)
    private String status;

    // Default constructor
    public Pokemon(final String name, final PokemonType type, final double maxHp, final double currentHp, final double attackPower, final double speed, final String status) {
        this.name = name;
        this.type = type;
        this.maxHp = maxHp;
        this.currentHp = currentHp;
        this.attackPower = attackPower;
        this.speed = speed;
        this.status = status;
    }

    // Getters and Setters
    public PokemonType getType() {
        return type;
    }

    public double getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(final double maxHp) {
        this.maxHp = maxHp;
    }

    public double getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(final double currentHp) {
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

}

