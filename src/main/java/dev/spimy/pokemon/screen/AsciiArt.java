package dev.spimy.pokemon.screen;


import org.jline.jansi.Ansi.Color;
import org.jline.terminal.Terminal;

import java.util.ArrayList;

public class AsciiArt {
    final private ArrayList<Color> logoTheme;
    final private ArrayList<Color> controlsTheme;
    final private ArrayList<Color> gameOverTheme;
    final private ArrayList<Color> pauseTheme;

    public AsciiArt(Terminal terminal, Theme theme) {
        logoTheme = theme.getTheme();
        controlsTheme = theme.getTheme();
        gameOverTheme = theme.getTheme();
        pauseTheme = theme.getTheme();
    }

    public String getLogo() {
        return """
                ╔───────────────────────────────────────────────────────────────╗
                │██████╗  ██████╗ ██╗  ██╗███████╗███╗   ███╗ ██████╗ ███╗   ██╗│
                │██╔══██╗██╔═══██╗██║ ██╔╝██╔════╝████╗ ████║██╔═══██╗████╗  ██║│
                │██████╔╝██║   ██║█████╔╝ █████╗  ██╔████╔██║██║   ██║██╔██╗ ██║│
                │██╔═══╝ ██║   ██║██╔═██╗ ██╔══╝  ██║╚██╔╝██║██║   ██║██║╚██╗██║│
                │██║     ╚██████╔╝██║  ██╗███████╗██║ ╚═╝ ██║╚██████╔╝██║ ╚████║│
                │╚═╝      ╚═════╝ ╚═╝  ╚═╝╚══════╝╚═╝     ╚═╝ ╚═════╝ ╚═╝  ╚═══╝│
                ╚───────────────────────────────────────────────────────────────╝
                """;
    }

    public String getControls() {
        return """
                ▐▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▌
                ▐      Controls        ▌
                ▐  P - [ Play-Pause ]  ▌
                ▐  W - [ Up ]          ▌
                ▐  A - [ Left ]        ▌
                ▐  S - [ Down ]        ▌
                ▐  D - [ Right ]       ▌
                ▐  Q - [ Quit ]        ▌
                ▐▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▌
                """;
    }

    public String getGameOver() {
        return """
                ▐▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▌
                ▐                                            ▌
                ▐    ░█▀▀░█▀█░█▄█░█▀▀░░░█▀█░█░█░█▀▀░█▀▄░█    ▌
                ▐    ░█░█░█▀█░█░█░█▀▀░░░█░█░▀▄▀░█▀▀░█▀▄░▀    ▌
                ▐    ░▀▀▀░▀░▀░▀░▀░▀▀▀░░░▀▀▀░░▀░░▀▀▀░▀░▀░▀    ▌
                ▐                                            ▌
                ▐▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▌
                """;
    }

    public String getPause() {
        return """
                ┌──────────────────┐
                │╔═╗╔═╗╦ ╦╔═╗╔═╗╔╦╗│
                │╠═╝╠═╣║ ║╚═╗║╣  ║║│
                │╩  ╩ ╩╚═╝╚═╝╚═╝═╩╝│
                └──────────────────┘
                """;
    }

    public ArrayList<Color> getLogoTheme() {
        return logoTheme;
    }

    public ArrayList<Color> getControlsTheme() {
        return controlsTheme;
    }

    public ArrayList<Color> getGameOverTheme() {
        return gameOverTheme;
    }

    public ArrayList<Color> getPauseTheme() {
        return pauseTheme;
    }

}
