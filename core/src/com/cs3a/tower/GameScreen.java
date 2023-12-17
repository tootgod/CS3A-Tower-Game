package com.cs3a.tower;

import java.util.Iterator;
import java.util.Random;
import java.math.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import org.w3c.dom.Text;
import org.w3c.dom.css.Rect;

public class GameScreen implements Screen {
    final TowerDefence game;

    Texture background;
    Texture menuBackground;
    Texture tempTowerDisplay;
    TiledMap map;
    Vector3 touchPos;
    Array<Enemy> enemies;
    long lastEnemySpawnTime;
    Array<Tower> towers;

    Array<Bullet> bullets;
    long timeBetweenEnemySpawns;
    int wave;
    int money;
    int totalMoney = 50;
    int playerHealth;
    int towerSelector = 1;

    Rectangle baseTower;
    Rectangle longTower;
    Rectangle strongTower;
    Rectangle saveButton;
    Rectangle exitButton;

    Texture baseTowerImg;
    Texture longTowerImg;
    Texture strongTowerImg;
    Texture saveButtonImg;
    Texture exitButtonImg;
    Vector2 imagePosition;

    // control button placement on the menu
    // also ties to the placement of the interaction boxes
    // increases consistency between the two being moved
    float menuTowerX = Gdx.graphics.getWidth() - 148;
    float basicTowerY = Gdx.graphics.getHeight() - 300;
    float longTowerY = Gdx.graphics.getHeight() - 450;
    float strongTowerY = Gdx.graphics.getHeight() - 600;
    float saveX = Gdx.graphics.getWidth() -230;
    float saveY = Gdx.graphics.getHeight() - 1040;
    float exitX = Gdx.graphics.getWidth() - 124;

    boolean isSelectingTower;
    boolean isPlaced;
    int[] pathX = new int[]{-64, 1062 + 32, 1062, 658 - 32, 658, 1664 + 64};
    int[] pathY = new int[]{647, 647, 937 + 32, 937, 129 - 32, 129};
    int[] directionX = new int[]{1, 0, -1, 0, 1};
    int[] directionY = new int[]{0, 1, 0, -1, 0};

    Random rand;

    int enemyHealthSpawnNumbers;

    OrthographicCamera camera;

    public GameScreen(final TowerDefence game) {
        this.game = game;

        background = new Texture(Gdx.files.internal("LevelBackground.png"));
        menuBackground = new Texture(Gdx.files.internal("MenuBackground.png"));
        tempTowerDisplay = new Texture(Gdx.files.internal("BasicTower.png"));
        map = new TiledMap();
        rand = new Random();
        touchPos = new Vector3();
        imagePosition = new Vector2();
        isPlaced = true;


        baseTowerImg = new Texture(Gdx.files.internal("BasicTower.png"));
        baseTower = new Rectangle();
        baseTower.width = 64;
        baseTower.height = 64;
        baseTower.x = menuTowerX;
        baseTower.y = basicTowerY;

        longTowerImg = new Texture(Gdx.files.internal("longTower.png"));
        longTower = new Rectangle();
        longTower.width = 64;
        longTower.height = 64;
        longTower.x = menuTowerX;
        longTower.y = longTowerY;

        strongTowerImg = new Texture(Gdx.files.internal("StrongTower.png"));
        strongTower = new Rectangle();
        strongTower.width = 64;
        strongTower.height = 64;
        strongTower.x = menuTowerX;
        strongTower.y = strongTowerY;

        saveButtonImg = new Texture(Gdx.files.internal("GameScreenSaveImg.png"));
        saveButton = new Rectangle();
        saveButton.width = 96;
        saveButton.height = 64;
        saveButton.x = saveX;
        saveButton.y = saveY;

        exitButtonImg = new Texture(Gdx.files.internal("GameScreenExitImg.png"));
        exitButton = new Rectangle();
        exitButton.width = 96;
        exitButton.height = 64;
        exitButton.x = exitX;
        exitButton.y = saveY;


        //Setup Default Enemy
        enemies = new Array<Enemy>();
        bullets = new Array<Bullet>();

        FileHandle waveData = Gdx.files.local("WaveData");
        FileHandle moneyData = Gdx.files.local("MoneyData");
        FileHandle healthData = Gdx.files.local("HealthData");


        String waveD = waveData.readString();
        String moneyD = moneyData.readString();
        String healthD = healthData.readString();

        int saveWave = Integer.parseInt(waveD);
        int saveMoney = Integer.parseInt(moneyD);
        int saveHealth = Integer.parseInt(healthD);



        timeBetweenEnemySpawns = 1000000000;
        wave = saveWave;
        money = saveMoney;
        playerHealth = saveHealth;

        towers = new Array<Tower>();

        Gdx.input.setInputProcessor(new TowerInputProcessor());

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

    }

    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to clear are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        ScreenUtils.clear(1, 1, 1, 1);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        FileHandle waveData = Gdx.files.local("WaveData");
        FileHandle moneyData = Gdx.files.local("MoneyData");
        FileHandle healthData = Gdx.files.local("HealthData");

