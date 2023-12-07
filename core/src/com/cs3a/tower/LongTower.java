package com.cs3a.tower;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

public class LongTower extends Tower {
    public LongTower(){
        towerTexture = new Texture("LongTower.png");
        interactionBox = new Rectangle();
        interactionBox.width = 64;
        interactionBox.height = 64;
        damage = 1;
        range = 464;
        bulletSpeed = 80f;
        price = 12;

        attackRange = new Circle();
        attackRange.set(0,0,range);
        attackTimer = 25000000000.0f;

    }
}
