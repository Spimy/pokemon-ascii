package dev.spimy.pokemon.controller;

import dev.spimy.pokemon.GameManager;
import dev.spimy.pokemon.State;
import dev.spimy.pokemon.battle.qte.QuickTimeEvent;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;

import java.io.IOException;

public class InputHandler implements Runnable {
    private final GameManager gameManager;
    private final Terminal terminal;
    private final Control control;

    private static QuickTimeEvent<?> quickTimeEvent;

    public InputHandler(final GameManager gameManager) {
        this.gameManager = gameManager;
        this.terminal = this.gameManager.getTerminal();
        this.control = this.gameManager.getControl();
    }

    @Override
    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        int key;
        final NonBlockingReader keyReader = this.terminal.reader();

        try {
            while (true) {
                key = keyReader.read();
                synchronized (this.gameManager) {
                    switch(this.gameManager.getState()) {
                        case State.FIRST -> this.handleFirstStateInput(key);
                        case State.PLAY -> this.handlePlayStateInput(key);
                        case State.PAUSE -> this.handlePauseStateInput(key);
                        case State.BATTLEEND -> this.handleBattleEndStateInput(key);
                        case State.BATTLE -> {
                            if (quickTimeEvent != null) quickTimeEvent.handleInputs(key);
                        }

                    }

                    if (this.control.isQuit(key)) {
                        this.gameManager.quit();
                    }
                }
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleFirstStateInput(final int key) {
        if (this.control.isPlay(key)) {
            this.gameManager.notify();
        }
    }

    private void handlePlayStateInput(final int key) {
        if (this.control.isPlay(key)) {
            synchronized (this.gameManager) {
                this.gameManager.setState(this.gameManager.getState() == State.PAUSE ? State.PLAY : State.PAUSE);
            }
            return;
        }

        this.gameManager.handleInput(key);
    }

    private void handlePauseStateInput(final int key) {
        if (this.control.isPlay(key)) {
            this.gameManager.setState(State.PLAY);
            this.gameManager.notify();
        }
    }

    private void handleBattleEndStateInput(final int key) {
        if (this.control.isEnter(key)) {
            this.gameManager.setState(State.PLAY);
            this.gameManager.notify();
        }
    }

    public static void setQuickTimeEvents(QuickTimeEvent<?> quickTimeEvent) {
        InputHandler.quickTimeEvent = quickTimeEvent;
    }

}
