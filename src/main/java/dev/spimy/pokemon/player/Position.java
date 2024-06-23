package dev.spimy.pokemon.player;

public class Position {
    private int currX;
    private int currY;
    private int prevX;
    private int prevY;
    private String mapChar;

    public Position(final int x, final int y) {
        this.currX = x;
        this.currY = y;
    }

    public int getCurrX() {
        return this.currX;
    }

    public void setCurrX(final int currX) {
        this.currX = currX;
    }

    public int getCurrY() {
        return this.currY;
    }

    public void setCurrY(final int currY) {
        this.currY = currY;
    }

    public int getPrevX() {
        return this.prevX;
    }

    public void setPrevX(final int prevX) {
        this.prevX = prevX;
    }

    public int getPrevY() {
        return this.prevY;
    }

    public void setPrevY(final int prevY) {
        this.prevY = prevY;
    }

    public String getMapChar() {
        return this.mapChar;
    }

    public void setMapChar(final String mapChar) {
        this.mapChar = mapChar;
    }
}