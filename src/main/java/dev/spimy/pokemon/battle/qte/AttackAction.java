package dev.spimy.pokemon.battle.qte;

import dev.spimy.pokemon.GameManager;

public class AttackAction extends QuickTimeEvent<AttackAction> {
    private int charge;

    public AttackAction(final GameManager gameManager) {
        super(gameManager, 2);
    }

    @Override
    public AttackAction execute() {
        System.out.println("Tap 'W' Repeatedly to Charge Up");
        final int maxCharge = 100;

        while (System.currentTimeMillis() < this.endTime) {
            if (this.gameManager.getControl().isUp(this.qteActionKey)) {
                final int amount = this.random.nextInt(1, 10);

                System.out.printf("%s -> ", this.charge);
                if (this.charge + amount >= maxCharge) {
                    this.charge = maxCharge;
                    this.qteActive = false;
                    System.out.println(this.charge);
                    return this;
                }

                this.charge += amount;
                this.qteActionKey = -1;
            }
        }

        if (this.charge > 0) System.out.println(this.charge);
        this.qteActive = false;
        return this;
    }

    public int getCharge() {
        return this.charge;
    }
}
