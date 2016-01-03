package com.softdev.nightlite;

import java.util.Random;

/**
 * Created by NiteLite on 30/11/15.
 */

public class Board {

    private int mapHeight;
    private int mapWidth;
    private Tile[][] board;
    //touch down on tile: x coord
    private int x;
    //touch down on tile: y coord
    private int y;

    private String currentColor;

    private int taps;

    public Board(int width, int height){
        this.mapHeight = height;
        this.mapWidth = width;
        this.board = new Tile[mapWidth][mapHeight];
        this.taps = 0;
    }

    private enum TileColor{
        BLUE,RED,YELLOW,GREEN,PURPLE,ORANGE,PINK;

        public static TileColor getRandom(){
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    }

    public enum Directions{
        NORTH (0,-1),
        EAST(1,0),
        SOUTH (0,1),
        WEST (-1,0);

        private final int dx;
        private final int dy;

        Directions(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    public Tile[][] generateNewMap(){
        for(int x = 0; x < board.length; x++){
            for(int y = 0; y < board.length; y++){
                Tile tile = new Tile(x,y);
                tile.setColor(TileColor.getRandom().toString());
                tile.setIsLinked(false);
                board[x][y] = tile;
            }
        }
        taps = 0;
        board[0][0].setIsLinked(true);
        return board;
    }

    public void move(int x, int y) {
        currentColor = this.getColor(x, y);
        Directions[] directions = Directions.values();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                for (Directions dir : directions) {
                    try{
                        if(board[i+dir.dx][j+dir.dy].getColor().equals(currentColor) && board[i][j].isLinked()){
                            board[i+dir.dx][j+dir.dy].setColor(currentColor);
                            board[i+dir.dx][j+dir.dy].setIsLinked(true);
                            this.setCurrentColor();
                        }
                    } catch(Exception e){

                    }
                }
            }
        }
    }

    private void setCurrentColor(){
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board.length; y++) {
                if(board[x][y].isLinked()){
                    board[x][y].setColor(currentColor);
                }
            }
        }
    }

    public boolean gameEnd(){
        int linked = 0;

        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board.length; y++) {
                if(board[x][y].isLinked()){
                    linked++;
                }
            }
        }

        if(linked == mapWidth*mapHeight){
            return true;
        }

        else{
            return false;
        }
    }

    public String getColor(int x, int y){
        if(x < getMapWidth() && y < getMapHeight()) {
            return board[x][y].getColor();
        }
        else{
            return currentColor;
        }
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getTaps() {
        return taps;
    }

    public void addTap(){
        if(!gameEnd()){
            this.taps++;
        }
    }

    public void resetTaps(){
        this.taps = -1;
    }
}
