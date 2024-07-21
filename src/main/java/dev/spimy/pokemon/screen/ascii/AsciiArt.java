package dev.spimy.pokemon.screen.ascii;

import org.jline.jansi.Ansi.Color;

public interface AsciiArt {
    String getContent();

    default Color getColor() {
        return Color.WHITE;
    }
}