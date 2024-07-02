package dev.spimy.pokemon.battle.qte;

import dev.spimy.pokemon.GameManager;
import dev.spimy.pokemon.battle.BattleManager;

public class AttackAction extends QuickTimeEvent<AttackAction> {
    private int charged;

    public AttackAction(final GameManager gameManager, final BattleManager battleManager) {
        super(gameManager, battleManager);
    }

    @Override
    public AttackAction execute(final int eventTimeSeconds) {
        System.out.println("Tap 'W' Repeatedly to Charge Up");
        final int maxCharge = 100;

        while (System.currentTimeMillis() < this.startTime + (eventTimeSeconds * 1000L)) {
            if (this.gameManager.getControl().isUp(this.qteActionKey)) {
                final int amount = this.random.nextInt(1, 10);

                System.out.printf("%s -> ", this.charged);
                if (this.charged + amount >= maxCharge) {
                    this.charged = maxCharge;
                    this.qteActive = false;
                    System.out.println(this.charged);
                    return this;
                }

                this.charged += amount;
                this.qteActionKey = -1;
                System.out.println(this.charged);
            }
        }

        this.qteActive = false;
        return this;
    }

    public int getCharged() {
        return this.charged;
    }
}
