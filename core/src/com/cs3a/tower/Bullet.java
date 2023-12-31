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
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.awt.*;

import static java.lang.Math.scalb;
import static java.lang.Math.sqrt;

public class Bullet {
    Tower homeTower;
    public Texture bulletTexture;
    public Rectangle interactionBox;
    public int damage;

    public float xAmt;
    public float yAmt;
    public boolean canDamage;


    public Bullet(float x, float y, float speed, int damage,float goalX, float goalY,Tower tower){
        bulletTexture = new Texture(Gdx.files.internal("Bullet.png"));
        interactionBox = new Rectangle(x,y,8,8);
        this.damage = damage;

        canDamage = true;

        xAmt = (goalX - interactionBox.x);
        yAmt = (goalY - interactionBox.y);

        double dist = sqrt((xAmt * xAmt) + (yAmt * yAmt));

        xAmt = xAmt /(float) dist;
        yAmt = yAmt /(float) dist;

        xAmt *= speed;
        yAmt *= speed;
        //System.out.println(xAmt + " ," + yAmt);

        homeTower = tower;
    }

    public void bulletAi(){
        interactionBox.x += xAmt;
        interactionBox.y += yAmt;
    }

    public boolean checkHit(Enemy enemy)
    { return Intersector.overlaps(interactionBox,enemy.interactionBox);
    }
    public void stop(){
        xAmt = 0;
        yAmt = 0;
    }
    public void hide()
    {
        canDamage = false;
        bulletTexture = new Texture(Gdx.files.internal("Transparent.png"));
    }
}
