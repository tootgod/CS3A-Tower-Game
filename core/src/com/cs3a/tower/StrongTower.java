package com.cs3a.tower;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

public class StrongTower extends Tower{

    public StrongTower(){
        towerTexture = new Texture("StrongTower.png");
        interactionBox = new Rectangle();
        interactionBox.width = 64;
        interactionBox.height = 64;
        damage = 2;
        range = 164;
        bulletSpeed = 20f;

        attackRange = new Circle();
        attackRange.set(0,0,range);
        attackTimer = 2000000000.0f;

    }
}
