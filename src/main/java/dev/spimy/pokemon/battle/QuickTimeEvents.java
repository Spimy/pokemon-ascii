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
    private Scanner scanner;
    private Random random;
    private GameManager gameManager;
    private BattleManager battleManager;
    private volatile boolean qteActive = false;
    private QTEType currentQTE;
    private int pressCount = 0;
    private long endTime;

    private enum QTEType {
        RANDOMIZE_POKEBALLS,
        CHARGE_UP_ATTACK,
        DODGE_ATTACK
    }

    public QuickTimeEvents(GameManager gameManager, BattleManager battleManager) {
        this.scanner = new Scanner(System.in);
        this.random = new Random();
        this.gameManager = gameManager;
        this.battleManager = battleManager;
        InputHandler.setQuickTimeEvents(this);
    }

    public void startQTE(QTEType qteType) {
        this.currentQTE = qteType;
        this.pressCount = 0;
        qteActive = true;
        long start = System.currentTimeMillis();
        switch (qteType) {
            case RANDOMIZE_POKEBALLS:
                endTime = start + (30 * 1000); // (seconds * 1000ms), 30 seconds
                System.out.println("Press 'Enter' to stop the wheel!");
                new randomizePokeballs().startQTE();
                break;
            case CHARGE_UP_ATTACK:
                endTime = start + (5 * 1000); // (seconds * 1000ms), 5 seconds
                System.out.println("Spam 'w' to charge up your attack");
                new chargeUpAttack().startQTE();
                break;
            case DODGE_ATTACK:
                endTime = start + (2 * 1000); // (seconds * 1000ms), 2 seconds
                System.out.println("Press 'Enter' to stop the wheel!");
                new dodgeAttack().startQTE();
                break;
        }
    }
    private class randomizePokeballs {
        public void startQTE() {
            while (System.currentTimeMillis() < endTime && qteActive) {
                // Checks for whether user has inputted the key
                // Idk if this works
                // Its the same
                if (scanner.hasNextLine()) {
                    handleInputs(scanner.nextLine().charAt(0));
                }
            }

            if (qteActive) {
                qteActive = false;
                System.out.println("Wheel was not stopped within allocated time.");
            }
        }
    }

    private class chargeUpAttack {
        public void startQTE() {
            while (System.currentTimeMillis() < endTime && qteActive) {
                if (scanner.hasNextLine()) {
                    handleInputs(scanner.nextLine().charAt(0));
                }
            }

            if (qteActive) {
                qteActive = false;
                System.out.println("Attack not fully charge. Normal Attack");
            }
        }
    }

    private class dodgeAttack {
        public void startQTE() {
            while (System.currentTimeMillis() < endTime && qteActive) {
                if (scanner.hasNextLine()) {
                    handleInputs(scanner.nextLine().charAt(0));
                }
            }

            if (qteActive) {
                qteActive = false;
                System.out.println("Attack not fully charge. Normal Attack");
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
                    // Random numbers in placed, idk how to code this
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
