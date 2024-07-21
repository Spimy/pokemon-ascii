package dev.spimy.pokemon.battle.qte;

import dev.spimy.pokemon.GameManager;
import dev.spimy.pokemon.pokemon.Pokemon;

import java.util.List;

public class PokemonSelection extends QuickTimeEvent<PokemonSelection> {
    private final List<Pokemon> pokemons;
    private final boolean pickStarter;

    private int pokemonIndex;

    public PokemonSelection(
            final GameManager gameManager,
            final List<Pokemon> pokemons,
            final boolean pickStarter
    ) {
        super(gameManager, 30);
        this.pokemons = pokemons;
        this.pickStarter = pickStarter;
    }

    @Override
    @SuppressWarnings("BusyWait")
    public PokemonSelection execute() {
        if (this.pickStarter) {
            System.out.println("Press Enter to Select Starter Pokémon. Do so wisely as your second starter will be completely random!");
        } else {
            System.out.println("Press Enter to Select Pokémon. Do so wisely as the second Pokémon will be completely random!");
        }

        int index = 0;
        final int longestWaitTime = 1000;
        final int shortestWaitTime = 50;
        final int waitTime = Math.max(longestWaitTime / this.pokemons.size(), shortestWaitTime);

        while (this.qteActive) {
            this.pokemonIndex = index;
            System.out.printf("%s ", index + 1);

            if (gameManager.getControl().isEnter(this.qteActionKey)) {
                this.qteActive = false;
            }

            index = (index + 1) % this.pokemons.size();

            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println();
        return this;
    }

    public int getPokemonIndex() {
        return this.pokemonIndex;
    }
}
