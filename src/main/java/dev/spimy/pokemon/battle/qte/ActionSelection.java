package dev.spimy.pokemon.battle.qte;

import dev.spimy.pokemon.GameManager;

public class ActionSelection extends QuickTimeEvent<ActionSelection> {
    private boolean isBattle;

    public ActionSelection(final GameManager gameManager) {
        super(gameManager, 5);
    }

    @Override
    public ActionSelection execute() {
        System.out.println("A: Battle | D: Catch");

        while (System.currentTimeMillis() < this.endTime) {
            if (this.gameManager.getControl().isLeft(qteActionKey)) {
                this.qteActive = false;
                this.isBattle = true;
            }

            if (this.gameManager.getControl().isRight(qteActionKey)) {
                this.qteActive = false;
                this.isBattle = false;
            }

            if (this.qteActive) continue;
            return this;
        }

        this.qteActive = false;
        this.isBattle = true;
        return this;
    }

    public boolean isBattle() {
        return this.isBattle;
    }
}
