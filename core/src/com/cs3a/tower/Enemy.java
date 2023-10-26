package com.cs3a.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.awt.*;

public class Enemy {
    public Texture enemyImage;
    public int health;
    public Rectangle interactionBox;

    public Enemy(){
        enemyImage = new Texture(Gdx.files.internal("BlueSquare.png"));
        health = 1;
        interactionBox = new Rectangle();
        interactionBox.width = 64;
        interactionBox.height = 64;
        interactionBox.x = 0;
        interactionBox.y = 1080/2;

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
