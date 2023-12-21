package com.cs3a.tower;

import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
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

    Texture upgradeButton;
    Rectangle upgrade;
    Texture sellButton;
    Rectangle sell;
    Texture displayTower;
    boolean selected;

    Tower shownTower;
    TiledMap map;
    Vector3 touchPos;
    Array<Enemy> enemies;
    long lastEnemySpawnTime;
    Array<Tower> towers;

    Array<Bullet> bullets;
    long timeBetweenEnemySpawns;
    int wave;
    int money;
    int playerHealth;
    int towerSelector = 1;

    Rectangle baseTower;
    Rectangle longTower;
    Rectangle strongTower;

    Texture baseTowerImg;
    Texture longTowerImg;
    Texture strongTowerImg;
    Vector2 imagePosition;

    // control button placement on the menu
    // also ties to the placement of the interaction boxes
    // increases consistency between the two being moved
    float menuTowerX = Gdx.graphics.getWidth() - 148;
    float basicTowerY = Gdx.graphics.getHeight() - 300;
    float longTowerY = Gdx.graphics.getHeight() - 450;
    float strongTowerY = Gdx.graphics.getHeight() - 600;

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

        sellButton = new Texture(Gdx.files.internal("Sell.png"));
        sell = new Rectangle();
        sell.width = 64;
        sell.height = 32;
        sell.x = menuTowerX - 40;
        sell.y = Gdx.graphics.getHeight() - 850;

        upgradeButton = new Texture(Gdx.files.internal("Upgrade.png"));
        upgrade = new Rectangle();
        upgrade.width = 64;
        upgrade.height = 32;
        upgrade.x = menuTowerX + 40;
        upgrade.y = Gdx.graphics.getHeight() - 850;


        isPlaced = true;
        selected = false;

        //Setup Default Enemy
        enemies = new Array<Enemy>();
        bullets = new Array<Bullet>();

        timeBetweenEnemySpawns = 1000000000;
        wave = 1;
        money = 50;
        playerHealth = 200;

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

        game.batch.begin();
        game.batch.draw(background, 0, 0, 1920, 1080);
        for (Enemy enemy : enemies) {
            game.batch.draw(enemy.enemyImage, enemy.interactionBox.x, enemy.interactionBox.y, enemy.interactionBox.width, enemy.interactionBox.height);
            //game.font.draw(game.batch, "MoneyInside: " + enemy.getMoneyToDrop(), enemy.interactionBox.x ,enemy.interactionBox.y );
        }

        game.font.setColor(Color.BLACK);
        for (Tower tower : towers) {
            game.batch.draw(tower.getTowerTexture(), tower.interactionBox.x, tower.interactionBox.y, tower.interactionBox.width, tower.interactionBox.height);

        }
        for (Bullet bullet : bullets) {
            game.batch.draw(bullet.bulletTexture, bullet.interactionBox.x, bullet.interactionBox.y, bullet.interactionBox.width, bullet.interactionBox.height);
        }

        game.batch.draw(menuBackground, 1664, 0);
        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, "Wave: " + (wave - 1), Gdx.graphics.getWidth() - 185, Gdx.graphics.getHeight() - 100);
        game.font.draw(game.batch, "Enemies to Spawn: " + enemyHealthSpawnNumbers, Gdx.graphics.getWidth() - 185, Gdx.graphics.getHeight() - 115);
        game.font.draw(game.batch, "Money: " + money, Gdx.graphics.getWidth() - 185, Gdx.graphics.getHeight() - 130);
        game.font.draw(game.batch, "Health: " + playerHealth, Gdx.graphics.getWidth() - 185, Gdx.graphics.getHeight() - 145);

        game.batch.draw(baseTowerImg, menuTowerX, basicTowerY);
        if(money >= 10) {
            game.font.setColor(Color.GREEN);
        } else {
            game.font.setColor(Color.RED);
        }
        game.font.draw(game.batch,"Cost: 10", menuTowerX, basicTowerY - 20);

        game.batch.draw(strongTowerImg, menuTowerX, strongTowerY);
        if(money >= 12) {
            game.font.setColor(Color.GREEN);
        } else {
            game.font.setColor(Color.RED);
        }
        game.font.draw(game.batch,"Cost: 12", menuTowerX, longTowerY - 20);

        game.batch.draw(longTowerImg, menuTowerX, longTowerY);
        if(money >= 14) {
            game.font.setColor(Color.GREEN);
        } else {
            game.font.setColor(Color.RED);
        }
        game.font.draw(game.batch,"Cost: 14", menuTowerX, strongTowerY - 20);

        // for dynamically drawing when tower is selected
        if(isSelectingTower) {
            game.batch.draw(tempTowerDisplay, imagePosition.x, imagePosition.y);
        }

        // For displaying a specific tower is selected
        if(selected) {
            game.font.draw(game.batch,"Level: " + shownTower.upgradeLevel,menuTowerX, Gdx.graphics.getHeight() - 670);
            game.batch.draw(displayTower, menuTowerX, Gdx.graphics.getHeight() - 750);

            if(shownTower.upgradeLevel < 3)
                game.font.draw(game.batch,"Price: "  + (int)(shownTower.price * shownTower.upgradePriceMultiplier), menuTowerX,Gdx.graphics.getHeight() - 760);

            game.batch.draw(sellButton, menuTowerX - 32, Gdx.graphics.getHeight() - 850);
            game.batch.draw(upgradeButton, menuTowerX + 32, Gdx.graphics.getHeight() - 850);
        }
        game.batch.end();


        if (Gdx.input.justTouched()) {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();

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

            Iterator <Tower> towerIterator = towers.iterator();
            while(towerIterator.hasNext())
            {
                Tower tower = towerIterator.next();

                if(tower.interactionBox.contains(x,y))
                {
                    displayTower = tower.getTowerTexture();
                    selected = true;
                    shownTower = tower;
                }

                if(sell.contains(x,y))
                {
                    money += shownTower.price + (int) (shownTower.price / shownTower.upgradePriceMultiplier * shownTower.upgradeLevel);
                    selected = false;
                    towers.removeValue(shownTower,true);
                    break;
                }

                if(upgrade.contains(x,y) && shownTower.upgradeLevel != 3  &&shownTower.upgradeLevel > -1 && money > (tower.timeSinceLastAttack * tower.upgradePriceMultiplier))
                {
                    money -= (int) (shownTower.price * shownTower.upgradePriceMultiplier);
                    shownTower.upgrade();

                }
            }

        }

        if (isSelectingTower) {
            imagePosition.set(Gdx.input.getX() - getImageWidth() / 2, Gdx.graphics.getHeight() - Gdx.input.getY() - getImageHeight() / 2);
        }

        if(Gdx.input.isTouched() && isPlaced)
        {
            isSelectingTower = false;
            selected = false;
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

    // check bug for towerplacement
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
