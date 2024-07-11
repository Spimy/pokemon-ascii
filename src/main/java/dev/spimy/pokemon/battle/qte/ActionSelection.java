package dev.spimy.pokemon.battle.qte;

import dev.spimy.pokemon.GameManager;

public class ActionSelection extends QuickTimeEvent<ActionSelection> {
    private boolean isCatch;

    public ActionSelection(final GameManager gameManager) {
        super(gameManager, 8);
    }

    @Override
    public ActionSelection execute() {
        System.out.println("A: Battle | D: Catch");

        while (System.currentTimeMillis() < this.endTime) {
            if (this.gameManager.getControl().isLeft(this.qteActionKey)) {
                this.qteActive = false;
                this.isCatch = false;
            }

            if (this.gameManager.getControl().isRight(this.qteActionKey)) {
                this.qteActive = false;
                this.isCatch = true;
            }

            if (this.qteActive) continue;
            return this;
        }

        this.qteActive = false;
        this.isCatch = false;
        return this;
    }

    public boolean isCatch() {
        return this.isCatch;
    }
}
