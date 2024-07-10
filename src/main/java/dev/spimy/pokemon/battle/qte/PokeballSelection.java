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
        System.out.println("Press Enter to Select Pokéball");
        int index = 0;

        while (this.qteActive) {
            final List<Pokeball> pokeballs = this.gameManager.getPlayer().getInventory().keySet().stream().toList();
            this.pokeball = pokeballs.get(index);
            System.out.printf("%s ", this.pokeball);

            if (gameManager.getControl().isEnter(this.qteActionKey)) {
                this.qteActive = false;
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