        game.batch.begin();
        game.batch.draw(background, 0, 0, 1920, 1080);
        for (Enemy enemy : enemies) {
            game.batch.draw(enemy.enemyImage, enemy.interactionBox.x, enemy.interactionBox.y, enemy.interactionBox.width, enemy.interactionBox.height);
            //game.font.draw(game.batch, "MoneyInside: " + enemy.getMoneyToDrop(), enemy.interactionBox.x ,enemy.interactionBox.y );

        }
        game.font.setColor(Color.BLACK);
        for (Tower tower : towers) {
            game.batch.draw(tower.getTowerTexture(), tower.interactionBox.x, tower.interactionBox.y, tower.interactionBox.width, tower.interactionBox.height);
            game.font.draw(game.batch,"" + tower.upgradeLevel,tower.interactionBox.x,tower.interactionBox.y);
            if(tower.upgradeLevel < 3)
                game.font.draw(game.batch,"Price: "  + (int)(tower.price * tower.upgradePriceMultiplier),tower.interactionBox.x,tower.interactionBox.y + 32);
        }
        for (Bullet bullet : bullets) {
            game.batch.draw(bullet.bulletTexture, bullet.interactionBox.x, bullet.interactionBox.y, bullet.interactionBox.width, bullet.interactionBox.height);
        }

        game.batch.draw(menuBackground, 1664, 0);


        if(money >= 10)
        {
            game.font.setColor(Color.GREEN);
        }
        else
        {
            game.font.setColor(Color.RED);
        }
        game.font.draw(game.batch,"Cost: 10", menuTowerX, basicTowerY - 20);

        if(money >= 12)
        {
            game.font.setColor(Color.GREEN);
        }
        else
        {
            game.font.setColor(Color.RED);
        }
        game.font.draw(game.batch,"Cost: 12", menuTowerX, longTowerY - 20);

        if(money >= 14)
        {
            game.font.setColor(Color.GREEN);
        }
        else
        {
            game.font.setColor(Color.RED);
        }
        game.font.draw(game.batch,"Cost: 14", menuTowerX, strongTowerY - 20);

        game.batch.draw(baseTowerImg, menuTowerX, basicTowerY);
        game.batch.draw(strongTowerImg, menuTowerX, strongTowerY);
        game.batch.draw(longTowerImg, menuTowerX, longTowerY);
        game.batch.draw(saveButtonImg, saveX, saveY);
        game.batch.draw(exitButtonImg, exitX, saveY);


