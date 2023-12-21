package com.cs3a.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

public class LongTower extends Tower {
    public LongTower(){
        super(1.5f);
        towerTexture = new Texture(Gdx.files.internal("LongTower.png"));
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

    @Override
    public void upgrade() {
        switch (upgradeLevel) {
            case 0:
                price *= upgradePriceMultiplier;
                upgradeLevel++;
                range += 150;
                attackTimer *=.5f;
                attackRange = new Circle(interactionBox.x + 32, interactionBox.y + 32, range);

                break;
            case 1:
                price *= upgradePriceMultiplier;
                upgradeLevel++;
                range += 150;
                attackTimer *=.75f;
                attackRange = new Circle(interactionBox.x + 32, interactionBox.y + 32, range);
                break;
            case 2:
                price *= upgradePriceMultiplier;
                upgradeLevel++;
                range += 150;
                attackRange = new Circle(interactionBox.x + 32, interactionBox.y + 32, range);
                damage++;
                break;
            default:
                break;
        }
    }

}
