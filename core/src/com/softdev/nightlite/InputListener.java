package com.softdev.nightlite;

import com.badlogic.gdx.input.*;

/**
 * Created by NiteLite on 01/12/15.
 *
 * Custom InputListener implementation that extends GestureDetector.GestureAdapter.
 * The InputListener registers all taps and will translate them to the x,y board grid if the board
 * is tapped. Otherwise it will just register a tap (count++);
 */

public class InputListener extends GestureDetector.GestureAdapter {

    private Board board;
    private int tileSize;

    public InputListener(Board board, int tileSize){
        this.board = board;
        this.tileSize = tileSize;
    }

    @Override
    public boolean tap(float x, float y, int count, int button){
        int mapx = (int) Math.floor((int)x / tileSize);
        int mapy = (int) Math.floor((int)y / tileSize);

        board.addTap();
        board.move(mapx, mapy);

        return false;
    }
}
