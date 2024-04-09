package com.example.gametest.gameObject;

import android.content.Context;

import androidx.core.content.ContextCompat;
import  android.graphics.Canvas;

import com.example.gametest.GameDisplay;
import com.example.gametest.GameLoop;
import com.example.gametest.gamePanel.Joystick;
import com.example.gametest.R;
import com.example.gametest.Utils;
import com.example.gametest.gamePanel.HealthBar;
import com.example.gametest.gamePanel.Performance;
import com.example.gametest.graphics.Animator;
import com.example.gametest.graphics.Sprite;

/*
    Player is the main character, can be controled by the touch of joystick
    the Player class extent to Cirle, which is an extend of GameObject

 */
public class Player extends Circle {
    public static final double SPEED_PIXELS_PER_SECOND = 400.0;
    public static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    public static final int MAX_HEALTH_POINTS = 10;
    private final Joystick joystick;
    private HealthBar healthBar;
    private int healthPoints;
    private Animator animator;
    private PlayerState playerState;

    public Player(Context context, Joystick joystick, double positionX, double positionY, double radius, Animator animator){
        super(context, ContextCompat.getColor(context, R.color.player), positionX, positionY, radius);
        this.joystick = joystick;
        this.healthBar = new HealthBar(context, this);
        this.healthPoints = MAX_HEALTH_POINTS;
        this.animator = animator;
        this.playerState = new PlayerState(this);
    }

    public void update() {
        //update velocity based on actuator of joystick
        velocityX = joystick.getActuatorX()*MAX_SPEED;
        velocityY = joystick.getActuatorY()*MAX_SPEED;
        //update position
        positionX += velocityX;
        positionY += velocityY;

        //update the direction
        if(velocityX != 0 || velocityY != 0)
        {
            //normalize velocity to get direction (unit vector of velocity)
            double distance = Utils.getDistanceBetweenPoints(0, 0, velocityX, velocityY);
            directionX = velocityX / distance;
            directionY = velocityY / distance;

        }

        playerState.update();
    }

    public void draw(Canvas canvas, GameDisplay gameDisplay)
    {
        animator.draw(canvas, gameDisplay, this);
        healthBar.draw(canvas, gameDisplay);
    }


    public void setPosition(double positionX, double positionY) {
            this.positionX = positionX;
            this.positionY = positionY;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        //only allow positive values
        if (healthPoints >= 0) {
            this.healthPoints = healthPoints;
        }
    }

    public PlayerState getPlayerState() {
        return playerState;
    }
}
