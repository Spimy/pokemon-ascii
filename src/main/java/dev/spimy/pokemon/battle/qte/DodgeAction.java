package dev.spimy.pokemon.battle.qte;

import dev.spimy.pokemon.GameManager;
import dev.spimy.pokemon.controller.Direction;

public class DodgeAction extends QuickTimeEvent<DodgeAction> {
    private Direction direction;

    public DodgeAction(final GameManager gameManager) {
        super(gameManager, 1);
    }

    @Override
    public DodgeAction execute() {
        System.out.println("A: Dodge Left | D: Dodge Right");

        while (System.currentTimeMillis() < this.endTime) {
            if (this.gameManager.getControl().isRight(this.qteActionKey)) {
                this.qteActive = false;
                this.direction = Direction.RIGHT;
            }

            if (this.gameManager.getControl().isLeft(this.qteActionKey)) {
                this.qteActive = false;
                this.direction = Direction.LEFT;
            }

            if (this.qteActive) continue;
            return this;
        }

        this.qteActive = false;

        final Direction[] possibleDirections = new Direction[]{Direction.LEFT, Direction.RIGHT};
        this.direction = possibleDirections[this.random.nextInt(0, possibleDirections.length)];

        return this;
    }

    public Direction getDirection() {
        return this.direction;
    }
}
