package dev.spimy.pokemon.battle.qte;

import dev.spimy.pokemon.GameManager;
import dev.spimy.pokemon.battle.BattleManager;
import dev.spimy.pokemon.player.controller.InputHandler;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Class to handle input from players
 * 'Enter' to stop wheel in randomizePokeBalls class
 * 'w' to charge up attack in chargeUpAttack class
 * 'a' to dodge to the left and 'd' to dodge to the right in dodgeAttack class
 * QTEActive to check whether any QTE is running
 * List of private enums to randomize the QTEs
 */

public abstract class QuickTimeEvent<T> {
    protected final Random random;
    protected final GameManager gameManager;
    protected final BattleManager battleManager;
    protected final long startTime = System.currentTimeMillis();

    protected boolean qteActive = true;
    protected volatile int qteActionKey; // Thank you, Lai, for teaching me about the volatile keyword

    private enum QTEType {
        SELECT_ACTION(5),
        CATCH_POKEMON(5),
        CHARGE_UP_ATTACK(10),
        DODGE_ATTACK(30);

        // In seconds
        public final int eventTime;

        QTEType(final int eventTime) {
            this.eventTime = eventTime;
        }
    }

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

//        switch (currentQTE) {
//            case QTEType.CATCH_POKEMON:
//                if (this.gameManager.getControl().isEnter(key)) {
//                    qteActive = false;
//                    int result = random.nextInt(100) + 1;
//                    if (result <= 15) {
//                        System.out.println("You got a Poké Ball!");
//                    } else if (result <= 45) {
//                        System.out.println("You got an Ultra Ball!");
//                    } else {
//                        System.out.println("You got a Master Ball!");
//                    }
//                }
//                break;
//
//            case QTEType.CHARGE_UP_ATTACK:
//                System.out.println("Tap 'W' Repeatedly to Charge Up");
//                if (this.gameManager.getControl().isUp(key)) {
//                    pressCount++;
//                    if (pressCount >= 20) {
//                        qteActive = false;
//                        System.out.println("Attack fully charged! Critical hit!"); // Perform critical hit (1.5x damage)
//                    }
//                }
//                break;
//
//            case QTEType.DODGE_ATTACK:
//                System.out.println("A: LEFT | D: RIGHT");
//                if (this.gameManager.getControl().isLeft(key)) {
//                    qteActive = false;
//                    System.out.println("Dodged left!"); // Perform left dodge
//                } else if (this.gameManager.getControl().isRight(key)) {
//                    qteActive = false;
//                    System.out.println("Dodged right!"); // Perform right dodge
//                }
//                break;
//        }
    }
}
