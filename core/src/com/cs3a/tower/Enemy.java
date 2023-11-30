package com.cs3a.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
public class Enemy {
    public Texture enemyImage;
    public int health;
    public Rectangle interactionBox;

    public int[] pathX;
    public int[] pathY;

    public int[] directionX;
    public int[] directionY;
    public int whatPoint;
    int[][] map;


    public Enemy() {
        enemyImage = new Texture(Gdx.files.internal("BlueSquare.png"));
        health = 1;
        interactionBox = new Rectangle();
        interactionBox.width = 64;
        interactionBox.height = 64;
        interactionBox.x = 0;
        interactionBox.y = 1080 / 2;
        whatPoint = 0;
    }
  
    public Enemy(int[] pathX, int[] pathY,int[] directionX, int[] directionY){
        health = 1;
        setEnemyImage();
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

    public Enemy(int[] pathX, int[] pathY,int[] directionX, int[] directionY,int health){
        this.health = health;
        setEnemyImage();
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

    public Enemy(int health, String texturePath) {
        enemyImage = new Texture(Gdx.files.internal(texturePath));
        this.health = health;
    }

    //Health should only be values of 3-0. Please don't set to anything but that. It shouldn't break but you
    //never know
    public void removeHealth(int damage) {
        health -= damage;
        System.out.println("current health: " + health);
        setEnemyImage();
    }

    public void setEnemyImage() {

        if(this.health >= 3){
            enemyImage = new Texture(Gdx.files.internal("RedSquare.png"));
        }
        else if(this.health == 2)
        {
            enemyImage = new Texture(Gdx.files.internal("PurpleSquare.png"));
        }
        else{
            enemyImage = new Texture(Gdx.files.internal("BlueSquare.png"));
        }


    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health){

        this.health = health;
        setEnemyImage();

    }

    public void enemyAi() {
        interactionBox.x += directionX[whatPoint] * Gdx.graphics.getDeltaTime();
        interactionBox.y += directionY[whatPoint] * Gdx.graphics.getDeltaTime();

        if (interactionBox.contains(pathX[whatPoint + 1], pathY[whatPoint + 1])) {
            if (whatPoint + 1 == directionY.length) {
                whatPoint = 0;
                interactionBox.x = pathX[whatPoint] - 32;
                interactionBox.y = pathY[whatPoint] - 32;
            } else {
                whatPoint++;
            }
        }
    }


    public Vector2 getPosition() {
        float centerX = interactionBox.x + interactionBox.width / 2;
        float centerY = interactionBox.y + interactionBox.height / 2;
        return new Vector2(centerX, centerY);
    }

}
