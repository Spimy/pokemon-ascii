package dev.spimy.pokemon.battle.qte;

import dev.spimy.pokemon.GameManager;
import dev.spimy.pokemon.controller.InputHandler;

import java.util.Random;

public abstract class QuickTimeEvent<T> {
    protected final Random random;
    protected final GameManager gameManager;
    protected long endTime;

    protected boolean qteActive = true;
    protected volatile int qteActionKey; // Thank you, Lai, for teaching me about the volatile keyword

    public QuickTimeEvent(final GameManager gameManager, final int eventTimeSeconds) {
        this.random = new Random();
        this.gameManager = gameManager;
        this.endTime = System.currentTimeMillis() + (eventTimeSeconds * 1000L);
        InputHandler.setQuickTimeEvents(this);
    }

    public QuickTimeEvent<T> execute() {
        System.out.println();
        System.out.print("Action will start in: ");
        this.endTime += 3000L;

        for (int i = 3; i > 0; i--) {
            System.out.printf("%s ", i);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println();
        System.out.println();

        return this;
    }

    public void handleInputs(final int key) {
        if (!qteActive) {
            InputHandler.setQuickTimeEvents(null);
            return;
        }
        this.qteActionKey = key;
    }
}
