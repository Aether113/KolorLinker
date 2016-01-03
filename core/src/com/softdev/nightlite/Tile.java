package com.softdev.nightlite;

/**
 * Created by NiteLite on 23/12/15.
 */
public class Tile {

    private int x;
    private int y;
    private boolean isLinked;
    private String color;

    public Tile(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isLinked() {
        return isLinked;
    }

    public void setIsLinked(boolean isLinked) {
        this.isLinked = isLinked;
    }

    public void setColor(String color){
        this.color = color;
    }

    public String getColor(){
        return this.color;
    }
}