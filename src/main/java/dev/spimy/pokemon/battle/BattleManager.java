package dev.spimy.pokemon.battle;

import dev.spimy.pokemon.GameManager;
import dev.spimy.pokemon.State;
import dev.spimy.pokemon.battle.qte.*;
import dev.spimy.pokemon.player.Pokeball;
import dev.spimy.pokemon.player.controller.Direction;
import dev.spimy.pokemon.pokemon.Pokemon;
import dev.spimy.pokemon.pokemon.PokemonType;

import java.util.*;

public class BattleManager {
    private final List<Pokemon> opponents = List.of(
            new Pokemon("Golduck", PokemonType.WATER, 321, 321, 210, 100, "", 69, 200),
            new Pokemon("Flareon", PokemonType.FIRE, 524, 524, 124, 120, "", 32, 200)
    );
    private final List<Pokemon> playerPokemons = List.of(
            new Pokemon("Squirtle", PokemonType.WATER, 428, 428, 144, 105, "", 75, 200),
            new Pokemon("Charmander", PokemonType.FIRE, 232, 232, 269, 130, "", 47, 200)
    );

    private final List<Pokemon> caughtPokemons = new ArrayList<>();
    private final GameManager gameManager;

    private int numCrit = 0;
    private int numSuccessfulCatch = 0;

