package com.example.gametest;

import android.graphics.Rect;

import com.example.gametest.gameObject.GameObject;

public class GameDisplay {

    public final Rect DISPLAY_RECT;
    private final int widthPixels;
    private final int heightPixels;
    private double gameToDisplayCoordinatesOffsetX;
    private double gameToDisplayCoordinatesOffsetY;
    private double displayCenterX;
    private double displayCenterY;
    private double gameCenterX;
    private double gameCenterY;
    private GameObject centerObject;

    public GameDisplay(int widthPixels, int heighPixels, GameObject centerObject){
        this.widthPixels = widthPixels;
        this.heightPixels = heighPixels;
        DISPLAY_RECT = new Rect(0,0,widthPixels, heighPixels);

        this.centerObject = centerObject;

        displayCenterX = widthPixels / 2.0;
        displayCenterY = heighPixels / 2.0;
    }

    public void update() {
        gameCenterX = centerObject.getPositionX();
        gameCenterY = centerObject.getPositionY();

        gameToDisplayCoordinatesOffsetX = displayCenterX - gameCenterX;
        gameToDisplayCoordinatesOffsetY = displayCenterY - gameCenterY;
    }


    public double gameToDisplayCoordinatesX(double x) {
        return x + gameToDisplayCoordinatesOffsetX;
    }

    public double gameToDisplayCoordinatesY(double y) {
        return y + gameToDisplayCoordinatesOffsetY;
    }

    public Rect getGameRect() {
        return new Rect(
                (int) (gameCenterX - widthPixels / 2),
                (int) (gameCenterY - heightPixels / 2),
                (int) (gameCenterX + widthPixels / 2),
                (int) (gameCenterY + heightPixels / 2)

        );
    }
}
