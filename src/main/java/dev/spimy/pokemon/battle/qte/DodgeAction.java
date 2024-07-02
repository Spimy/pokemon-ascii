package dev.spimy.pokemon.battle.qte;

import dev.spimy.pokemon.GameManager;
import dev.spimy.pokemon.battle.BattleManager;

public class DodgeAction extends QuickTimeEvent<DodgeAction> {
    private boolean dodgeLeft;

    public DodgeAction(final GameManager gameManager, final BattleManager battleManager) {
        super(gameManager, battleManager);
    }

    @Override
    public DodgeAction execute(final int eventTimeSeconds) {
        System.out.println("A: Dodge Left | D: Dodge Right");

        while (System.currentTimeMillis() < this.startTime + (eventTimeSeconds * 1000L)) {
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
