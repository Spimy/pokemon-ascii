package dev.spimy.pokemon.player.saves;

import dev.spimy.pokemon.pokemon.Pokemon;
import dev.spimy.pokemon.pokemon.PokemonType;

public class OwnedPokemon extends SaveFileHandler<Pokemon> {
    public OwnedPokemon() {
        super("pokemons", new String[]{
            "Name", "Type", "Max HP", "Current HP", "Attack Power", "Speed", "Crit Rate", "Exp"
        });
    }

    @Override
    protected Pokemon parseData(final String[] rawDataRow) {
        try {
            return new Pokemon(
                rawDataRow[0],
                PokemonType.valueOf(rawDataRow[1]),
                Integer.parseInt(rawDataRow[2]),
                Integer.parseInt(rawDataRow[3]),
                Integer.parseInt(rawDataRow[4]),
                Integer.parseInt(rawDataRow[5]),
                Integer.parseInt(rawDataRow[6]),
                Integer.parseInt(rawDataRow[7])
            );

        } catch (NumberFormatException _) {
            return null;
        }
    }

    @Override
    protected String[] createColumns(final Pokemon pokemon) {
        return pokemon.toString().split(",");
    }

    public void addPokemon(final Pokemon pokemon) {
        this.getData().add(pokemon);
    }
}
