/*
Kyler Geesink
Gregory Shane
William Woods
Daniel Roberts
Garron Grim????(I'm sorry i never got your last name)
*/

package com.cs3a.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

public class StrongTower extends Tower{

    public StrongTower(){
        super(3.5f);
        towerTexture = new Texture(Gdx.files.internal("StrongTower.png"));
        interactionBox = new Rectangle();
        interactionBox.width = 64;
        interactionBox.height = 64;
        damage = 2;
        range = 164;
        bulletSpeed = 20f;
        price = 14;

        attackRange = new Circle();
        attackRange.set(0,0,range);
        attackTimer = 1500000000.0f;

    }

    @Override
    public void upgrade() {
        switch (upgradeLevel) {
            case 0:
                price *= upgradePriceMultiplier;
                upgradeLevel++;
                bulletSpeed += 20f;
                attackTimer *= .65f;
                break;
            case 1:
                price *= upgradePriceMultiplier;
                upgradeLevel++;
                range += 50;
                attackTimer *=.85f;
                attackRange = new Circle(interactionBox.x + 32, interactionBox.y + 32, range);
                break;
            case 2:
                price *= upgradePriceMultiplier;
                upgradeLevel++;
                damage++;
                break;
            default:
                break;
        }
    }

}
