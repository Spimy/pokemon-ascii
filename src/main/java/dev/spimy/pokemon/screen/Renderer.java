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
    private final GameMap map;

    private final String[][] screen;
    private final String[][] buffer;

    public Renderer(GameManager gameManager, GameMap map) {
        this.terminal = gameManager.getTerminal();
        this.map = map;

        this.screen = new String[this.terminal.getHeight()][this.terminal.getWidth()];
        this.buffer = new String[this.terminal.getHeight()][this.terminal.getWidth()];

        this.initializeArrays();
        this.drawBorder();
    }

    public void initializeArrays() {
        for (int i = 0; i < this.terminal.getHeight(); i++) {
            Arrays.fill(this.screen[i], Ansi.ansi().bg(Theme.BACKGROUND_COLOR).a(" ").reset().toString());
            Arrays.fill(this.buffer[i], Ansi.ansi().bg(Theme.BACKGROUND_COLOR).a(" ").reset().toString());
        }
    }

    public void renderGame(Player player) {
        this.renderMap();
        this.renderMapName();
        this.renderPlayer(player);
        this.updateScreen();
    }

    private void renderMap() {
        final ArrayList<String> map = this.map.getCurrentMapData();
        final int startRow = Math.floorDiv(this.buffer.length - map.size(), 2);
        this.renderContent(map, startRow);
    }

    private void renderMapName() {
        this.drawBorder();

        int y = (int) (this.terminal.getWidth() * 0.05);
        for (int i = 0; i < this.map.getCurrentMap().toString().length(); i++) {
            final String s = Ansi.ansi()
                    .a(this.map.getCurrentMap().toString().charAt(i))
                    .reset()
                    .toString();

            this.buffer[0][y + i] = s;
        }
    }

    @SuppressWarnings("StringEquality")
    public void updateScreen() {
        for (int i = 0; i < this.terminal.getHeight(); i++) {
            for (int j = 0; j < this.terminal.getWidth(); j++) {
                if (this.buffer[i][j] != this.screen[i][j]) {
                    this.terminal.puts(Capability.cursor_address, i, j);
                    this.terminal.flush();
                    System.out.print(this.buffer[i][j]);
                    this.screen[i][j] = this.buffer[i][j];
                }
            }
        }
        this.terminal.flush();
    }

    public void drawBorder() {
        for (int i = 0; i < this.terminal.getWidth(); i++) {
            this.buffer[0][i] = Ansi.ansi().bg(Theme.BORDER_COLOR).a(" ").reset().toString();
            this.buffer[this.terminal.getHeight() - 1][i] = Ansi.ansi().bg(Theme.BORDER_COLOR).a(" ").reset().toString();
        }
        for (int i = 0; i < this.terminal.getHeight(); i++) {
            this.buffer[i][0] = Ansi.ansi().bg(Theme.BORDER_COLOR).a(" ").reset().toString();
            this.buffer[i][terminal.getWidth() - 1] = Ansi.ansi().bg(Theme.BORDER_COLOR).a(" ").reset().toString();
        }
    }

    public void renderStartMenu() {
        this.initializeArrays();
        this.drawBorder();
        
        final int controlStartRow = renderLogo();
        this.renderContent(
                Ascii.getControls().getContent().split("\n"),
                controlStartRow
        );

        this.updateScreen();
    }

    public void renderPauseMenu() {
        this.drawBorder();

        final int controlStartRow = renderPause();
        this.renderContent(
                Ascii.getControls().getContent().split("\n"),
                controlStartRow
        );

        this.updateScreen();
    }

    // Used as reference, will remove later
    public void renderGameOver() {
        this.initializeArrays();
        this.drawBorder();

        final int controlStartRow = renderGameOverText();
        this.renderContent(
                Ascii.getControls().getContent().split("\n"),
                controlStartRow
        );

        this.updateScreen();
    }

    public void renderOutOfBounds() {
        this.drawBorder();
        this.renderMapName();

        final String dialogue = Ascii.getOutOfBoundDialogue().getContent();

        final int BOTTOM_MARGIN = 2;
        final String[] dialogueArray = dialogue.split("\n");
        final int startRow = this.terminal.getHeight() - dialogueArray.length - BOTTOM_MARGIN;

        this.renderContent(dialogueArray, startRow);

        this.updateScreen();
    }

    private int renderLogo() {
        final String[] logoArray = Ascii.getLogo().getContent().split("\n");
        final int startRow = (int) Math.floor(this.terminal.getHeight() * 0.2);
        this.renderContent(logoArray, startRow, Ascii.getLogo().getColor());
        return startRow + logoArray.length;
    }

    private int renderPause() {
        final String[] pauseArray = Ascii.getPause().getContent().split("\n");
        final int startRow = (int) Math.floor(this.terminal.getHeight() * 0.25);
        this.renderContent(pauseArray, startRow);
        return startRow + pauseArray.length;
    }

    public int renderGameOverText() {
        final String[] gameOverArray = Ascii.getGameOver().getContent().split("\n");
        final int startRow = (int) Math.floor(this.terminal.getHeight() * 0.33);
        this.renderContent(gameOverArray, startRow);
        return gameOverArray.length;
    }


    /**
     * Always centered horizontally
     *
     * @param content the content to display
     * @param y       the start row
     * @param fg      the foreground (text) color, defaults to Theme.FOREGROUND_COLOR if not provided
     */
    private void renderContent(final String[] content, int y, Color fg) {
        for (int i = y; i < y + content.length; i++) {
            final String contentRow = content[i - y];
            final int startCol = Math.floorDiv(this.terminal.getWidth() - contentRow.length(), 2);

            for (int j = startCol; j < startCol + contentRow.length(); j++) {
                final String curChar = String.valueOf(contentRow.charAt(j - startCol));

                if (curChar.equals(";")) {
                    final String character = Ansi.ansi()
                            .bg(Theme.BACKGROUND_COLOR)
                            .fg(Theme.GRASS_COLOR)
                            .a(curChar)
                            .reset()
                            .toString();

                    this.buffer[i][j] = character;
                    continue;
                }

                if (fg.equals(Theme.FOREGROUND_COLOR)) this.buffer[i][j] = curChar;
                else {
                    this.buffer[i][j] = Ansi.ansi()
                        .bg(Theme.BACKGROUND_COLOR)
                        .fg(fg)
                        .a(curChar)
                        .reset()
                        .toString();
                }
            }
        }
    }

    private void renderContent(final String[] content, int y) {
        this.renderContent(content, y, Theme.FOREGROUND_COLOR);
    }

    private void renderContent(final ArrayList<String> content, int y) {
        this.renderContent(content.toArray(new String[0]), y, Theme.FOREGROUND_COLOR);
    }

    public void renderPlayer(final Player player) {
        if (this.isWithinBounds(player)) {
            this.buffer[player.position.getPrevX()][player.position.getPrevY()] = Ansi.ansi()
                .bg(Theme.BACKGROUND_COLOR)
                .a(" ")
                .reset()
                .toString();

            player.position.setMapChar(this.buffer[player.position.getCurrX()][player.position.getCurrY()]);

            this.buffer[player.position.getCurrX()][player.position.getCurrY()] = Ansi.ansi()
                .bg(Theme.PLAYER_COLOR)
                .a(" ")
                .reset()
                .toString();
        }
    }

    public boolean isWithinBounds(final Player player) {
        return
            player.position.getCurrX() > 0 &&
            player.position.getCurrX() < this.terminal.getHeight() - 1 &&
            player.position.getCurrY() > 0 &&
            player.position.getCurrY() < this.terminal.getWidth() - 1;
    }
}
