package dev.spimy.pokemon;

import dev.spimy.pokemon.battle.BattleManager;
import dev.spimy.pokemon.player.Inventory;
import dev.spimy.pokemon.player.Player;
import dev.spimy.pokemon.player.Pokeball;
import dev.spimy.pokemon.controller.Control;
import dev.spimy.pokemon.pokemon.PokemonRepository;
import dev.spimy.pokemon.player.saves.Scoreboard;
import dev.spimy.pokemon.screen.Renderer;
import dev.spimy.pokemon.screen.ascii.Ascii;
import dev.spimy.pokemon.screen.map.GameMap;
import dev.spimy.pokemon.screen.map.MapLayer;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GameManager {
    private final Terminal terminal;
    private final Control control;
    private final GameMap map;
    private final Scoreboard scoreboard;

    private Player player;
    private Renderer renderer;
    private State state;
    private PokemonRepository pokemonRepository;

    public GameManager(final State state, final Terminal terminal) {
        this.state = state;
        this.terminal = terminal;
        this.control = new Control();
        this.map = new GameMap();
        this.scoreboard = new Scoreboard();
    }

    private void setup() {
        this.terminal.enterRawMode();
        this.terminal.puts(InfoCmp.Capability.cursor_invisible);

        this.renderer = new Renderer(this, this.map);
        this.player = new Player(this.terminal, this.control, this.renderer, this.map);
        this.pokemonRepository = new PokemonRepository(this.player);
    }

    public void startGame() {
        this.setup();
        this.renderer.renderStartMenu();

        if (this.getState() != State.PLAY) {
            try {
                synchronized (this) {
                    this.wait();
                    this.setState(State.PLAY);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        this.gameLoop();
    }

    public Control getControl() {
        return this.control;
    }

    public Terminal getTerminal() {
        return this.terminal;
    }

    @SuppressWarnings({"InfiniteLoopStatement", "BusyWait"})
    private void gameLoop() {
        boolean cleared = false;

        while (true) {
            switch (this.getState()) {
                case State.PLAY -> {
                    if (!cleared) {
                        this.terminal.puts(InfoCmp.Capability.clear_screen);
                        cleared = true;
                    }

                    final String mapChar = this.player.getPosition().getMapChar();
                    if (mapChar != null && this.map.getWalls().contains(mapChar.charAt(0))) this.player.backtrack();

                    if (!this.renderer.isWithinBounds(this.player)) {
                        this.setState(State.OUTOFBOUNDS);
                        continue;
                    }

                    this.renderer.renderGame(this.player);
                }
                case State.PAUSE -> {
                    this.renderer.renderPauseMenu();
                    this.renderer.drawBorder();
                    cleared = false;
                }
                case State.OUTOFBOUNDS -> {
                    try {
                        this.renderer.renderDialogue(Ascii.getOutOfBoundDialogue());

                        Thread.sleep(2000);
                        cleared = false;

                        this.player.backtrack();

                        while (!this.renderer.isWithinBounds(this.player)) {
                            this.player.move();
                        }

                        this.player.move();

                        this.setState(State.PLAY);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                case State.BATTLE -> {
                    this.terminal.puts(InfoCmp.Capability.clear_screen);
                    new BattleManager(this).startBattle();
                }
                case State.BATTLEEND -> {
                    synchronized (this) {
                        try {
                            System.out.println("Press Enter/Return to continue...");
                            this.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    cleared = false;
                }
                case State.HEAL -> {
                    try {
                        this.renderer.renderDialogue(Ascii.getHealedDialogue());

                        Thread.sleep(2000);
                        cleared = false;

                        this.setState(State.PLAY);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public void handleInput(int key) {
        this.player.setDirection(key);
        this.player.move();
        this.renderer.renderPlayer(this.player);

        this.checkDoor();
        this.checkGrass();
        this.checkPurchase();
        this.checkHeal();
    }

    public void checkDoor() {
        final char selectedDoor = this.player.getPosition().getMapChar().charAt(0);
        if (!this.map.getDoors().containsKey(selectedDoor)) return;

        final MapLayer mapBeforeSet = this.map.getCurrentMap();
        if (mapBeforeSet != MapLayer.OVERWORLD && selectedDoor != MapLayer.OVERWORLD.doorChar) return;

        final boolean isSet = this.map.setCurrentMap(this.map.getDoors().get(selectedDoor));
        if (!isSet) return;

        this.terminal.puts(InfoCmp.Capability.clear_screen);

        // This is here to let the buffer update before proceeding
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        final HashMap<Character, Integer[]> doors = this.map.getDoorCoordinates(this.renderer.getBuffer());
        Integer[] coordinates;

        if (mapBeforeSet == MapLayer.OVERWORLD) {
            coordinates = doors.get(MapLayer.OVERWORLD.doorChar);
            coordinates = new Integer[]{coordinates[0] - 1, coordinates[1]};
        } else {
            coordinates = doors.get(mapBeforeSet.doorChar);
            coordinates = new Integer[]{coordinates[0] + 1, coordinates[1]};
        }

        this.player.setPosition(coordinates[0], coordinates[1]);
    }

    public void checkPurchase() {
        if (this.map.getCurrentMap() != MapLayer.STORE) return;

        final List<Character> validChars = List.of('P', 'U', 'M');
        final char selectedChar = this.player.getPosition().getMapChar().charAt(0);
        if (!validChars.contains(selectedChar)) return;

        final Inventory inventory = this.getPlayer().getInventorySave().getData().getFirst();
        switch (selectedChar) {
            case 'P' -> {
                if (inventory.getMoney() < 120) return;

                this.getPlayer().getInventorySave().getData().getFirst().setMoney(
                       inventory.getMoney() - 120
                );

                this.getPlayer().getInventorySave().getData().getFirst().getPokeballs().put(
                        Pokeball.NORMAL,
                        inventory.getPokeballs().get(Pokeball.NORMAL) + 1
                );
            }
            case 'U' -> {
                if (inventory.getMoney() < 180) return;

                this.getPlayer().getInventorySave().getData().getFirst().setMoney(
                        inventory.getMoney() - 180
                );

                this.getPlayer().getInventorySave().getData().getFirst().getPokeballs().put(
                        Pokeball.ULTRA,
                        inventory.getPokeballs().get(Pokeball.ULTRA) + 1
                );
            }
            case 'M' -> {
                if (inventory.getMoney() < 500) return;

                this.getPlayer().getInventorySave().getData().getFirst().setMoney(
                        inventory.getMoney() - 500
                );

                this.getPlayer().getInventorySave().getData().getFirst().getPokeballs().put(
                        Pokeball.MASTER,
                        inventory.getPokeballs().get(Pokeball.MASTER) + 1
                );
            }
        }

        this.getPlayer().getInventorySave().updateSaveFile();
    }

    public void checkHeal() {
        if (this.map.getCurrentMap() != MapLayer.CENTER) return;
        final char selectedChar = this.player.getPosition().getMapChar().charAt(0);
        if (selectedChar != '+') return;

        this.getPlayer().getOwnedPokemon().getData().forEach(p -> p.setCurrentHp(p.getMaxHp()));
        this.getPlayer().getOwnedPokemon().updateSaveFile();

        this.setState(State.HEAL);
    }

    public void checkGrass() {
        final String mapChar = this.player.getPosition().getMapChar();
        if (!mapChar.equals(this.map.getGrass())) return;

        final boolean foundWildPokemon = getSuccessChance(15);
        if (!foundWildPokemon) return;

        this.setState(State.BATTLE);
    }

    public State getState() {
        return this.state;
    }

    public void setState(final State state) {
        this.state = state;
    }

    public void quit() {
        System.out.print("\u001B[H\u001B[2J");
        System.out.print("\u001B[H\u001B[2J");
        System.exit(0);
    }

    public boolean getSuccessChance(final double percentageSuccess) {
        if (percentageSuccess <= 0) return false;
        if (percentageSuccess >= 100) return true;

        final Random random = new Random();
        final int percent = 100;
        return random.nextInt(0, percent + 1) > percent - percentageSuccess;
    }

    public Player getPlayer() {
        return player;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public PokemonRepository getPokemonRepository() {
        return pokemonRepository;
    }
}
