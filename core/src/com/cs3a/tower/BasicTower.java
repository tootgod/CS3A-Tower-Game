package com.cs3a.tower;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BasicTower extends Tower {
    public BasicTower(){
        towerTexture = new Texture("BasicTower.png");
        interactionBox = new Rectangle();
        interactionBox.width = 64;
        interactionBox.height = 64;
        damage = 1;
        range = 164;
        bulletSpeed = 20f;
        price = 10;

        attackRange = new Circle();
        attackRange.set(0,0,range);
        attackTimer = 1000000000.0f;

    }
}
