package dev.spimy.pokemon.player.controller;

public class Control {
    int upp = 80;           // P
    int lowp = 112;         // p
    int upq = 81;           // Q
    int lowq = 113;         // q
    int upw = 87;           // W
    int loww = 119;         // w
    int upa = 65;           // A
    int lowa = 97;          // a
    int ups = 83;           // S
    int lows = 115;         // s
    int upd = 68;           // D
    int lowd = 100;         // d

    public boolean isPlay(final int key) {
        return (key == lowp || key == upp);
    }

    public boolean isQuit(final int key) {
        return (key == upq || key == lowq);
    }

    public boolean isUp(final int key) {
        return (key == upw || key == loww);
    }

    public boolean isLeft(final int key) {
        return (key == upa || key == lowa);
    }

    public boolean isDown(final int key) {
        return (key == ups || key == lows);
    }

    public boolean isRight(final int key) {
        return (key == upd || key == lowd);
    }
}
