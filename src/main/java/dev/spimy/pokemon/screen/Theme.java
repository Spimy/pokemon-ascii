package dev.spimy.pokemon.screen;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import org.jline.jansi.Ansi.Color;

public class Theme {
  final private Color backgroundColor;
  final private ArrayList<ArrayList<Color>> themeColors;

  public Theme() {
    backgroundColor = Color.DEFAULT;
    themeColors = createThemeArray();
  }

  public ArrayList<Color> getTheme() {
    while (true) {
      ArrayList<Color> c = themeColors.get(
          ThreadLocalRandom.current().nextInt(themeColors.size()));
      if (c.getFirst() != backgroundColor)
        return c;
    }
  }

  private ArrayList<ArrayList<Color>> createThemeArray() {
    Color[] JLineColors = Color.values();
    ArrayList<ArrayList<Color>> Colors = new ArrayList<>();
    int index = 0;
    for (Color foreground : JLineColors) {
      for (Color background : JLineColors) {
        if (foreground != background &&
            (foreground != Color.DEFAULT && background != Color.DEFAULT) &&
            background != Color.BLACK) {
          Colors.add(new ArrayList<>());
          Colors.get(index).add(background);
          Colors.get(index).add(foreground);
          index++;
        }
      }
    }
    return Colors;
  }

  public Color getBackground() {
    return backgroundColor;
  }
}
