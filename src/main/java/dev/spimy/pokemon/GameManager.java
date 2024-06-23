package dev.spimy.pokemon;

import dev.spimy.pokemon.player.Player;
import dev.spimy.pokemon.player.Position;
import dev.spimy.pokemon.player.controller.Control;
import dev.spimy.pokemon.screen.AsciiArt;
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

    public GameManager(State state, final Terminal terminal) {
        this.state = state;
        this.terminal = terminal;
        this.control = new Control();
        this.map = new GameMap();
    }

    private void setup() {
        this.terminal.enterRawMode();
        this.terminal.puts(InfoCmp.Capability.cursor_invisible);

        Theme theme = new Theme();
        AsciiArt ascii = new AsciiArt(this.terminal, theme);

        this.renderer = new Renderer(this.terminal, theme, ascii, this.map);
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

    @SuppressWarnings("InfiniteLoopStatement")
    private void gameLoop() {
        boolean cleared = false;

        while (true) {
            switch (this.getState()) {
                case State.PLAY -> {
                    if (!cleared) {
                        this.terminal.puts(InfoCmp.Capability.clear_screen);
                        cleared = true;
                    }

                    this.renderer.renderGame(this.player);
                }
                case State.PAUSE -> {
                    this.renderer.renderPauseMenu();
                    this.renderer.drawBorder();
                    cleared = false;
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
        final String selectedDoor = this.player.position.getMapChar().toUpperCase();
        if (!this.map.getDoors().containsKey(selectedDoor.charAt(0))) return;

        this.terminal.puts(InfoCmp.Capability.clear_screen);

        this.map.setCurrentMap(this.map.getDoors().get(selectedDoor.charAt(0)));
        this.player.position = new Position(terminal.getHeight() / 2, terminal.getWidth() / 2);
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void quit() {
        System.out.print("\u001B[H\u001B[2J");
        System.out.print("\u001B[H\u001B[2J");
        System.exit(0);
    }
}
