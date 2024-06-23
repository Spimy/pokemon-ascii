package dev.spimy.pokemon.screen;

import dev.spimy.pokemon.GameManager;
import dev.spimy.pokemon.player.Player;
import dev.spimy.pokemon.screen.map.GameMap;
import org.jline.jansi.Ansi;
import org.jline.jansi.Ansi.Color;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;

import java.util.ArrayList;
import java.util.Arrays;

public class Renderer {
    private final Terminal terminal;
    private final Theme theme;
    private final AsciiArt ascii;
    private final GameMap map;

    private final String[][] screen;
    private final String[][] buffer;
    private final ArrayList<Color> borderTheme;

    public Renderer(GameManager gameManager, Theme theme, GameMap map) {
        this.terminal = gameManager.getTerminal();
        this.theme = theme;
        this.ascii = new AsciiArt(this.theme);
        this.map = map;

        this.borderTheme = this.theme.getTheme();
        this.screen = new String[terminal.getHeight()][terminal.getWidth()];
        this.buffer = new String[terminal.getHeight()][terminal.getWidth()];

        initializeArrays();
        drawBorder();
    }

    public void initializeArrays() {
        for (int i = 0; i < terminal.getHeight(); i++) {
            Arrays.fill(screen[i], Ansi.ansi().bg(theme.getBackground()).a(" ").reset().toString());
            Arrays.fill(buffer[i], Ansi.ansi().bg(theme.getBackground()).a(" ").reset().toString());
        }
    }

    public void renderGame(Player player) {
        renderMap();
        renderMapName();
        renderPlayer(player);
        updateScreen();
    }

    private void renderMap() {
        final ArrayList<String> map = this.map.getCurrentMapData();
        final int startRow = Math.floorDiv(this.buffer.length - map.size(), 2);
        this.renderContent(map, startRow);
    }

    private void renderMapName() {
        drawBorder();

        int y = (int) (terminal.getWidth() * 0.05);
        for (int i = 0; i < this.map.getCurrentMap().toString().length(); i++) {
            final String s = Ansi.ansi()
                    .a(this.map.getCurrentMap().toString().charAt(i))
                    .reset()
                    .toString();

            buffer[0][y + i] = s;
        }
    }

    @SuppressWarnings("StringEquality")
    public void updateScreen() {
        for (int i = 0; i < terminal.getHeight(); i++) {
            for (int j = 0; j < terminal.getWidth(); j++) {
                if (buffer[i][j] != screen[i][j]) {
                    terminal.puts(Capability.cursor_address, i, j);
                    terminal.flush();
                    System.out.print(buffer[i][j]);
                    screen[i][j] = buffer[i][j];
                }
            }
        }
        terminal.flush();
    }

    public void drawBorder() {
        for (int i = 0; i < terminal.getWidth(); i++) {
            buffer[0][i] = Ansi.ansi().bg(borderTheme.getFirst()).a(" ").reset().toString();
            buffer[terminal.getHeight() - 1][i] = Ansi.ansi().bg(borderTheme.getFirst()).a(" ").reset().toString();
        }
        for (int i = 0; i < terminal.getHeight(); i++) {
            buffer[i][0] = Ansi.ansi().bg(borderTheme.getFirst()).a(" ").reset().toString();
            buffer[i][terminal.getWidth() - 1] = Ansi.ansi().bg(borderTheme.getFirst()).a(" ").reset().toString();
        }
    }

    public void renderStartMenu() {
        initializeArrays();
        drawBorder();
        final int controlStartRow = renderLogo();
        this.renderContent(this.ascii.getControls().split("\n"), controlStartRow);
        updateScreen();
    }

    public void renderPauseMenu() {
        drawBorder();
        final int controlStartRow = renderPause();
        this.renderContent(this.ascii.getControls().split("\n"), controlStartRow);
        updateScreen();
    }

    // Used as reference, will remove later
    public void renderGameOver() {
        initializeArrays();
        drawBorder();
        final int controlStartRow = renderGameOverText();
        this.renderContent(this.ascii.getControls().split("\n"), controlStartRow);
        updateScreen();
    }

    public void renderOutOfBounds() {
        drawBorder();
        renderMapName();

        final String dialogue = this.ascii.getOutOfBoundDialogue();

        final int BOTTOM_MARGIN = 2;
        final String[] dialogueArray = dialogue.split("\n");
        final int startRow = this.terminal.getHeight() - dialogueArray.length - BOTTOM_MARGIN;

        this.renderContent(dialogueArray, startRow);

        updateScreen();
    }

    private int renderLogo() {
        final String[] logoArray = this.ascii.getLogo().split("\n");
        final int startRow = (int) Math.floor(this.terminal.getHeight() * 0.2);
        this.renderContent(logoArray, startRow);
        return startRow + logoArray.length;
    }

    private int renderPause() {
        final String[] pauseArray = this.ascii.getPause().split("\n");
        final int startRow = (int) Math.floor(this.terminal.getHeight() * 0.25);
        this.renderContent(pauseArray, startRow);
        return startRow + pauseArray.length;
    }

    public int renderGameOverText() {
        final String[] gameOverArray = this.ascii.getGameOver().split("\n");
        final int startRow = (int) Math.floor(this.terminal.getHeight() * 0.33);
        this.renderContent(gameOverArray, startRow);
        return gameOverArray.length;
    }


    /**
     * Always centered horizontally
     *
     * @param content the content to display
     * @param y the start row
     */
    private void renderContent(final String[] content, int y) {
        for (int i = y; i < y + content.length; i++) {
            final String contentRow = content[i - y];
            final int startCol = Math.floorDiv(this.terminal.getWidth() - contentRow.length(), 2);

            for (int j = startCol; j < startCol + contentRow.length(); j++) {
                final String curChar = String.valueOf(contentRow.charAt(j - startCol));

                if (curChar.equals(";")) {
                    final String character = Ansi.ansi()
                            .bg(Color.DEFAULT)
                            .fg(Color.GREEN)
                            .a(curChar)
                            .reset()
                            .toString();

                    this.buffer[i][j] = character;
                    continue;
                }

                this.buffer[i][j] = curChar;
            }
        }
    }

    private void renderContent(final ArrayList<String> content, int y) {
        this.renderContent(content.toArray(new String[0]), y);
    }

    public void renderPlayer(final Player player) {
        if (isWithinBounds(player)) {
            buffer[player.position.getPrevX()][player.position.getPrevY()] = Ansi.ansi()
                    .bg(Color.DEFAULT)
                    .a(player.position.getMapChar())
                    .reset()
                    .toString();

            player.position.setMapChar(buffer[player.position.getCurrX()][player.position.getCurrY()]);

            buffer[player.position.getCurrX()][player.position.getCurrY()] = Ansi.ansi()
                    .bg(player.getTheme().getFirst())
                    .a(" ")
                    .reset()
                    .toString();
        }
    }

    public boolean isWithinBounds(final Player player) {
        return
            player.position.getCurrX() > 0 &&
            player.position.getCurrX() < terminal.getHeight() - 1 &&
            player.position.getCurrY() > 0 &&
            player.position.getCurrY() < terminal.getWidth() - 1;
    }
}
