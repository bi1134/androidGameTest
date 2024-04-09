package com.example.gametest.map;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.gametest.graphics.Sprite;
import com.example.gametest.graphics.SpriteSheet;

public class GrassyTile extends Tile {
    private final Sprite sprite;

    public GrassyTile(SpriteSheet spriteSheet, Rect mapLocationRect) {
        super(mapLocationRect);
        sprite = spriteSheet.getGrassySprite();
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas, mapLocationRect.left, mapLocationRect.top);
    }
}
