package com.example.gametest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.gametest.gameObject.Enemy;
import com.example.gametest.gameObject.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.example.gametest.gameObject.Circle;
import com.example.gametest.gameObject.Spell;
import com.example.gametest.gamePanel.GameOver;
import com.example.gametest.gamePanel.Joystick;
import com.example.gametest.gamePanel.Performance;

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final Player player;
    private final Joystick joystick;
    private GameLoop gameLoop;
    private List<Enemy> enemyList = new ArrayList<Enemy>();
    private List<Spell> spellList = new ArrayList<Spell>();
    private Context context;
    private final Enemy enemy;
    private int joystickPointerID = 0;
    private int numberOfSpellToCast = 0;
    private GameOver gameOver;
    private Performance performance;

    public Game(Context context) {
        super(context);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        this.context = context;
        gameLoop = new GameLoop(this, surfaceHolder);

        //initialize game panel
        performance = new Performance(context, gameLoop);
        gameOver = new GameOver(context);
        joystick = new Joystick(275, 750, 100, 40);

        //initialize game objects
        player = new Player(context, joystick, 2*500, 500, 30);
        enemy = new Enemy(context, player, 500, 200, 30);

        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //handle touch event action
        switch (event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (joystick.getIsPressed())
                {
                    //joystick is pressed before this event -> cast spell
                    numberOfSpellToCast++;
                }
                else if (joystick.isPressed((double) event.getX(), (double) event.getY())){
                    //joystick is pressed in this event -> setIsPressed(true) and store ID
                    joystickPointerID = event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);
                }
                else {
                    //joystick was not previously, and not pressed in this event -> cast spell
                    numberOfSpellToCast++;
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                //checking if the joystick is pressed previously and is now moved
                if (joystick.getIsPressed())
                {
                    joystick.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if(joystickPointerID == event.getPointerId(event.getActionIndex()))
                {
                    //joystick has been let go of -> setIsPressed(false) and resetActuator
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                }
                return true;
        }


        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        Log.d("Game.java", "surfaceCreated()");
        if (gameLoop.getState().equals(Thread.State.TERMINATED))
        {
            gameLoop = new GameLoop(this,holder);
        }
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        Log.d("Game.java", "surfaceChanged()");
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder)
    {
        Log.d("Game.java", "surfaceDestroyed()");
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        player.draw(canvas);
        for (Enemy enemy : enemyList)
        {
            enemy.draw(canvas);
        }
        for (Spell spell : spellList)
        {
            spell.draw(canvas);
        }
        joystick.draw(canvas);
        performance.draw(canvas);


        // Draw Game over if the player is dead (health = 0)
        if (player.getHealthPoints() <= 0) {
            gameOver.draw(canvas);
        }
    }

    public void update() {
        //stop update the game if the player is dead
        if(player.getHealthPoints() <= 0)
        {
            return  ;
        }

        //update game state
        joystick.update();
        player.update();

        //spawn enemy if it is time to spawn new enemy
        if (Enemy.readyToSpawn())
        {
            enemyList.add(new Enemy(getContext(), player));
        }

        //update state of each enemy
        for (Enemy enemy : enemyList)
        {
            enemy.update();
        }

        //update state of each spell
        while (numberOfSpellToCast > 0)
        {
            spellList.add(new Spell(getContext(), player));
            numberOfSpellToCast--;
        }
        for (Spell spell : spellList)
        {
            spell.update();
        }

        // iterate through enemyList and check for collision between each enemy and the player and all spell
        Iterator<Enemy> iteratorEnemy = enemyList.iterator();
        while (iteratorEnemy.hasNext())
        {
            Circle enemy = iteratorEnemy.next();
            if (Circle.isColiding(enemy, player))
            {
                //remove the enemy if it collides with the player
                iteratorEnemy.remove();
                player.setHealthPoints(player.getHealthPoints() - 1);
                continue;
            }
            Iterator<Spell> iteratorSpell = spellList.iterator();
            while(iteratorSpell.hasNext())
            {
                Circle spell = iteratorSpell.next();
                //remove the spell if collide with enemy
                if (Circle.isColiding(spell,enemy))
                {
                    iteratorSpell.remove();
                    iteratorEnemy.remove();
                    break;
                }
            }
        }
    }

    public void pause() {
        gameLoop.stopLoop();
    }
}
