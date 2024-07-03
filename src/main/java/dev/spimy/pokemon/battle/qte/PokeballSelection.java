package dev.spimy.pokemon.battle.qte;

import dev.spimy.pokemon.GameManager;
import dev.spimy.pokemon.player.Pokeball;

import java.util.List;

public class PokeballSelection extends QuickTimeEvent<PokeballSelection> {
    private Pokeball pokeball = Pokeball.NORMAL;

    public PokeballSelection(final GameManager gameManager) {
        super(gameManager, 30);
    }

    @Override
    @SuppressWarnings("BusyWait")
    public PokeballSelection execute() {
        int index = 0;

        while (qteActive) {
            final List<Pokeball> pokeballs = gameManager.getPlayer().getInventory().keySet().stream().toList();
            pokeball = pokeballs.get(index);
            System.out.printf("%s ", pokeball);

            if (gameManager.getControl().isEnter(qteActionKey)) {
                qteActive = false;
            }

            index = (index + 1) % pokeballs.size();

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println();
        return this;
    }

    public Pokeball getSelectedPokeball() {
        return this.pokeball;
    }
}
