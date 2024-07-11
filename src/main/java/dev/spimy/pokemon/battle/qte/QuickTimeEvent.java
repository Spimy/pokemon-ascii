package dev.spimy.pokemon.battle.qte;

import dev.spimy.pokemon.GameManager;
import dev.spimy.pokemon.player.controller.InputHandler;

import java.util.Random;

public abstract class QuickTimeEvent<T> {
    protected final Random random;
    protected final GameManager gameManager;
    protected final long endTime;

    protected boolean qteActive = true;
    protected volatile int qteActionKey; // Thank you, Lai, for teaching me about the volatile keyword

    public QuickTimeEvent(final GameManager gameManager, final int eventTimeSeconds) {
        this.random = new Random();
        this.gameManager = gameManager;
        this.endTime = System.currentTimeMillis() + (eventTimeSeconds * 1000L);
        InputHandler.setQuickTimeEvents(this);
    }

    @SuppressWarnings("unused")
    public abstract T execute();

    public void handleInputs(final int key) {
        if (!qteActive) {
            InputHandler.setQuickTimeEvents(null);
            return;
        }
        this.qteActionKey = key;
    }
}
