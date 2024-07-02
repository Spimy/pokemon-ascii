package dev.spimy.pokemon.battle;

import dev.spimy.pokemon.GameManager;
import dev.spimy.pokemon.State;
import dev.spimy.pokemon.battle.qte.ActionSelection;
import dev.spimy.pokemon.battle.qte.AttackAction;
import dev.spimy.pokemon.pokemon.Pokemon;
import dev.spimy.pokemon.pokemon.PokemonType;

import java.util.*;

public class BattleManager {

    // TODO: Switch int array to pokemon array, int for now is pokemon HP stat
    private final Pokemon[] opponents = new Pokemon[]{
            new Pokemon("Squirtle", PokemonType.WATER, 100, 100, 100, 100, "", 75, 200),
            new Pokemon("Charmander", PokemonType.FIRE, 100, 100, 100, 100, "", 75, 200)
    };
    private final Pokemon[] playerPokemons = new Pokemon[]{
            new Pokemon("Squirtle", PokemonType.WATER, 100, 100, 100, 100, "", 75, 200),
            new Pokemon("Charmander", PokemonType.FIRE, 100, 100, 100, 100, "", 75, 200)
    };

    // Stores the index of caught Pokémon where the index is the same as the one for array of opponents
    private final List<Pokemon> caughtPokemons = new ArrayList<>();

    private final GameManager gameManager;

    public BattleManager(final GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void startBattle() {
        this.battleLoop();
    }

    private void battleLoop() {
        while (this.gameManager.getState() == State.BATTLE) {
            System.out.printf(
                    "Opponent 1 HP: %s/%s%s%n",
                    this.opponents[0].getCurrentHp(),
                    this.opponents[0].getMaxHp(),
                    this.caughtPokemons
                            .stream()
                            .anyMatch(o -> o.getName().equals(this.opponents[0].getName())) ? "(caught)" : ""
            );
            System.out.printf("Opponent 2 HP: %s/%s%s%n",
                    this.opponents[1].getCurrentHp(),
                    this.opponents[1].getMaxHp(),
                    this.caughtPokemons
                            .stream()
                            .anyMatch(o -> o.getName().equals(this.opponents[1].getName())) ? "(caught)" : ""
            );
            System.out.printf("Player Pokemon 1 HP: %s/100%n", this.playerPokemons[0].getCurrentHp());
            System.out.printf("Player Pokemon 2 HP: %s/100%n", this.playerPokemons[1].getCurrentHp());

            final boolean caughtAndDeadPokemon = Arrays.stream(this.opponents)
                    .anyMatch(o -> o.getCurrentHp() == 0) && !this.caughtPokemons.isEmpty();
            final boolean allOpponentPokemonDead = Arrays.stream(this.opponents)
                    .allMatch(o -> o.getCurrentHp() == 0);
            final boolean allPokemonCaught = this.caughtPokemons.size() >= this.opponents.length;
            final boolean allPlayerPokemonDead = Arrays.stream(this.playerPokemons)
                    .allMatch(o -> o.getCurrentHp() == 0);

            // If all opponents Pokémon have 0 HP or if all player Pokémon have 0 HP, the battle is over
            if (caughtAndDeadPokemon || allOpponentPokemonDead || allPokemonCaught || allPlayerPokemonDead) {
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
            final Optional<Pokemon> catchablePokemon = Arrays.stream(this.opponents)
                    .filter(o -> o.getCurrentHp() > 0 && o.getCurrentHp() < 50)
                    .findFirst();

            if (catchablePokemon.isEmpty()) {
                this.battle();
                continue;
            }

            final boolean isBattle = new ActionSelection(this.gameManager, this)
                    .execute(5)
                    .isBattle();

            if (isBattle) {
                System.out.println("battle");
                this.battle();
                continue;
            }

            System.out.println("catch");
            this.catchPokemon(catchablePokemon.get());
        }
    }

    /**
     *
     */
    private void battle() {
        final Pokemon opponent = this.choosePokemon(this.opponents);
        final Pokemon playerPokemon = this.choosePokemon(this.playerPokemons);

        boolean attemptDodge = false;
        final int opponentIndex = opponent.getSpeed() > playerPokemon.getSpeed() ? 0 : 1;

        for (int i = 0; i < 2; i++) {
            if (i == opponentIndex) {
//                attemptDodge = true;
                final Random random = new Random();
                final int charge = random.nextInt(1, 100);
                this.attack(opponent, playerPokemon, charge);
            } else {
                if (attemptDodge) return;
                final AttackAction action = new AttackAction(this.gameManager, this).execute(2);
                final int charge = action.getCharged();
                this.attack(playerPokemon, opponent, charge);
            }
        }
    }

    private Pokemon choosePokemon(final Pokemon[] pokemonList) {
        final Random random = new Random();

        int target = -1;
        boolean isCaught = true;

        // If the selected Pokémon has fainted or was caught, select again
        while (target == -1 || pokemonList[target].getCurrentHp() == 0 || isCaught) {
            target = random.nextInt(0, pokemonList.length);
            isCaught = this.caughtPokemons.contains(pokemonList[target]);
        }

        return pokemonList[target];
    }

    /**
     * @param from   the attacking Pokémon
     * @param to     the target Pokémon
     * @param charge the amount the attacking Pokémon has charged up
     */
    private void attack(final Pokemon from, final Pokemon to, final int charge) {
        final int newHp = to.getCurrentHp() - this.getDamage(from, to, charge);
        to.setCurrentHp(Math.max(newHp, 0));
    }

    /**
     * A variation of the damage formula used by the classic Gen 1 Pokémon game.
     * <a href="https://m.bulbapedia.bulbagarden.net/wiki/Damage">Source</a>.
     *
     * @param from   the attacking Pokémon
     * @param to     the target Pokémon
     * @param charge the amount the attacking Pokémon has charged up
     * @return the amount of damage dealt
     */
    private int getDamage(final Pokemon from, final Pokemon to, final int charge) {
        final int extraCritThreshold = 80;
        final int extraCritRate = charge >= extraCritThreshold ? 15 : 0;
        final boolean isCrit = this.gameManager.getSuccessChance(from.getCritRate() + extraCritRate);

        final int critical = isCrit ? 2 : 1;
        final int constant1 = 2;
        final int constant2 = 5;

        final double typeMultiplier;
        if (to.getType().getWeakAgainst().contains(from.getType())) {
            typeMultiplier = 1.5;
        } else if (from.getType().getWeakAgainst().contains(to.getType())) {
            typeMultiplier = 0.5;
        } else {
            typeMultiplier = 1;
        }

        final double part1 = ((double) (constant1 * from.getLevel() * critical) / constant2) + constant1;
        final double part2 = ((part1 * from.getAttackPower()) / (constant2 * 10)) + constant1;
        final double damage = part2 * typeMultiplier * new Random().nextInt(1, 10);

        return (int) damage;
    }

    /**
     * Kept simple, not following the exact formula for catching.
     * Success chance rates:
     * - Poké Ball: 15%
     * - Ultra Ball: 30% (2x)
     * - Master Ball: 100%
     *
     * @param pokemon the index of the opponent Pokémon in the opponents array
     */
    private void catchPokemon(final Pokemon pokemon) {
        final boolean success = this.gameManager.getSuccessChance(10);
        if (!success) return;
        this.caughtPokemons.add(pokemon);
    }
}
