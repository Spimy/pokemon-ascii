package dev.spimy.pokemon;

import dev.spimy.pokemon.player.controller.InputHandler;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try (Terminal terminal = TerminalBuilder.builder().system(true).dumb(false).build()) {
            final GameManager gameManager = new GameManager(State.FIRST, terminal);
            final Thread inputHandler = new Thread(new InputHandler(gameManager));
            inputHandler.start();
            gameManager.startGame();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}