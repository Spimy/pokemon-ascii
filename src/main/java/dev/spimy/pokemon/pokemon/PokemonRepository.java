package dev.spimy.pokemon.pokemon;

import dev.spimy.pokemon.player.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PokemonRepository {
    private final List<String> pokemonMetadatas = new ArrayList<>();
    private final Player player;

    public PokemonRepository(final Player player) {
        this.player = player;

        try (final InputStream inputStream = getClass().getClassLoader().getResourceAsStream("pokemons.txt")) {
            if (inputStream == null) return;
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                this.pokemonMetadatas.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Pokemon createRandomPokemon() {
        final Random random = new Random();
        final int index = random.nextInt(this.pokemonMetadatas.size());

        final int playerExp = this.player.getInventorySave().getData().getFirst().getExp();
        final int playerLevel = this.player.getInventorySave().getData().getFirst().getLevel();

        final int levelRange = 2;
        final int range = playerExp - (250 * (playerLevel - levelRange - 1) * (playerLevel - levelRange));

        final String[] metadata = this.pokemonMetadatas.get(index).split(" : ");
        final int hp = random.nextInt(100, 501) * playerLevel;

        return new Pokemon(
            metadata[0],
            PokemonType.valueOf(metadata[1]),
            hp,
            hp,
            random.nextInt(100, 501) * (1 + ((double) playerLevel / 100)),
            random.nextInt(60, 201) * (1 + ((double) playerLevel / 100)),
            Math.min(random.nextInt(101) * (1 + (playerLevel / 100)), 100),
            Math.max(playerExp + random.nextInt(range * 2 + 1) - range, playerExp)
        );
    }

    public List<Pokemon> getStarterPokemons() {
        final List<String[]> starterMetadatas = this.pokemonMetadatas
                .stream()
                .filter(
                        metadata -> metadata
                                .split(" : ")[metadata.split(" : ").length - 1]
                                .equalsIgnoreCase("STARTER")
                )
                .map(metadata -> metadata.split(" : ")).toList();

        final Random random = new Random();
        return starterMetadatas.stream().map(d -> {
            final int hp = random.nextInt(300, 600);

            return new Pokemon(
                    d[0],
                    PokemonType.valueOf(d[1]),
                    hp,
                    hp,
                    random.nextInt(100, 501),
                    random.nextInt(60, 201),
                    random.nextInt(101),
                    random.nextInt(50)
            );
        }).toList();
    }
}
