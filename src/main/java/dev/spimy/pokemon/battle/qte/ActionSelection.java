package dev.spimy.pokemon.battle.qte;

import dev.spimy.pokemon.GameManager;
import dev.spimy.pokemon.battle.BattleManager;

public class ActionSelection extends QuickTimeEvent<ActionSelection> {
    private boolean isBattle;

    public ActionSelection(final GameManager gameManager, final BattleManager battleManager) {
        super(gameManager, battleManager);
    }

    /**
     * @param eventTimeSeconds the amount of time this QTE can last for in seconds
     * @return the action selection instance
     */
    @Override
    public ActionSelection execute(final int eventTimeSeconds) {
        System.out.println("A: Battle | D: Catch");

        while (System.currentTimeMillis() < this.startTime + (eventTimeSeconds * 1000L)) {
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
