package dev.spimy.pokemon.player.controller;

public class Control {
    final int upp = 80;           // P
    final int lowp = 112;         // p
    final int upq = 81;           // Q
    final int lowq = 113;         // q
    final int upw = 87;           // W
    final int loww = 119;         // w
    final int upa = 65;           // A
    final int lowa = 97;          // a
    final int ups = 83;           // S
    final int lows = 115;         // s
    final int upd = 68;           // D
    final int lowd = 100;         // d

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
}
