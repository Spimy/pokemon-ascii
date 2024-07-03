package dev.spimy.pokemon.battle.qte;

import dev.spimy.pokemon.GameManager;

public class AttackAction extends QuickTimeEvent<AttackAction> {
    private int charged;

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

                System.out.printf("%s -> ", this.charged);
                if (this.charged + amount >= maxCharge) {
                    this.charged = maxCharge;
                    this.qteActive = false;
                    System.out.println(this.charged);
                    return this;
                }

                this.charged += amount;
                this.qteActionKey = -1;
                System.out.printf("%s -> ", this.charged);
            }
        }

        if (this.charged > 0) System.out.println(this.charged);
        this.qteActive = false;
        return this;
    }

    public int getCharged() {
        return this.charged;
    }
}
