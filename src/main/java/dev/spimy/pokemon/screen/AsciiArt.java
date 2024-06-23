package dev.spimy.pokemon.screen;


import org.jline.jansi.Ansi.Color;

import java.util.ArrayList;

public class AsciiArt {
    final private ArrayList<Color> logoTheme;
    final private ArrayList<Color> controlsTheme;
    final private ArrayList<Color> gameOverTheme;
    final private ArrayList<Color> pauseTheme;
    final private ArrayList<Color> outOfBoundDialogueTheme;

    public AsciiArt(Theme theme) {
        logoTheme = theme.getTheme();
        controlsTheme = theme.getTheme();
        gameOverTheme = theme.getTheme();
        pauseTheme = theme.getTheme();
        outOfBoundDialogueTheme = theme.getTheme();
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

    public String getOutOfBoundDialogue() {
        return """
                ╔══════════════════════════════════════════════════════════╗
                ║ Otherworldly Being                                       ║
                ╟──────────────────────────────────────────────────────────╢
                ║ How about we explore the area ahead of us later?         ║
                ╚═════════════════════════════════════════════════════════▼╝
                """;
    }

    public ArrayList<Color> getLogoTheme() {
        return logoTheme;
    }

    public ArrayList<Color> getControlsTheme() {
        return this.controlsTheme;
    }

    public ArrayList<Color> getGameOverTheme() {
        return this.gameOverTheme;
    }

    public ArrayList<Color> getPauseTheme() {
        return this.pauseTheme;
    }

    public ArrayList<Color> getOutOfBoundDialogueTheme() {
        return this.outOfBoundDialogueTheme;
    }

}
