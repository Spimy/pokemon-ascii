package dev.spimy.pokemon.screen;

import dev.spimy.pokemon.GameManager;
import dev.spimy.pokemon.player.Inventory;
import dev.spimy.pokemon.player.Player;
import dev.spimy.pokemon.player.Pokeball;
import dev.spimy.pokemon.player.Position;
import dev.spimy.pokemon.screen.ascii.Ascii;
import dev.spimy.pokemon.screen.ascii.AsciiArt;
import dev.spimy.pokemon.screen.map.GameMap;
import org.jline.jansi.Ansi;
import org.jline.jansi.Ansi.Color;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;

import java.util.Arrays;
import java.util.List;

public class Renderer {
    private final GameManager gameManager;
    private final Terminal terminal;
    private final GameMap map;

    private final String[][] screen;
    private final String[][] buffer;

    public Renderer(GameManager gameManager, GameMap map) {
        this.gameManager = gameManager;
        this.terminal = gameManager.getTerminal();
        this.map = map;

        this.screen = new String[this.terminal.getHeight()][this.terminal.getWidth()];
        this.buffer = new String[this.terminal.getHeight()][this.terminal.getWidth()];

        this.initializeArrays();
        this.renderMap();
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
        this.renderMetadata();
        this.renderPlayer(player);
        this.updateScreen();
    }

    private void renderMap() {
        final List<String> map = this.map.getCurrentMapData();
        final int startRow = Math.floorDiv(this.buffer.length - map.size(), 2);
        this.renderContent(map, startRow);
    }

    private void renderMetadata() {
        this.drawBorder();

        int column = (int) (this.terminal.getWidth() * 0.05);
        Inventory inventory = this.gameManager.getPlayer().getInventorySave().getData().getFirst();

        String top = String.format(
                "%s | Level: %s | Money: %s",
                this.map.getCurrentMap().toString(),
                inventory.getLevel(),
                inventory.getMoney()
        );
        String bottom = String.format(
                "%s: %s | %s: %s | %s: %s",
                Pokeball.NORMAL.name,
                inventory.getPokeballs().get(Pokeball.NORMAL),
                Pokeball.ULTRA.name,
                inventory.getPokeballs().get(Pokeball.ULTRA),
                Pokeball.MASTER.name,
                inventory.getPokeballs().get(Pokeball.MASTER)
        );

        for (int i = 0; i < top.length(); i++) {
            final String s = Ansi.ansi()
                    .bg(Theme.BORDER_COLOR)
                    .fg(Theme.PLAYER_COLOR)
                    .a(top.charAt(i))
                    .reset()
                    .toString();

            this.buffer[0][column + i] = s;
        }
        for (int i = 0; i < bottom.length(); i++) {
            final String s = Ansi.ansi()
                    .bg(Theme.BORDER_COLOR)
                    .fg(Theme.PLAYER_COLOR)
                    .a(bottom.charAt(i))
                    .reset()
                    .toString();

            this.buffer[this.buffer.length - 1][column + i] = s;
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
                "It is recommended to go full screen on your terminal.".split("\n"),
                controlStartRow + 1,
                Theme.FOREGROUND_COLOR
        );

        this.renderContent(
                "Restart to take effect.".split("\n"),
                controlStartRow + 2,
                Theme.FOREGROUND_COLOR
        );

        this.renderContent(
                Ascii.getControls().getContent().split("\n"),
                controlStartRow + 4,
                Ascii.getControls().getColor()
        );

        this.updateScreen();
    }

    public void renderPauseMenu() {
        this.drawBorder();

        final int controlStartRow = renderPause();
        this.renderContent(
                Ascii.getControls().getContent().split("\n"),
                controlStartRow,
                Ascii.getControls().getColor()
        );

        this.updateScreen();
    }

    public void renderDialogue(AsciiArt dialogue) {
        this.drawBorder();
        this.renderMetadata();

        final int BOTTOM_MARGIN = 2;
        final String[] dialogueArray = dialogue.getContent().split("\n");
        final int startRow = this.terminal.getHeight() - dialogueArray.length - BOTTOM_MARGIN;

        this.renderContent(dialogueArray, startRow, dialogue.getColor());

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
        this.renderContent(pauseArray, startRow, Ascii.getPause().getColor());
        return startRow + pauseArray.length;
    }

    /**
     * Always centered horizontally
     *
     * @param content   the content to display
     * @param startRow  the start row
     * @param fg        the foreground (text) color, defaults to Theme.FOREGROUND_COLOR if not provided
     */
    private void renderContent(final String[] content, int startRow, Color fg) {
        for (int i = startRow; i < startRow + content.length; i++) {
            final String contentRow = content[i - startRow];
            final int startCol = Math.floorDiv(this.terminal.getWidth() - contentRow.length(), 2);

            for (int j = startCol; j < startCol + contentRow.length(); j++) {
                final String curChar = String.valueOf(contentRow.charAt(j - startCol));

                if (curChar.equals(";")) {
                    this.buffer[i][j] = this.map.getGrass();
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

    private void renderContent(final List<String> content, int x) {
        this.renderContent(content.toArray(new String[0]), x, Theme.FOREGROUND_COLOR);
    }

    public void renderPlayer(final Player player) {
        if (this.isWithinBounds(player)) {
            Position position = player.getPosition();
            this.buffer[position.getPrevX()][position.getPrevY()] = Ansi.ansi()
                .bg(Theme.BACKGROUND_COLOR)
                .a(" ")
                .reset()
                .toString();

            position.setMapChar(this.buffer[position.getCurrX()][position.getCurrY()]);

            this.buffer[position.getCurrX()][position.getCurrY()] = Ansi.ansi()
                .bg(Theme.PLAYER_COLOR)
                .a(" ")
                .reset()
                .toString();
        }
    }

    public boolean isWithinBounds(final Player player) {
        return
            player.getPosition().getCurrX() > 0 &&
            player.getPosition().getCurrX() < this.terminal.getHeight() - 1 &&
            player.getPosition().getCurrY() > 0 &&
            player.getPosition().getCurrY() < this.terminal.getWidth() - 1;
    }

    public String[][] getBuffer() {
        return this.buffer;
    }
}
