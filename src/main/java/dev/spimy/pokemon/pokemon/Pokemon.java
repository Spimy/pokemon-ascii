package dev.spimy.pokemon.pokemon;

public class Pokemon {

    // Name of the Pokemon
    private String name;

    // Type of the Pokemon (e.g., fire, water, grass)
    private String type;

    // HP (hit points/health) of the Pokemon
    private double HP;

    // Attack power of the Pokemon
    private double attackPower;

    // Speed of the Pokemon
    private double speed;

    // Current status of the Pokemon (e.g., healthy, poisoned, paralyzed)
    private String status;

    // Default constructor
    public Pokemon(String name, String type, double HP, double attackPower, double speed, String status) {
        this.name = name;
        this.type = type;
        this.HP = HP;
        this.attackPower = attackPower;
        this.speed = speed;
        this.status = status;
    }

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getHP() {
        return HP;
    }

    public void setHP(double HP) {
        this.HP = HP;
    }

    public double getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(double attackPower) {
        this.attackPower = attackPower;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