        if(isSelectingTower) {
            game.batch.draw(tempTowerDisplay, imagePosition.x, imagePosition.y);
        }

        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, "Wave: " + (wave - 1), Gdx.graphics.getWidth() - 185, Gdx.graphics.getHeight() - 100);
        game.font.draw(game.batch, "Enemies to Spawn: " + enemyHealthSpawnNumbers, Gdx.graphics.getWidth() - 185, Gdx.graphics.getHeight() - 115);
        game.font.draw(game.batch, "Money: " + money, Gdx.graphics.getWidth() - 185, Gdx.graphics.getHeight() - 130);
        game.font.draw(game.batch, "Health: " + playerHealth, Gdx.graphics.getWidth() - 185, Gdx.graphics.getHeight() - 145);
        game.batch.end();


        if (Gdx.input.justTouched()) {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();

            if(exitButton.contains(x, y)){
                Gdx.app.exit();
            }
            if(saveButton.contains(x, y)){
                waveData.writeString(Integer.toString(wave), false);
                moneyData.writeString(Integer.toString(totalMoney), false);
                healthData.writeString(Integer.toString(playerHealth), false);
            }

            if (baseTower.contains(x, y) && money >= 10) {
                towerSelector = 1;
                tempTowerDisplay = new Texture(Gdx.files.internal("BasicTower.png"));
                isSelectingTower = true;
            } else if (strongTower.contains(x, y) && money >= 12) {
                towerSelector = 2;
                tempTowerDisplay = new Texture(Gdx.files.internal("StrongTower.png"));
                isSelectingTower = true;
            } else if (longTower.contains(x, y) && money >= 14) {
                towerSelector = 3;
                tempTowerDisplay = new Texture(Gdx.files.internal("LongTower.png"));
                isSelectingTower = true;
            } else {
                isSelectingTower = false;
            }
            for(Tower tower : towers) {
                if (tower.interactionBox.contains(x,y) && tower.upgradeLevel != 3  &&tower.upgradeLevel > -1 && money > (tower.price * tower.upgradePriceMultiplier)) {
                    money -= (int) (tower.price * tower.upgradePriceMultiplier);
                    tower.upgrade();
                }
            }
        }

        if (isSelectingTower) {
            imagePosition.set(Gdx.input.getX() - getImageWidth() / 2, Gdx.graphics.getHeight() - Gdx.input.getY() - getImageHeight() / 2);
        }

        if(Gdx.input.isTouched() && isPlaced)
        {
            isSelectingTower = false;
        }

        Iterator<Bullet> bulletIterator;
        bulletIterator = bullets.iterator();

        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.bulletAi();
        }

        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            bulletIterator = bullets.iterator();
            Enemy enemy = enemyIterator.next();
            enemy.enemyAi();

            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();
                if (!Intersector.overlaps(bullet.homeTower.attackRange, bullet.interactionBox)) {
                    bullet.stop();
                    bulletIterator.remove();
                }
                if (bullet.checkHit(enemy) && bullet.canDamage) {
                    enemy.removeHealth(bullet.damage);
                    bullet.hide();
                }

            }
            if (enemy.getHealth() <= 0) {
                money += enemy.getMoneyToDrop();
                totalMoney += enemy.getMoneyToDrop();
                enemyIterator.remove();
                break;
            }

            if (enemy.hasReachedEndOfPath()) {
                playerHealth -= enemy.getHealth();

                enemyIterator.remove();

                if (playerHealth <= 0) {
                    game.setScreen((new GameOverScreen(game)));
                }
            }


            for (Tower tower : towers) {
                //tower.attackUpdate(delta, enemy);

                if (tower.isEnemyInRange(enemy) && tower.canFire(enemy)) {
                    //enemy.removeHealth(tower.getDamage());
                    spawnBullet(enemy, tower);

                }
                if(tower.upgradeLevel < 0)
                {
                    tower.upgradeLevel = 0;
                }
            }

        }
        for (Tower tower : towers) {
            if(tower.upgradeLevel < 0)
            {
                tower.upgradeLevel = 0;
            }
        }
        if(TimeUtils.nanoTime() - lastEnemySpawnTime > timeBetweenEnemySpawns && enemyHealthSpawnNumbers > 0){
            int enemyHealthModifier = 3;
            if (wave > 10)
            {
                enemyHealthModifier = 4;
            }
            float i = rand.nextInt(enemyHealthModifier) + 1;
            if (wave > 5)
            {
                i += wave * .1f;
            }
            if(i > enemyHealthModifier )
            {
                i = enemyHealthModifier;
            }

            if(enemyHealthSpawnNumbers >= i) {
                spawnEnemy((int) i);
            } else {
                spawnEnemy(enemyHealthSpawnNumbers);
            }
        }


        boolean isPressed = Gdx.input.isKeyJustPressed(Keys.SPACE);
        if (isPressed && enemies.isEmpty()){
            processWaveStart();
        }

    }

    private int getImageWidth() {
        switch (towerSelector) {
            case 1:
                return baseTowerImg.getWidth();
            case 2:
                return strongTowerImg.getWidth();
            case 3:
                return longTowerImg.getWidth();
            default:
                return 0;
        }
    }

    private int getImageHeight() {
        switch (towerSelector) {
            case 1:
                return baseTowerImg.getHeight();
            case 2:
                return strongTowerImg.getHeight();
            case 3:
                return longTowerImg.getHeight();
            default:
                return 0;
        }
    }

    private class TowerInputProcessor extends com.badlogic.gdx.InputAdapter {
        public boolean touchDown(int screenX, int screenY, int pointer, int button)
        {
            int[][] area = map.getMap();
            int mapWidth = area.length;
            int mapHeight = area[0].length;

            int towerWidth = 30;
            int towerHeight = 30;

            if (money >= getSelectedTowerPrice()) {
                if (screenX >= towerWidth && screenX < mapWidth - towerWidth * 2 && screenY >= towerHeight && screenY < mapHeight - towerHeight * 2 &&
                        area[screenX][screenY] == 1 && area[screenX + towerWidth][screenY] == 1 &&  area[screenX - towerWidth][screenY] == 1 &&  area[screenX][screenY + towerHeight] == 1 &&  area[screenX][screenY - towerHeight] == 1) {

                    touchPos.set(screenX, screenY, 0);
                    camera.unproject(touchPos);
                    Tower tower = createSelectedTower();
                    if (canPlaceTower(area, screenX, screenY, towerWidth, towerHeight)) {
                        money -= tower.price;
                        tower.setPosition(touchPos.x - 32, touchPos.y - 32);
                        towers.add(tower);

                        // Prevent towers from overlapping and update area
                        updateAreaWithTower(area, screenX, screenY, towerWidth, towerHeight);
                        isPlaced = true;
                        return isPlaced;
                    }
                }
            }
            isPlaced = false;
            return isPlaced;
        }

        private boolean canPlaceTower(int[][] area, int screenX, int screenY, int towerWidth, int towerHeight) {
            // Check if the tower placement is within map boundaries and not on the pathway
            for (int i = screenX - towerWidth; i < screenX + towerWidth; i++) {
                for (int j = screenY - towerHeight; j < screenY + towerHeight; j++) {
                    if (area[i][j] != 1) {
                        return false;
                    }
                }
            }
            return true;
        }

        private void updateAreaWithTower(int[][] area, int screenX, int screenY, int towerWidth, int towerHeight) {
            // Update the area to denote the tower's placement
            for (int i = screenX - towerWidth; i < screenX + towerWidth; i++) {
                for (int j = screenY - towerHeight; j < screenY + towerHeight; j++) {
                    area[i][j] = -1;
                }
            }
        }

        private int getSelectedTowerPrice() {
            switch (towerSelector) {
                case 1: return new BasicTower().price;
                case 2: return new StrongTower().price;
                case 3: return new LongTower().price;
                default: return new BasicTower().price;
            }
        }

        private Tower createSelectedTower() {
            switch (towerSelector) {
                case 1: return new BasicTower();
                case 2: return new StrongTower();
                case 3: return new LongTower();
                default: return new BasicTower();
            }
        }
    }



    private void spawnEnemy(int health) {
        float speed = 400 + (wave * 3);
        Enemy enemy = new Enemy(pathX,pathY,directionX,directionY,health,speed);
        enemies.add(enemy);
        lastEnemySpawnTime = TimeUtils.nanoTime();
        enemyHealthSpawnNumbers-= health;
    }

    private void spawnBullet(Enemy enemy, Tower tower)
    {
        Bullet bullet = new Bullet(tower.interactionBox.x + 10, tower.interactionBox.y + 10, tower.bulletSpeed, tower.getDamage(), enemy.interactionBox.x + 32,enemy.interactionBox.y + 32, tower);
        bullets.add(bullet);

    }

    private void processWaveStart(){
        enemyHealthSpawnNumbers += (wave + 3) * ((wave + 3) % 1000);
        timeBetweenEnemySpawns = 1000000000 / (long) wave;
        wave++;
    }


    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        for(Tower tower: towers)
        {
            tower.dispose();
        }

    }
}
