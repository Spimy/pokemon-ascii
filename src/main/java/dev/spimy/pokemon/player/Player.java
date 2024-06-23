package dev.spimy.pokemon.player;

import dev.spimy.pokemon.player.controller.Control;
import dev.spimy.pokemon.player.controller.Direction;
import dev.spimy.pokemon.screen.Theme;
import org.jline.jansi.Ansi;
import org.jline.terminal.Terminal;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Player {
    private final Control control;
    private final ArrayList<Ansi.Color> theme;

    public Position position;
    private Direction direction;

    public Player(Terminal terminal, Control control, Theme theme) {
        this.control = control;
        this.theme = theme.getTheme();

        final int height = terminal.getHeight();
        final int width = terminal.getWidth();

        this.position = new Position(height / 2, width / 2);
        this.direction = Direction.values()[ThreadLocalRandom.current().nextInt(Direction.values().length)];
    }

    public ArrayList<Ansi.Color> getTheme() {
        return theme;
    }

    public void move() {
        position.setPrevX(position.getCurrX());
        position.setPrevY(position.getCurrY());
        switch (this.direction) {
            case UP -> position.setCurrX(position.getCurrX() - 1);
            case DOWN -> position.setCurrX(position.getCurrX() + 1);
            case LEFT -> position.setCurrY(position.getCurrY() - 1);
            case RIGHT -> position.setCurrY(position.getCurrY() + 1);
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
        if (control.isUp(key)) {
            this.direction = Direction.UP;
        } else if (control.isLeft(key)) {
            this.direction = Direction.LEFT;
        } else if (control.isDown(key)) {
            this.direction = Direction.DOWN;
        } else if (control.isRight(key)) {
            this.direction = Direction.RIGHT;
        }
    }

    public void setDirection(final Direction direction) {
        this.direction = direction;
    }
}