    public BattleManager(final GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void startBattle() {
        this.battleLoop();
    }

    private void battleLoop() {
        while (this.gameManager.getState() == State.BATTLE) {
            this.displayStats();

            final boolean allOpponentPokemonDead = this.opponents.stream().allMatch(o -> o.getCurrentHp() == 0);
            final boolean allPokemonCaught = this.caughtPokemons.size() >= this.opponents.size();
            final boolean allPlayerPokemonDead = this.playerPokemons.stream().allMatch(o -> o.getCurrentHp() == 0);
            final boolean caughtAndDeadPokemon =
                    this.opponents.stream().anyMatch(o -> o.getCurrentHp() == 0) && !this.caughtPokemons.isEmpty();

            // If all opponents Pokémon have 0 HP or if all player Pokémon have 0 HP, the battle is over
            if (caughtAndDeadPokemon || allOpponentPokemonDead || allPokemonCaught || allPlayerPokemonDead) {
                System.out.println();

                final int battlePoints = this.getBattlePoints();
                this.gameManager.getScoreboard().addScore(battlePoints);
                this.gameManager.getScoreboard().updateSaveFile();

                System.out.printf("Battle Points earned: %s%n", battlePoints);

                if (allPlayerPokemonDead) {
                    System.out.println("You lost.");
                } else {
                    System.out.println("You won.");
                }

                this.gameManager.setState(State.BATTLEEND);
                return;
            }

            // If HP is below 50 then the Pokémon can be caught
            final Optional<Pokemon> catchablePokemon = this.opponents
                    .stream()
                    .filter(
                            o -> ((double) o.getCurrentHp() / o.getMaxHp() * 100) > 0
                                    && ((double) o.getCurrentHp() / o.getMaxHp() * 100) < 50
                                    && !this.caughtPokemons.contains(o)

                    )
                    .findFirst();

            if (catchablePokemon.isEmpty()) {
                this.battle();
                System.out.println();
                continue;
            }

            final boolean isBattle = new ActionSelection(this.gameManager)
                    .execute()
                    .isBattle();

            if (isBattle) {
                System.out.println("battle");
                this.battle();
                System.out.println();
                continue;
            }

            System.out.println("catch");
            this.catchPokemon(catchablePokemon.get());
            System.out.println();
        }
    }

    private void displayStats() {
        System.out.printf(
                "Opponent 1 (%s) HP: %s/%s%s%n",
                this.opponents.getFirst().getName(),
                this.opponents.getFirst().getCurrentHp(),
                this.opponents.getFirst().getMaxHp(),
                this.caughtPokemons.contains(this.opponents.getFirst()) ? " (caught)" : ""
        );
        System.out.printf(
                "Opponent 2 (%s) HP: %s/%s%s%n",
                this.opponents.get(1).getName(),
                this.opponents.get(1).getCurrentHp(),
                this.opponents.get(1).getMaxHp(),
                this.caughtPokemons.contains(this.opponents.get(1)) ? " (caught)" : ""
        );
        System.out.printf(
                "Player Pokemon 1 (%s) HP: %s/%s%n",
                this.playerPokemons.getFirst().getName(),
                this.playerPokemons.getFirst().getCurrentHp(),
                this.playerPokemons.getFirst().getMaxHp()
        );
        System.out.printf(
                "Player Pokemon 2 (%s) HP: %s/%s%n",
                this.playerPokemons.get(1).getName(),
                this.playerPokemons.get(1).getCurrentHp(),
                this.playerPokemons.get(1).getMaxHp()
        );
    }

    /**
     * If player goes second, then the player can only dodge or ATK
     * If player goes first, then the player can ATK first and then dodge the opponent ATK
     */
    private void battle() {
        final Pokemon opponent = this.choosePokemon(this.opponents);
        final Pokemon playerPokemon = this.choosePokemon(this.playerPokemons);
        final int chargeToAttack = 20;

        boolean attemptDodge;
        final int opponentIndex = opponent.getSpeed() > playerPokemon.getSpeed() ? 0 : 1;

        for (int i = 0; i < 2; i++) {
            if (i == opponentIndex) {
                if (opponent.getCurrentHp() == 0) continue;

                final Random random = new Random();
                final int charge = random.nextInt(chargeToAttack, 100);

                attemptDodge = new BattleActionSelection(this.gameManager)
                        .execute()
                        .isDodge();

                if (!attemptDodge) {
                    this.attack(opponent, playerPokemon, charge);
                    continue;
                }

                final Direction attackDirection =
                        this.gameManager.getSuccessChance(50) ?
                                Direction.LEFT :
                                Direction.RIGHT;

                final Direction dodgeDirection = new DodgeAction(this.gameManager)
                        .execute()
                        .getDirection();

                if (attackDirection == dodgeDirection) {
                    System.out.println("Dodge failed.");
                    this.attack(opponent, playerPokemon, charge);
                    break;
                }

                System.out.println("Successfully dodged.");
                break;
            } else {
                if (playerPokemon.getCurrentHp() == 0) continue;

                final AttackAction action = new AttackAction(this.gameManager).execute();
                final int charge = action.getCharged();

                if (charge < chargeToAttack) {
                    System.out.println("Attack missed.");
                    continue;
                }

                this.attack(playerPokemon, opponent, charge);
            }
        }
    }

    private Pokemon choosePokemon(final List<Pokemon> pokemonList) {
        final Random random = new Random();

        int target = -1;
        boolean isCaught = true;

        // If the selected Pokémon has fainted or was caught, select again
        while (target == -1 || pokemonList.get(target).getCurrentHp() == 0 || isCaught) {
            target = random.nextInt(0, pokemonList.size());
            isCaught = this.caughtPokemons.contains(pokemonList.get(target));
        }

        return pokemonList.get(target);
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

        // Only count if the Pokémon belongs to the player
        if (isCrit && this.playerPokemons.contains(from)) this.numCrit++;

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
     * @param pokemon one of the Pokémon in the opponents array
     */
    private void catchPokemon(final Pokemon pokemon) {
        final Pokeball pokeball = new PokeballSelection(this.gameManager).execute().getSelectedPokeball();
        this.gameManager.getPlayer().getInventory().put(
                pokeball,
                this.gameManager.getPlayer().getInventory().get(pokeball) - 1
        );
        System.out.printf("Selected: %s%n", pokeball.name);

        final boolean success = this.gameManager.getSuccessChance(pokeball.successRate);
        if (!success) return;

        System.out.println("Successfully caught.");

        this.numSuccessfulCatch++;
        this.caughtPokemons.add(pokemon);
    }

    /**
     * Formula:
     * BattlePoints =
     * MinPoint +
     * (P1_HP + P2_HP + [2 * (1 - O1_HP / O1_MAX_HP)] + [2 * (1 - O2_HP / O2_MAX_HP)]) +
     * (NumCrit * PointsPerCrit) +
     * (NumCatch * PointsPerCatch)
     *
     * @return the battle points earned for this battle
     */
    private int getBattlePoints() {
        final int minPoint = 1000;
        final int pointsPerCrit = 500;
        final int pointsPerCatch = 200;

        final int playerPokemonTotalHp = this.playerPokemons
                .stream()
                .mapToInt(Pokemon::getCurrentHp)
                .reduce(0, Integer::sum);

        final double opponentHpPercentage = this.opponents
                .stream()
                .mapToDouble((p) -> 2 * (1 - ((double) p.getCurrentHp() / p.getMaxHp())))
                .reduce(0, Double::sum);

        final int critPoints = this.numCrit * pointsPerCrit;
        final int catchPoints = this.numSuccessfulCatch * pointsPerCatch;

        return (int) (minPoint + playerPokemonTotalHp + opponentHpPercentage + critPoints + catchPoints);
    }
}
