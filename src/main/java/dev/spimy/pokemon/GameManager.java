package dev.spimy.pokemon;

import dev.spimy.pokemon.player.Player;
import dev.spimy.pokemon.player.Position;
import dev.spimy.pokemon.player.controller.Control;
import dev.spimy.pokemon.screen.Renderer;
import dev.spimy.pokemon.screen.Theme;
import dev.spimy.pokemon.screen.map.GameMap;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

public class GameManager {

    final private Terminal terminal;
    final private Control control;
    final private GameMap map;

    private Player player;
    private Renderer renderer;
    private State state;

    public GameManager(final State state, final Terminal terminal) {
        this.state = state;
        this.terminal = terminal;
        this.control = new Control();
        this.map = new GameMap();
    }

    private void setup() {
        this.terminal.enterRawMode();
        this.terminal.puts(InfoCmp.Capability.cursor_invisible);

        final Theme theme = new Theme();
        this.renderer = new Renderer(this, theme, this.map);
        this.player = new Player(this.terminal, this.control, theme);
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

                    final String mapChar = this.player.position.getMapChar();
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
                        this.renderer.renderOutOfBounds();

                        Thread.sleep(1500);
                        cleared = false;

                        player.backtrack();

                        while (!this.renderer.isWithinBounds(this.player)) {
                            player.move();
                        }

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
    }

    public void checkDoor() {
        final String selectedDoor = this.player.position.getMapChar();
        if (!this.map.getDoors().containsKey(selectedDoor.charAt(0))) return;

        this.terminal.puts(InfoCmp.Capability.clear_screen);

        this.map.setCurrentMap(this.map.getDoors().get(selectedDoor.charAt(0)));
        this.player.position = new Position(terminal.getHeight() / 2, terminal.getWidth() / 2);
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
}
