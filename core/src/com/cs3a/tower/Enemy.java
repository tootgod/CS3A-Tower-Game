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
    private int moneyToDrop;

    boolean reachedEndOfPath;
    float speed;


    public Enemy() {
        enemyImage = new Texture(Gdx.files.internal("BlueSquare.png"));
        health = 1;
        moneyToDrop = health;
        interactionBox = new Rectangle();
        interactionBox.width = 64;
        interactionBox.height = 64;
        interactionBox.x = 0;
        interactionBox.y = 1080 / 2;
        whatPoint = 0;
        speed = 400;
    }
  
    public Enemy(int[] pathX, int[] pathY,int[] directionX, int[] directionY){
        health = 1;
        moneyToDrop = health;
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

    public Enemy(int[] pathX, int[] pathY,int[] directionX, int[] directionY,int health, float speed){
        this.health = health;
        moneyToDrop = health;
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
        reachedEndOfPath = false;
        this.speed = speed;

    }

    public Enemy(int health, String texturePath) {
        enemyImage = new Texture(Gdx.files.internal(texturePath));
        this.health = health;
    }

    //Health should only be values of 3-0. Please don't set to anything but that. It shouldn't break but you
    //never know
    public void removeHealth(int damage) {
        health -= damage;
        //System.out.println("current health: " + health);
        setEnemyImage();
    }

    public void setEnemyImage() {

        if(this.health >= 4){
            enemyImage = new Texture(Gdx.files.internal("GreenSquare.png"));
        }
        else if(this.health == 3){
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
        interactionBox.x += directionX[whatPoint] * Gdx.graphics.getDeltaTime() * speed;
        interactionBox.y += directionY[whatPoint] * Gdx.graphics.getDeltaTime() * speed;

        if (interactionBox.contains(pathX[whatPoint + 1], pathY[whatPoint + 1])) {
            if (whatPoint + 1 == directionY.length) {
                reachedEndOfPath = true;
            } else {
                whatPoint++;
            }
        } else {
            reachedEndOfPath = false;
        }
    }

    public boolean hasReachedEndOfPath()
    {
        return reachedEndOfPath;
    }

    public int getMoneyToDrop(){return moneyToDrop;}



    public Vector2 getPosition() {
        float centerX = interactionBox.x + interactionBox.width / 2;
        float centerY = interactionBox.y + interactionBox.height / 2;
        return new Vector2(centerX, centerY);
    }

}
