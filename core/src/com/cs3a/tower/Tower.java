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

public class Tower
{
    private Texture towerTexture;

    private Rectangle interactionBox;
    private Circle attackRange;
    private Vector2 position;
    private int damage;
    private float range;

    private float attackTimer;
    private float timeSinceLastAttack;

    public Tower()
    {
        towerTexture = new Texture("Tower.png");
        interactionBox = new Rectangle();
        interactionBox.width = 64;
        interactionBox.height = 64;
        position = new Vector2(0,0);
        damage = 1;
        range = 64;

        attackRange = new Circle();
        attackRange.set(0,0,range);
        attackTimer = 1000000000.0f;

    }

    public void setPosition(float x, float y) {
        float halfWidth = (float) towerTexture.getWidth() / 2;
        float halfHeight = (float) towerTexture.getHeight() / 2;

        position.set(x - halfWidth, y - halfHeight);

        interactionBox = new Rectangle(position.x, position.y, towerTexture.getWidth(), towerTexture.getHeight());

        attackRange = new Circle(position.x + halfWidth - range / 2, position.y + halfHeight - range / 2, range);
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

    public Vector2 getPosition()
    {
        return position;
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

        if(TimeUtils.nanoTime() - timeSinceLastAttack > 1000000000){
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
