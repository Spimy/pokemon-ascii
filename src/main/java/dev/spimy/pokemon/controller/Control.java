package dev.spimy.pokemon.controller;

public class Control {
    private final int upp = 80;           // P
    private final int lowp = 112;         // p
    private final int upq = 81;           // Q
    private final int lowq = 113;         // q
    private final int upw = 87;           // W
    private final int loww = 119;         // w
    private final int upa = 65;           // A
    private final int lowa = 97;          // a
    private final int ups = 83;           // S
    private final int lows = 115;         // s
    private final int upd = 68;           // D
    private final int lowd = 100;         // d
    private final int enter = 13;         // enter / return

    public boolean isPlay(final int key) {
        return (key == this.lowp || key == this.upp);
    }

    public boolean isQuit(final int key) {
        return (key == this.upq || key == this.lowq);
    }

    public boolean isUp(final int key) {
        return (key == this.upw || key == this.loww);
    }

    public boolean isLeft(final int key) {
        return (key == this.upa || key == this.lowa);
    }

    public boolean isDown(final int key) {
        return (key == this.ups || key == this.lows);
    }

    public boolean isRight(final int key) {
        return (key == this.upd || key == this.lowd);
    }

    public boolean isEnter(final int key) {
        return key == this.enter;
    }
}
