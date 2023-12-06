package com.cs3a.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

import java.awt.*;

public class  Tower
{
    protected Texture towerTexture;

    public Rectangle interactionBox;
    public Circle attackRange;
    protected int damage;
    protected float range;

    protected float attackTimer;
    protected float timeSinceLastAttack;

    public Tower()
    {
        towerTexture = new Texture("Tower.png");
        interactionBox = new Rectangle();
        interactionBox.width = 64;
        interactionBox.height = 64;
        damage = 1;
        range = 164;

        attackRange = new Circle();
        attackRange.set(0,0,range);
        attackTimer = 1000000000.0f;

    }

    public void setPosition(float x, float y) {

        interactionBox = new Rectangle(x,y,towerTexture.getWidth(), towerTexture.getHeight());

        attackRange = new Circle(x + 32, y + 32, range);
    }

    public Texture getTowerTexture()
    {
        return towerTexture;
    }

    public Rectangle getInteractionBox() {
        return interactionBox;
    }

    public int getDamage()
    {
        return damage;
    }

    public double getRange()
    {
        return range;
    }


    public void setDamage(int damage)
    {
        this.damage = damage;
    }

    public void setRange(float range)
    {
        this.range = range;
    }

    public boolean canFire(Enemy enemy)
    {

        if(TimeUtils.nanoTime() - timeSinceLastAttack > attackTimer){
            timeSinceLastAttack = TimeUtils.nanoTime();
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isEnemyInRange(Enemy enemy)
    {
        return Intersector.overlaps(attackRange, enemy.interactionBox);
    }

    public void dispose()
    {
        towerTexture.dispose();
    }
}
