package dev.spimy.pokemon.battle.qte;

import dev.spimy.pokemon.GameManager;
import dev.spimy.pokemon.battle.BattleManager;
import dev.spimy.pokemon.player.controller.InputHandler;

import java.util.Random;

public abstract class QuickTimeEvent<T> {
    protected final Random random;
    protected final GameManager gameManager;
    protected final BattleManager battleManager;
    protected final long startTime = System.currentTimeMillis();

    protected boolean qteActive = true;
    protected volatile int qteActionKey; // Thank you, Lai, for teaching me about the volatile keyword

    public QuickTimeEvent(final GameManager gameManager, final BattleManager battleManager) {
        this.random = new Random();
        this.gameManager = gameManager;
        this.battleManager = battleManager;
        InputHandler.setQuickTimeEvents(this);
    }

    public abstract T execute(final int eventTimeSeconds);

    public void handleInputs(final int key) {
        if (!qteActive) {
            InputHandler.setQuickTimeEvents(null);
            return;
        }
        this.qteActionKey = key;
    }
}
