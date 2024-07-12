package dev.spimy.pokemon.battle.qte;

import dev.spimy.pokemon.GameManager;

public class BattleActionSelection extends QuickTimeEvent<BattleActionSelection> {
    private boolean isDodge = false;

    public BattleActionSelection(final GameManager gameManager) {
        super(gameManager, 2);
    }

    @Override
    public BattleActionSelection execute() {
        super.execute();
        System.out.println("A: Continue | D: Attempt Dodge");

        while (System.currentTimeMillis() < this.endTime) {
            if (this.gameManager.getControl().isLeft(this.qteActionKey)) {
                this.qteActive = false;
                this.isDodge = false;
            }

            if (this.gameManager.getControl().isRight(this.qteActionKey)) {
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
