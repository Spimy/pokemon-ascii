package dev.spimy.pokemon.battle;

import dev.spimy.pokemon.GameManager;
import dev.spimy.pokemon.State;
import org.jline.utils.InfoCmp;

import java.util.*;

public class BattleManager {

    // TODO: Switch int array to pokemon array, int for now is pokemon HP stat
    private final Integer[] opponents = new Integer[]{100, 100};
    private final Integer[] playerPokemons = new Integer[]{100, 100};

    // Stores the index of caught Pokémon where the index is the same as the one for array of opponents
    private final List<Integer> caughtPokemons = new ArrayList<>();

    private final GameManager gameManager;

    public BattleManager(final GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void startBattle() {
        this.battleLoop();
    }

    @SuppressWarnings("BusyWait")
    private void battleLoop() {
        while (this.gameManager.getState() == State.BATTLE) {
            System.out.printf("Opponent 1 HP: %s/100%s%n", this.opponents[0], this.caughtPokemons.contains(0) ? "(caught)" : "");
            System.out.printf("Opponent 2 HP: %s/100%s%n", this.opponents[1], this.caughtPokemons.contains(1) ? "(caught)" : "");
            System.out.printf("Player Pokemon 1 HP: %s/100%n", this.playerPokemons[0]);
            System.out.printf("Player Pokemon 2 HP: %s/100%n", this.playerPokemons[1]);

            final boolean caughtAndDeadPokemon = Arrays.stream(this.opponents).anyMatch(o -> o == 0) && !this.caughtPokemons.isEmpty();
            final boolean allOpponentPokemonDead = Arrays.stream(this.opponents).allMatch(o -> o == 0);
            final boolean allPlayerPokemonDead = Arrays.stream(this.playerPokemons).allMatch(o -> o == 0);

            // If all opponents Pokémon have 0 HP or if all player Pokémon have 0 HP, the battle is over
            final boolean isBattleOver = caughtAndDeadPokemon || allOpponentPokemonDead || allPlayerPokemonDead;
            if (isBattleOver) {
                // TODO: Calculate battle points

                if (allPlayerPokemonDead) {
                    System.out.println("You lost.");
                } else {
                    System.out.println("You won.");
                }

                this.gameManager.setState(State.BATTLEEND);
                return;
            }

            // If HP is below 50 then the Pokémon can be caught
            final Optional<Integer> catchablePokemon = Arrays.stream(this.opponents).filter(o -> o > 0 && o < 50).findFirst();

            if (catchablePokemon.isEmpty()) {
                this.attack();
                continue;
            }

            // TODO: start QTE to choose between attack
            // This simulates the result of the QTE action for choosing attack or attempt catch
            final boolean isAttack = true;

            if (isAttack) {
                this.attack();
                continue;
            }

            this.catchPokemon(Arrays.asList(this.opponents).indexOf(catchablePokemon.get()));
        }
    }

    private void attack() {
        // TODO: randomly choose QTE action
        // Currently just does a random amount of damage
        final Random random = new Random();

        // The following damage calculation will be based on Pokémon speed

        // ---- DAMAGE CALCULATION FOR OPPONENT POKEMON ----
        int target = -1;
        while (target == -1 || this.opponents[target] == 0 || this.caughtPokemons.contains(target)) {
            target = random.nextInt(0, this.opponents.length);
        }

        // TODO: calculate damage based on player and opponent Pokémon type
        this.opponents[target] -= random.nextInt(0, this.opponents[target] + 1);

        // If all opponents are dead then player does not need to take damage
        if (Arrays.stream(this.opponents).allMatch(o -> o == 0)) return;

        // ---- DAMAGE CALCULATION FOR PLAYER POKEMON ----
        int playerTarget = random.nextInt(0, this.playerPokemons.length);
        while (playerTarget == -1 || this.playerPokemons[playerTarget] == 0 || this.caughtPokemons.contains(playerTarget)) {
            playerTarget = random.nextInt(0, this.playerPokemons.length);
        }

        // TODO: calculate damage based on player and opponent Pokémon type
        this.playerPokemons[playerTarget] -= random.nextInt(0, this.playerPokemons[playerTarget] + 1);
    }


    /**
     * Kept simple, not following the exact formula for catching.
     * Success chance rates:
     * - Poké Ball: 15%
     * - Great Ball: 22.5% (1.5x)
     * - Ultra Ball: 30% (2x)
     *
     * @param pokemonIndex the index of the opponent Pokémon in the opponents array
     */
    private void catchPokemon(final int pokemonIndex) {
        final boolean success = this.gameManager.getSuccessChance(10);
        if (!success) return;
        this.caughtPokemons.add(pokemonIndex);
    }
}
