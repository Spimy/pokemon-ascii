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

    public boolean isPlay(int key) {
        return (key == lowp || key == upp);
    }

    public boolean isQuit(int key) {
        return (key == upq || key == lowq);
    }

    public boolean isUp(int key) {
        return (key == upw || key == loww);
    }

    public boolean isLeft(int key) {
        return (key == upa || key == lowa);
    }

    public boolean isDown(int key) {
        return (key == ups || key == lows);
    }

    public boolean isRight(int key) {
        return (key == upd || key == lowd);
    }
}
