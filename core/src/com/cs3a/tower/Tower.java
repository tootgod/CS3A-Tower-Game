package com.cs3a.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.awt.*;

public class Tower
{
    private Texture towerTexture;
    private Vector2 position;
    private int damage;
    private double range;

    private float attackTimer;
    private float timeSinceLastAttack;

    public Tower(int damage, double range, float delta)
    {
        towerTexture = new Texture("Tower.png");
        position = new Vector2(0, 0);
        this.damage = damage;
        this.range = range;
        this.attackTimer = 4.0f;
        this.timeSinceLastAttack = 0.0f;

    }

    public void setPosition(float x, float y)
    {
        position.set(x - (float) towerTexture.getWidth() / 2, y - (float) towerTexture.getHeight() / 2);
    }

    public void render(SpriteBatch batch)
    {
        batch.begin();
        batch.draw(towerTexture, position.x, position.y);
        batch.end();
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

    public void setRange(double range)
    {
        this.range = range;
    }

    public void attackUpdate(float delta, Enemy enemy)
    {
        timeSinceLastAttack += delta;

        if(timeSinceLastAttack > attackTimer && isEnemyInRange(this, enemy))
        {
            timeSinceLastAttack = 0;
        }
    }

    public boolean isEnemyInRange(Tower tower, Enemy enemy)
    {
        Vector2 towerPosition = tower.getPosition();
        Vector2 enemyPosition = enemy.getPosition();

        double distance = towerPosition.dst(enemyPosition);
        System.out.println("Distance is: " + distance);
        return distance <= tower.getRange();
    }

    public void dispose()
    {
        towerTexture.dispose();
    }
}
