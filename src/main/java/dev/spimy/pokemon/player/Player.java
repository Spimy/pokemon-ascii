package dev.spimy.pokemon.player;

import dev.spimy.pokemon.controller.Control;
import dev.spimy.pokemon.controller.Direction;
import dev.spimy.pokemon.player.saves.InventorySave;
import dev.spimy.pokemon.player.saves.OwnedPokemon;
import org.jline.terminal.Terminal;

import java.util.concurrent.ThreadLocalRandom;

public class Player {
    private final Control control;
    private final Position position;

    private final OwnedPokemon ownedPokemon = new OwnedPokemon();
    private final InventorySave inventorySave = new InventorySave();

    private Direction direction;

    public Player(final Terminal terminal, final Control control) {
        this.control = control;

        final int height = terminal.getHeight();
        final int width = terminal.getWidth();

        this.position = new Position(height / 2, width / 2);
        this.direction = Direction.values()[ThreadLocalRandom.current().nextInt(Direction.values().length)];
    }

    public void move() {
        this.position.setPrevX(this.position.getCurrX());
        this.position.setPrevY(this.position.getCurrY());
        switch (this.direction) {
            case UP -> this.position.setCurrX(this.position.getCurrX() - 1);
            case DOWN -> this.position.setCurrX(this.position.getCurrX() + 1);
            case LEFT -> this.position.setCurrY(this.position.getCurrY() - 1);
            case RIGHT -> this.position.setCurrY(this.position.getCurrY() + 1);
        }
    }

    public void backtrack() {
        switch (this.direction) {
            case Direction.UP -> this.setDirection(Direction.DOWN);
            case Direction.DOWN -> this.setDirection(Direction.UP);
            case Direction.LEFT -> this.setDirection(Direction.RIGHT);
            case Direction.RIGHT -> this.setDirection(Direction.LEFT);
        }
        this.move();
    }

    public void setDirection(final int key) {
        if (this.control.isUp(key)) {
            this.direction = Direction.UP;
        } else if (this.control.isLeft(key)) {
            this.direction = Direction.LEFT;
        } else if (this.control.isDown(key)) {
            this.direction = Direction.DOWN;
        } else if (this.control.isRight(key)) {
            this.direction = Direction.RIGHT;
        }
    }

    public void setDirection(final Direction direction) {
        this.direction = direction;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(final int x, final int y) {
        this.position.setCurrX(x);
        this.position.setCurrY(y);
    }

    public OwnedPokemon getOwnedPokemon() {
        return ownedPokemon;
    }

    public InventorySave getInventorySave() {
        return inventorySave;
    }
}
