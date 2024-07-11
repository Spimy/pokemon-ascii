package dev.spimy.pokemon.screen;


import org.jline.jansi.Ansi.Color;

public class Ascii {
    public interface AsciiArt {
        String getContent();

        default Color getColor() {
            return Color.WHITE;
        }
    }

    public static AsciiArt getLogo() {
        return new AsciiArt() {
            @Override
            public String getContent() {
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

            @Override
            public Color getColor() {
                return Color.YELLOW;
            }
        };
    }

    public static AsciiArt getControls() {
        return () -> """
                ╔══════════════════════╗
                ║       Controls       ║
                ║  P - [ Play-Pause ]  ║
                ║  W - [ Up ]          ║
                ║  A - [ Left ]        ║
                ║  S - [ Down ]        ║
                ║  D - [ Right ]       ║
                ║  Q - [ Quit ]        ║
                ╚══════════════════════╝
                """;
    }

    public static AsciiArt getPause() {
        return () -> """
                ┌──────────────────┐
                │╔═╗╔═╗╦ ╦╔═╗╔═╗╔╦╗│
                │╠═╝╠═╣║ ║╚═╗║╣  ║║│
                │╩  ╩ ╩╚═╝╚═╝╚═╝═╩╝│
                └──────────────────┘
                """;
    }

    public static AsciiArt getOutOfBoundDialogue() {
        return () -> """
                ╔══════════════════════════════════════════════════════════╗
                ║ Otherworldly Being                                       ║
                ║──────────────────────────────────────────────────────────║
                ║ How about we explore the area ahead of us later?         ║
                ╚═════════════════════════════════════════════════════════v╝
                """;
    }

    public static AsciiArt getHealedDialogue() {
        return () -> """
                ╔══════════════════════════════════════════════════════════╗
                ║ Nurse Joy                                                ║
                ║──────────────────────────────────────────────────────────║
                ║ All your Pokémons have been healed to full health!       ║
                ╚═════════════════════════════════════════════════════════v╝
                """;
    }
}
