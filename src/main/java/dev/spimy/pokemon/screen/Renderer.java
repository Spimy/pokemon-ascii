package dev.spimy.pokemon.screen;

import java.util.ArrayList;
import java.util.Arrays;

import dev.spimy.pokemon.player.Player;
import dev.spimy.pokemon.screen.map.GameMap;
import org.jline.jansi.Ansi;
import org.jline.jansi.Ansi.Color;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;

public class Renderer {
  private final Terminal terminal;
  private final Theme theme;
  private final AsciiArt ascii;
  private final GameMap map;

  private final String[][] screen;
  private final String[][] buffer;
  private final ArrayList<Color> borderTheme;

  public Renderer(Terminal terminal, Theme theme, AsciiArt ascii, GameMap map) {
    this.terminal = terminal;
    this.theme = theme;
    this.ascii = ascii;
    this.map = map;

    this.borderTheme = this.theme.getTheme();
    this.screen = new String[terminal.getHeight()][terminal.getWidth()];
    this.buffer = new String[terminal.getHeight()][terminal.getWidth()];

    initializeArrays();
    drawBorder();
  }

  public ArrayList<Color> getBorderTheme() {
    return borderTheme;
  }

  public void initializeArrays() {
    for (int i = 0; i < terminal.getHeight(); i++) {
      Arrays.fill(screen[i], Ansi.ansi().bg(theme.getBackground()).a(" ").reset().toString());
      Arrays.fill(buffer[i], Ansi.ansi().bg(theme.getBackground()).a(" ").reset().toString());
    }
  }

  public void renderGame(Player player) {
    drawMap();
    drawBorder();
    renderPlayer(player);
    updateScreen();
  }

  private void drawMap() {
    final ArrayList<String> map = this.map.getCurrentMapData();
    final int startRow = Math.floorDiv(this.buffer.length - map.size(), 2);

    for (int i = startRow; i < startRow + map.size(); i++) {
      final String mapRow = map.get(i - startRow);
      final int startCol = Math.floorDiv(this.buffer[i].length - mapRow.length(), 2);

      for (int j = startCol; j < startCol + mapRow.length(); j++) {
        String curChar = String.valueOf(mapRow.charAt(j - startCol));

        if (curChar.equals(";")) {
          String character = Ansi.ansi()
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

  // Used as reference, will remove later
  private void renderScore() {
    drawBorder();
    String[] scoreArray = ("Score: " + 1).split("");
    int y = (int) (terminal.getWidth() * 0.05);
    for (int i = 0; i < scoreArray.length; i++) {
      String s = Ansi.ansi()
          .a(scoreArray[i])
          .reset()
          .toString();
      buffer[0][y + i] = s;
    }
  }

  public void updateScreen() {
    for (int i = 0; i < terminal.getHeight(); i++) {
      for (int j = 0; j < terminal.getWidth(); j++) {
        if (buffer[i][j] != screen[i][j]) {
          terminal.puts(Capability.cursor_address, i, j);
          terminal.flush();
          System.out.print(String.valueOf(buffer[i][j]));
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
    renderLogo();
    renderControls();
    updateScreen();
  }

  public void renderPauseMenu() {
    drawBorder();
    renderPause();
    renderControls();
    updateScreen();
  }

  // Used as reference, will remove later
  public void renderGameOver() {
    initializeArrays();
    drawBorder();
    renderGameOverText();
    renderControls();
    updateScreen();
  }

  // Will try to clean up the math a bit later
  private void renderControls() {
    String[][] controlsArray = convertTo2DArray(ascii.getControls());
    drawText(controlsArray, terminal.getHeight() * 0.4, terminal.getWidth() * 0.43, ascii.getControlsTheme());
  }

  private void renderLogo() {
    String[][] logoArray = convertTo2DArray(ascii.getLogo());
    drawText(logoArray, terminal.getHeight() * 0.1, terminal.getWidth() * 0.33, ascii.getLogoTheme());
  }

  private void renderPause() {
    String[][] pauseArray = convertTo2DArray(ascii.getPause());
    drawText(pauseArray, terminal.getHeight() * 0.25, terminal.getWidth() * 0.44, ascii.getPauseTheme());
  }

  public void renderGameOverText() {
    String[][] gameOverArray = convertTo2DArray(ascii.getGameOver());
    drawText(gameOverArray, terminal.getHeight() * 0.15, terminal.getWidth() * 0.36, ascii.getGameOverTheme());
  }

  private void drawText(String[][] grid, double x, double y, ArrayList<Color> colorTheme) {
      for (String[] row : grid) {
          for (int j = 0; j < row.length; j++) {
              String cellContent = row[j];

              String l = Ansi.ansi()
                      .bg(colorTheme.get(0))
                      .fg(colorTheme.get(1))
                      .a(cellContent)
                      .reset()
                      .toString();

              buffer[(int) x][(int) y + j] = l;
          }
          x++;
      }
    }

    public void renderPlayer(Player player) {
      if (isWithinBounds(player.position.getCurrX(), player.position.getCurrY())) {
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
    } else {
        // TODO Out of bounds
        System.out.println("[Otherworldly Being] How about we explore the area ahead of us later?");
    }
  }

  private boolean isWithinBounds(int x, int y) {
    return x > 0 && x < terminal.getHeight() - 1 && y > 0 && y < terminal.getWidth() - 1;
  }

  private String[][] convertTo2DArray(String text) {
    String[] rows = text.split("\n");
    String[][] logo2DArray = new String[rows.length][];

    for (int i = 0; i < rows.length; i++) {
      logo2DArray[i] = rows[i].split("");
    }

    return logo2DArray;
  }
}
