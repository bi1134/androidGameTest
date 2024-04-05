package com.example.gametest.gameObject;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.gametest.GameLoop;
import com.example.gametest.R;

public class Enemy extends Circle {
    private static final double SPEED_PIXELS_PER_SECOND = Player.SPEED_PIXELS_PER_SECOND*0.6;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private static final double SPAWNS_PER_MINUTES = 20;
    private static final double SPAWNS_PER_SECOND = SPAWNS_PER_MINUTES/60;
    private static final double UPDATES_PER_SPAWN = GameLoop.MAX_UPS/SPAWNS_PER_SECOND;
    private static double updatesUntilNextSpawn = UPDATES_PER_SPAWN;
    private final Player player;

    public Enemy(Context context, Player player, double positionX, double positionY, double radius) {
        super(context, ContextCompat.getColor(context, R.color.enemy), positionX, positionY, radius);
        this.player = player;
    }

    public Enemy(Context context, Player player) {
        super(context,
              ContextCompat.getColor(context, R.color.spell),
              Math.random() * 1000,
              Math.random() * 1000,
              30);
        this.player = player;
    }

    /* readyToSpawn check if a new enemy should spawn, according to the decided number of spawns
       per minute (see SPAWN_PER_MINUTE at top)
       @return
     */
    public static boolean readyToSpawn() {
        if (updatesUntilNextSpawn <= 0)
        {
            updatesUntilNextSpawn += UPDATES_PER_SPAWN;
            return true;
        }
        else
        {
            updatesUntilNextSpawn --;
            return false;
        }
    }

    @Override
    public void update() {
    //===========================================
    // update velocity
    //===========================================
    // calculate vector from enemy to player (in x and y)
        double distanceToPlayerX = player.getPositionX() - positionX;
        double distanceToPlayerY = player.getPositionY() - positionY;

        // Calculate (absolute) distance between enemy (this) and player
        double distanceToPlayer = GameObject.getDistanceBetweenObjects(this, player);

    // calculate direction from enemy to player
        double directionX = distanceToPlayerX/distanceToPlayer;
        double directionY = distanceToPlayerY/distanceToPlayer;

    // Set velocity in the direction to the player
        if(distanceToPlayer > 0) { // Avoid division by zero
            velocityX = directionX*MAX_SPEED;
            velocityY = directionY*MAX_SPEED;
        }
        else {
            velocityX = 0;
            velocityY = 0;
        }

    // update the position of the enemy
        positionX += velocityX;
        positionY += velocityY;

    }
}
