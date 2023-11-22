package com.cs3a.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.awt.*;

public class Enemy {
    public Texture enemyImage;
    public int health;
    public Rectangle interactionBox;

    public int[] pathX;
    public int[] pathY;

    public int[] directionX;
    public int[] directionY;
    public int whatPoint;
    private Vector2 position;

    public Enemy()
    {
        enemyImage = new Texture(Gdx.files.internal("BlueSquare.png"));
        health = 3;
        interactionBox = new Rectangle();
        interactionBox.width = 64;
        interactionBox.height = 64;
        interactionBox.x = 0;
        interactionBox.y = 1080/2;
        directionX = new int[]{400};
        directionY = new int[]{0};
        whatPoint = 0;
        position = new Vector2(pathX[0], pathY[0]);
    }
    public Enemy(int[] pathX, int[] pathY,int[] directionX, int[] directionY)
    {
        health = 3;
        // setEnemyImage();
        enemyImage = new Texture(Gdx.files.internal("BlueSquare.png"));
        interactionBox = new Rectangle();
        interactionBox.width = 64;
        interactionBox.height = 64;
        interactionBox.x = pathX[0] - 32;
        interactionBox.y = pathY[0] - 32;
        this.pathX = pathX;
        this.pathY = pathY;
        this.directionX = directionX;
        this.directionY = directionY;
        whatPoint = 0;
        position = new Vector2(pathX[0], pathY[0]);

    }
    public Enemy(int[] pathX, int[] pathY,int[] directionX, int[] directionY,int health)
    {
        this.health = health;
        // setEnemyImage();
        enemyImage = new Texture(Gdx.files.internal("BlueSquare.png"));
        interactionBox = new Rectangle();
        interactionBox.width = 64;
        interactionBox.height = 64;
        interactionBox.x = pathX[0] - 32;
        interactionBox.y = pathY[0] - 32;
        this.pathX = pathX;
        this.pathY = pathY;
        this.directionX = directionX;
        this.directionY = directionY;
        whatPoint = 0;
        position = new Vector2(pathX[0], pathY[0]);
    }

    public Enemy(int health, String texturePath)
    {
        enemyImage = new Texture(Gdx.files.internal(texturePath));
        this.health = health;
    }

    public void setEnemyImage()
    {
        if (this.health >= 3)
        {
            enemyImage = new Texture(Gdx.files.internal("RedSquare.png"));
        }
        else if (this.health == 2)
        {
            enemyImage = new Texture(Gdx.files.internal("PurpleSquare.png"));
        }
        else
        {
            enemyImage = new Texture(Gdx.files.internal("BlueSquare.png"));
        }
    }
        public Vector2 getPosition()
    {
        return position;
    }

    public int getHealth()
    {
        return health;
    }


    public void takeDamage(int damage)
    {
        health -= damage;
        setEnemyImage();

    }

    public void enemyAi()
    {
        if (whatPoint < directionX.length && whatPoint < directionY.length)
        {
            position.x += directionX[whatPoint] * Gdx.graphics.getDeltaTime();
            position.y += directionY[whatPoint] * Gdx.graphics.getDeltaTime();

            if (interactionBox.contains(pathX[whatPoint + 1], pathY[whatPoint + 1]))
            {
                if (whatPoint + 1 == directionY.length)
                {
                    whatPoint = 0;
                    interactionBox.x = pathX[whatPoint] - 32;
                    interactionBox.y = pathY[whatPoint] - 32;
                } else
                {
                    whatPoint++;
                }
            }
        }
    }

    public void dispose()
    {
        enemyImage.dispose();
    }
}
