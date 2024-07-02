package dev.spimy.pokemon.battle;

import dev.spimy.pokemon.GameManager;
import dev.spimy.pokemon.player.controller.InputHandler;

import java.util.Random;
import java.util.Scanner;

/**
 *  Class to handle input from players
 * 'Enter' to stop wheel in randomizePokeBalls class
 * 'w' to charge up attack in chargeUpAttack class
 * 'a' to dodge to the left and 'd' to dodge to the right in dodgeAttack class
 * QTEActive to check whether any QTE is running
 * List of private enums to randomize the QTEs
 */

public class QuickTimeEvents {
    private final Random random;
    private final GameManager gameManager;
    private final BattleManager battleManager;
    private boolean qteActive = false;
    private QTEType currentQTE;
    private int pressCount = 0;
    private long endTime;

    private enum QTEType {
        RANDOMIZE_POKEBALLS,
        CHARGE_UP_ATTACK,
        DODGE_ATTACK
    }

    public QuickTimeEvents(GameManager gameManager, BattleManager battleManager) {
        this.random = new Random();
        this.gameManager = gameManager;
        this.battleManager = battleManager;
        InputHandler.setQuickTimeEvents(this);
    }


    public void startBattleQTE(QTEType qteType) {
        // Dodge and Attack
        long start = System.currentTimeMillis();
        switch (qteType) {
            case DODGE_ATTACK:
                endTime = start + (5 * 1000); // (seconds * 1000ms), 5 seconds
                System.out.println("Press 'Enter' to stop the wheel!");
                if (System.currentTimeMillis() > endTime && qteActive) {
                    qteActive = false;
                    break;
                }

            case CHARGE_UP_ATTACK:
                endTime = start + (10 * 1000); // (seconds * 1000ms), 10 seconds
                System.out.println("Press 'Enter' to stop the wheel!");
                if (System.currentTimeMillis() > endTime && qteActive) {
                    qteActive = false;
                    break;
                }
        }
    }

    // Catching Pokemon
    public void catchPokemon(QTEType qteType) {
        long start = System.currentTimeMillis();
        switch (qteType) {
            case RANDOMIZE_POKEBALLS:
                endTime = start + (30 * 1000); // (seconds * 1000ms), 30 seconds
                System.out.println("Press 'Enter' to stop the wheel!");
                if (System.currentTimeMillis() > endTime && qteActive) {
                    qteActive = false;
                    break;
            }
        }
    }

    public void handleInputs(final int key) {
        if (!qteActive) {
            return;
        }

        switch (currentQTE) {
            case RANDOMIZE_POKEBALLS:
                if (this.gameManager.getControl().isEnter(key)) {
                    qteActive = false;
                    int result = random.nextInt(100) + 1;
                    if (result <= 15) {
                        System.out.println("You got a Poké Ball!");
                    } else if (result <= 45) {
                        System.out.println("You got an Ultra Ball!");
                    } else {
                        System.out.println("You got a Master Ball!");
                    }
                }
                break;

            case CHARGE_UP_ATTACK:
                if (this.gameManager.getControl().isUp(key)) {
                    pressCount++;
                    if (pressCount >= 20) {
                        qteActive = false;
                        System.out.println("Attack fully charged! Critical hit!"); // Perform critical hit (1.5x damage)
                    }
                }
                break;
            case DODGE_ATTACK:
                if (this.gameManager.getControl().isLeft(key)) {
                    qteActive = false;
                    System.out.println("Dodged left!"); // Perform left dodge
                } else if (this.gameManager.getControl().isRight(key)) {
                    qteActive = false;
                    System.out.println("Dodged right!"); // Perform right dodge
                }
                break;
        }
    }
}
