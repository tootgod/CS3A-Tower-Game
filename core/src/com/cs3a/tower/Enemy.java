package com.cs3a.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

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

    public Enemy(){
        enemyImage = new Texture(Gdx.files.internal("BlueSquare.png"));
        health = 1;
        interactionBox = new Rectangle();
        interactionBox.width = 64;
        interactionBox.height = 64;
        interactionBox.x = 0;
        interactionBox.y = 1080/2;
        directionX = new int[]{400};
        directionY = new int[]{0};
        whatPoint = 0;


    }
    public Enemy(int[] pathX, int[] pathY,int[] directionX, int[] directionY){
        enemyImage = new Texture(Gdx.files.internal("BlueSquare.png"));
        health = 1;
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

    }
    public Enemy(int health)
    {
        enemyImage = new Texture(Gdx.files.internal("BlueSquare.png"));
        this.health = health;
    }

    public Enemy(int health, String texturePath)
    {
        enemyImage = new Texture(Gdx.files.internal(texturePath));
        this.health = health;
    }


}
