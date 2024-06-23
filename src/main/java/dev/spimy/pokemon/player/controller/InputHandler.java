package dev.spimy.pokemon.player.controller;

import dev.spimy.pokemon.GameManager;
import dev.spimy.pokemon.State;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;

import java.io.IOException;

public class InputHandler implements Runnable {
    private final GameManager gameManager;
    private final NonBlockingReader keyReader;
    private final Control control;

    public InputHandler(GameManager gameManager) {
        this.gameManager = gameManager;

        final Terminal terminal = this.gameManager.getTerminal();

        this.control = this.gameManager.getControl();
        this.keyReader = terminal.reader();
    }

    @Override
    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        int key;
        try {
            while (true) {
                key = keyReader.read();
                synchronized (gameManager) {
                    switch(gameManager.getState()) {
                        case State.FIRST -> this.handleFirstStateInput(key);
                        case State.PLAY -> this.handlePlayStateInput(key);
                        case State.GAMEOVER -> this.handleGameOverStateInput(key);
                        case State.PAUSE -> this.handlePauseStateInput(key);
                    }
                }
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleFirstStateInput(int key) {
        if (control.isPlay(key)) {
            gameManager.notify();
            return;
        }

        if (control.isQuit(key)) {
            gameManager.quit();
        }
    }

    private void handlePlayStateInput(int key) {
        if (control.isPlay(key)) {
            synchronized (gameManager) {
                gameManager.setState(gameManager.getState() == State.PAUSE ? State.PLAY : State.PAUSE);
            }
            return;
        }

        if (control.isQuit(key)) {
            gameManager.quit();
            return;
        }

        gameManager.handleInput(key);
    }

    private void handleGameOverStateInput(int key) {
        if (control.isPlay(key)) {
            gameManager.setState(State.FIRST);
            gameManager.notify();
            return;
        }

        if (control.isQuit(key)) {
            gameManager.quit();
        }
    }

    private void handlePauseStateInput(int key) {
        if (control.isPlay(key)) {
            gameManager.setState(State.PLAY);
            gameManager.notify();
            return;
        }

        if (control.isQuit(key)) {
            gameManager.quit();
        }
    }
}
