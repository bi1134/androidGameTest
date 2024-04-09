package com.example.gametest.gameObject;

import android.graphics.Canvas;

import com.example.gametest.GameDisplay;

/*
    GameObject is an abstract class which is the foundation of  all world object in the game
 */
public abstract class GameObject {
    protected double positionX;
    protected double positionY;
    protected double velocityX = 0;
    protected double velocityY = 0;
    protected double directionX = 1;
    protected double directionY = 0;

    public GameObject(double positionX, double positionY){
        this.positionX = positionX;
        this.positionY = positionY;
    }
    public abstract void draw(Canvas canvas, GameDisplay gameDisplay);
    public abstract void update();
    public double getPositionX() {
        return positionX;
    }
    public double getPositionY() {
        return positionY;
    }
    /*
     * getDistanceBetweenObjects returns the distance between two game objects
     * @param obj1
     * @param obj2
     * @return
     */
    public static double getDistanceBetweenObjects(GameObject obj1, GameObject obj2) {
        return Math.sqrt(
                Math.pow(obj2.getPositionX() - obj1.getPositionX(), 2) +
                Math.pow(obj2.getPositionY() - obj1.getPositionY(), 2)
        );
    }
    public double getDirectionX() {
        return directionX;
    }
    public double getDirectionY() {
        return directionY;
    }
}
