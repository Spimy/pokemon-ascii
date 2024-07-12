package dev.spimy.pokemon.screen.ascii;

import org.jline.jansi.Ansi;

public interface AsciiArt {
    String getContent();

    default Ansi.Color getColor() {
        return Ansi.Color.WHITE;
    }
}