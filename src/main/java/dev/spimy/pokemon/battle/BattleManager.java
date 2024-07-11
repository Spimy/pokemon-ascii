package dev.spimy.pokemon.battle;

import dev.spimy.pokemon.GameManager;
import dev.spimy.pokemon.State;
import dev.spimy.pokemon.battle.qte.*;
import dev.spimy.pokemon.player.Pokeball;
import dev.spimy.pokemon.player.controller.Direction;
import dev.spimy.pokemon.pokemon.Pokemon;
import dev.spimy.pokemon.saves.OwnedPokemon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class BattleManager {
    private final List<Pokemon> opponents;
    private final List<Pokemon> playerPokemons;
    private final List<Pokemon> caughtPokemons = new ArrayList<>();

    private final GameManager gameManager;

    private int numCrit = 0;
    private int numSuccessfulCatch = 0;
    private int turnsUntilCatchable = 0;

    public BattleManager(final GameManager gameManager) {
        this.gameManager = gameManager;

        // Select opponent Pokémons
        this.opponents = List.of(
                this.gameManager.getPokemonRepository().createRandomPokemon(),
                this.gameManager.getPokemonRepository().createRandomPokemon()
        );

        // Get Pokémons that the player owns
        final OwnedPokemon ownedPokemon = this.gameManager.getPlayer().getOwnedPokemon();

        // If player has now Pokémons, then they need to select a starter Pokémon
        boolean pickStarter = false;
        if (ownedPokemon.getData().isEmpty()) {
            pickStarter = true;
            this.gameManager.getPokemonRepository().getStarterPokemons().forEach(p -> ownedPokemon.getData().add(p));
        }

        // Display the Pokémons that can be selected
        ownedPokemon.tabulate();

        // Make a shallow copy of the Pokémons that can be selected so that manipulating it does not affect the player
        final List<Pokemon> playerPokemons = new ArrayList<>(ownedPokemon.getData());

        // QTE to choose their first Pokémon
        int pokemon1Index = new PokemonSelection(this.gameManager, 30, playerPokemons, pickStarter)
                .execute()
                .getPokemonIndex();
        final Pokemon pokemon1 = playerPokemons.get(pokemon1Index);

        System.out.println();
        playerPokemons.remove(pokemon1Index);

        // Second Pokémon is completely random from the remaining pool of Pokémons
        final Pokemon pokemon2 = playerPokemons.get(new Random().nextInt(playerPokemons.size()));

        // Set the selected Pokémons for battle
        this.playerPokemons = List.of(pokemon1, pokemon2);

        // If the user picked starter Pokémons then only the ones picked should be saved
        if (pickStarter) {
            ownedPokemon.getData().clear();
            ownedPokemon.addPokemon(pokemon1);
            ownedPokemon.addPokemon(pokemon2);
        }
    }

    public void startBattle() {
        this.battleLoop();
    }

    private void battleLoop() {
        while (this.gameManager.getState() == State.BATTLE) {
            this.displayStats();

            if (isBattleOver()) {
                this.handleBattleEnd();
                return;
            }

            final Optional<Pokemon> catchablePokemon = this.getCatchablePokemon();
            if (catchablePokemon.isPresent() && this.canCatch()) {
                final boolean isCatch = new ActionSelection(this.gameManager)
                        .execute()
                        .isCatch();

                if (isCatch) {
                    System.out.println("Catch");
                    this.catchPokemon(catchablePokemon.get());

                    System.out.println();
                    continue;
                }
            }

            if (this.turnsUntilCatchable > 0) this.turnsUntilCatchable--;
            this.battle();
            System.out.println();
        }
    }

    private void handleBattleEnd() {
        System.out.println();
        System.out.println("-".repeat(10));
        System.out.println();

        // Set battle score
        int battleScore = this.getBattleScore();
        this.gameManager.getScoreboard().addBattleScore(battleScore);
        this.gameManager.getScoreboard().updateSaveFile();

        System.out.printf("Battle Score earned: %s%n", battleScore);

        // Print battle status and calculate money earned based on the status
        final int money;
        if (allPlayerPokemonDead()) {
            money = (int) (battleScore * 0.005);
            System.out.println("You lost.");
        } else {
            money = (int) (battleScore * 0.05);
            System.out.println("You won.");
        }

        // Increase money
        this.gameManager.getPlayer().getInventorySave().getData().getFirst().setMoney(
            this.gameManager.getPlayer().getInventorySave().getData().getFirst().getMoney() + money
        );

        // Increase exp of player Pokémons
        int[] currentLevels = this.playerPokemons.stream().mapToInt(Pokemon::getLevel).toArray();
        for (int i = 0; i < this.playerPokemons.size(); i++) {
            this.playerPokemons.get(i).setExp(this.playerPokemons.get(i).getExp() + (int) (battleScore * 0.05));

            // Pokémon has levelled up, therefore enhance its stats
            if (this.playerPokemons.get(i).getLevel() > currentLevels[i]) {
                System.out.println();

                System.out.printf(
                        "%s has levelled up! Level %s -> %s%n",
                        this.playerPokemons.get(i).getName(),
                        currentLevels[i],
                        this.playerPokemons.get(i).getLevel()
                );
                System.out.println("Max stats have increase!");

                this.playerPokemons.get(i).setMaxHp((int) (this.playerPokemons.get(i).getMaxHp() * 1.05));
                this.playerPokemons.get(i).setAttackPower((int) (this.playerPokemons.get(i).getAttackPower() * 1.04));
                this.playerPokemons.get(i).setSpeed((int) (this.playerPokemons.get(i).getSpeed() * 1.03));
                this.playerPokemons.get(i).setCritRate(
                        Math.min((int) (this.playerPokemons.get(i).getCritRate() * 1.02), 100)
                );
            }
        }
        System.out.println();

        // Increase player exp
        // There is no need to handle levelling up as player level is only used to scale opponent Pokémons in battle
        this.gameManager.getPlayer().getInventorySave().getData().getFirst().setExp(
            this.gameManager.getPlayer().getInventorySave().getData().getFirst().getExp() + (int) (battleScore * 0.05)
        );

        // Transfer caught Pokémons to inventory
        this.caughtPokemons.forEach(p -> this.gameManager.getPlayer().getOwnedPokemon().addPokemon(p));
        this.gameManager.getPlayer().getOwnedPokemon().updateSaveFile();
        this.gameManager.getPlayer().getInventorySave().updateSaveFile();

        // Set the state to the battle end state
        this.gameManager.setState(State.BATTLEEND);
    }

    private boolean isBattleOver() {
        return this.caughtAndDeadPokemon() || this.allOpponentPokemonDead() || this.allPokemonCaught() || this.allPlayerPokemonDead();
    }

    private boolean allOpponentPokemonDead() {
        return this.opponents.stream().allMatch(o -> o.getCurrentHp() == 0);
    }

    private boolean allPokemonCaught() {
        return this.caughtPokemons.size() >= this.opponents.size();
    }

    private boolean allPlayerPokemonDead() {
        return this.playerPokemons.stream().allMatch(o -> o.getCurrentHp() == 0);
    }

    private boolean caughtAndDeadPokemon() {
        return this.opponents.stream().anyMatch(o -> o.getCurrentHp() == 0) && !this.caughtPokemons.isEmpty();
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

        System.out.println();
        System.out.printf("%s vs %s%n", playerPokemon.getName(), opponent.getName());

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
        final boolean isPlayer = this.playerPokemons.contains(from);

        // Only count if the Pokémon belongs to the player
        if (isCrit && isPlayer) {
            System.out.println("Critical hit!");
            this.numCrit++;
        }

        final int critical = isCrit ? 2 : 1;
        final int constant1 = 2;
        final int constant2 = 5;

        final double typeMultiplier;
        if (to.getType().getWeakAgainst().contains(from.getType())) {
            typeMultiplier = 1.5;
            if (isPlayer) System.out.println("It was very effective!");
        } else if (from.getType().getWeakAgainst().contains(to.getType())) {
            typeMultiplier = 0.5;
            if (isPlayer) System.out.println("It was not very effective!");
        } else {
            typeMultiplier = 1;
        }

        final double part1 = ((double) (constant1 * from.getLevel() * critical) / constant2) + constant1;
        final double part2 = ((part1 * from.getAttackPower()) / (constant2 * 10)) + constant1;
        final double damage = part2 * typeMultiplier * new Random().nextInt(1, 10);

        return (int) damage;
    }

    private Optional<Pokemon> getCatchablePokemon() {
        // If HP is below 50 then the Pokémon can be caught
        return this.opponents
                .stream()
                .filter(
                        o -> ((double) o.getCurrentHp() / o.getMaxHp() * 100) > 0
                                && ((double) o.getCurrentHp() / o.getMaxHp() * 100) < 50
                                && !this.caughtPokemons.contains(o)

                )
                .findFirst();
    }

    private boolean canCatch() {
        final boolean hasPokeballs = this.gameManager.getPlayer().getInventorySave().getTotalPokeballs() > 0;
        return hasPokeballs && this.turnsUntilCatchable == 0;
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

        this.gameManager.getPlayer().getInventorySave().getData().getFirst().getPokeballs().put(
                pokeball,
                this.gameManager.getPlayer().getInventorySave().getPokeballs().get(pokeball) - 1
        );
        System.out.printf("Selected: %s%n", pokeball.name);

        final boolean success = this.gameManager.getSuccessChance(pokeball.successRate);
        if (!success) {
            System.out.println("Failed to catch.");
            this.turnsUntilCatchable += 2;
            return;
        }

        System.out.println("Successfully caught.");

        this.numSuccessfulCatch++;
        this.caughtPokemons.add(pokemon);
    }

    /**
     * Formula:
     * BattleScore =
     *      MinScore +
     *      (P1_HP + P2_HP + [2 * (RAND(300, 501) - O1_HP / O1_MAX_HP)] + [RAND(300, 501) * (1 - O2_HP / O2_MAX_HP)]) +
     *      (NumCrit * ScorePerCrit) +
     *      (NumCatch * ScorePerCatch)
     *
     * @return the battle score earned for this battle
     */
    private int getBattleScore() {
        final int minScore = 1000;
        final int scorePerCrit = 500;
        final int scorePerCatch = 200;

        final Random random = new Random();

        final int playerPokemonTotalHp = this.playerPokemons
                .stream()
                .mapToInt(Pokemon::getCurrentHp)
                .reduce(0, Integer::sum);

        final double opponentHpPercentage = this.opponents
                .stream()
                .mapToDouble((p) -> random.nextInt(300, 501) * (1 - ((double) p.getCurrentHp() / p.getMaxHp())))
                .reduce(0, Double::sum);

        final int critScore = this.numCrit * scorePerCrit;
        final int catchScore = this.numSuccessfulCatch * scorePerCatch;

        return (int) (minScore + playerPokemonTotalHp + opponentHpPercentage + critScore + catchScore);
    }
}
