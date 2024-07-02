package dev.spimy.pokemon.battle.qte;

import dev.spimy.pokemon.GameManager;

public class DodgeAction extends QuickTimeEvent<DodgeAction> {
    private boolean dodgeLeft;

    public DodgeAction(final GameManager gameManager) {
        super(gameManager, 2);
    }

    @Override
    public DodgeAction execute() {
        System.out.println("A: Dodge Left | D: Dodge Right");

        while (System.currentTimeMillis() < this.endTime) {
            if (this.gameManager.getControl().isRight(qteActionKey)) {
                this.qteActive = false;
                this.dodgeLeft = false;
            }

            if (this.gameManager.getControl().isLeft(qteActionKey)) {
                this.qteActive = false;
                this.dodgeLeft = true;
            }

            if (this.qteActive) continue;
            return this;
        }

        this.qteActive = false;
        this.dodgeLeft = false;
        return this;
    }

    public boolean isDodgeLeft() {
        return this.dodgeLeft;
    }
}
