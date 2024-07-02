package dev.spimy.pokemon.battle.qte;

import dev.spimy.pokemon.GameManager;
import dev.spimy.pokemon.battle.BattleManager;

public class BattleActionSelection extends QuickTimeEvent<BattleActionSelection> {
    private boolean isDodge = false;

    public BattleActionSelection(final GameManager gameManager, final BattleManager battleManager) {
        super(gameManager, battleManager);
    }

    @Override
    public BattleActionSelection execute(final int eventTimeSeconds) {
        System.out.println("A: Continue | D: Attempt Dodge");

        while (System.currentTimeMillis() < this.startTime + (eventTimeSeconds * 1000L)) {
            if (this.gameManager.getControl().isLeft(qteActionKey)) {
                this.qteActive = false;
                this.isDodge = false;
            }

            if (this.gameManager.getControl().isRight(qteActionKey)) {
                this.qteActive = false;
                this.isDodge = true;
            }

            if (this.qteActive) continue;
            return this;
        }

        this.qteActive = false;
        this.isDodge = false;
        return this;
    }

    public boolean isDodge() {
        return this.isDodge;
    }
}
