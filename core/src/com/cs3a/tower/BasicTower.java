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
import org.w3c.dom.Text;

public class BasicTower extends Tower {
    public BasicTower(){
        super(3.5f);
        towerTexture = new Texture(Gdx.files.internal("BasicTower.png"));
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

    @Override
    public void upgrade() {
        switch (upgradeLevel) {
            case 0:
                price *= upgradePriceMultiplier;
                upgradeLevel++;
                attackTimer *= 0.5f;
                break;
            case 1:
                price *= upgradePriceMultiplier;
                upgradeLevel++;
                attackTimer *= 0.85f;
                bulletSpeed += 10f;
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
