package com.cs3a.tower;

import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import org.w3c.dom.Text;

public class GameScreen implements Screen {
    final TowerDefence game;

    Texture background;
    Texture menuBackground;
    Texture tempTowerDisplay;
    TiledMap map;
    Vector3 touchPos;
    SpriteBatch batch;

    Array<Enemy> enemies;
    long lastEnemySpawnTime;
    Array<Tower> towers;

    Array<Bullet> bullets;
    boolean isPlacing;
    long timeBetweenEnemySpawns;
    int wave;
    int money;
    int playerHealth;
    int towerSelector = 1;

    int[] pathX = new int[]{-64, 1062 + 32, 1062, 658 - 32, 658, 1664 + 64};
    int[] pathY = new int[]{647, 647, 937 + 32, 937, 129 - 32, 129};
    int[] directionX = new int[]{300, 0, -300, 0, 300};
    int[] directionY = new int[]{0, 300, 0, -300, 0};

    Random rand;

    boolean shown = false;

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
        batch = new SpriteBatch();

        //Setup Default Enemy
        enemies = new Array<Enemy>();
        bullets = new Array<Bullet>();

        timeBetweenEnemySpawns = 1000000000;
        wave = 1;
        money = 50;
        playerHealth = 200;

        towers = new Array<Tower>();
        isPlacing = true;

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

        for (Tower tower : towers) {
            game.batch.draw(tower.getTowerTexture(), tower.interactionBox.x, tower.interactionBox.y,tower.interactionBox.width,tower.interactionBox.height);
        }
        for(Bullet bullet : bullets)
        {
            game.batch.draw(bullet.bulletTexture, bullet.interactionBox.x, bullet.interactionBox.y,  bullet.interactionBox.width,  bullet.interactionBox.height);
        }

        game.batch.draw(menuBackground, 1664, 0);
        game.batch.draw(tempTowerDisplay, 1764, 500);
        game.font.draw(game.batch, "Wave: " + (wave - 1), 100 ,100 );
        game.font.draw(game.batch, "Enemys to Spawn: " + enemyHealthSpawnNumbers, 100 ,115 );
        game.font.draw(game.batch, "Money: " + money, 100 ,130 );
        game.font.draw(game.batch, "Health: " + playerHealth, 100, 145);
        game.batch.end();

       // map.showMap();
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

            if(enemy.hasReachedEndOfPath())
            {
                playerHealth -= enemy.getHealth();
                enemyIterator.remove();
            }



            for (Tower tower : towers)
            {
                //tower.attackUpdate(delta, enemy);

                if(tower.isEnemyInRange(enemy) && tower.canFire(enemy))
                {
                    //enemy.removeHealth(tower.getDamage());
                    spawnBullet(enemy,tower);

                }
            }

        }
        if(TimeUtils.nanoTime() - lastEnemySpawnTime > timeBetweenEnemySpawns && enemyHealthSpawnNumbers > 0){
            int enemyHealthModifier = 3;
            if (wave > 10)
            {
                enemyHealthModifier = 4;
            }
            int i = rand.nextInt(enemyHealthModifier) + 1;
            if(enemyHealthSpawnNumbers >= i) {
                spawnEnemy(i);
            }
            else
            {
                spawnEnemy(enemyHealthSpawnNumbers);
            }
        }

        boolean isPressed = Gdx.input.isKeyJustPressed(Keys.SPACE);
        if (isPressed && enemies.isEmpty()){
            processWaveStart();
        }


        //select BasicTower
        isPressed = Gdx.input.isKeyJustPressed(Keys.NUM_1);
        if(isPressed)
        {
            tempTowerDisplay = new Texture(Gdx.files.internal("BasicTower.png"));
            towerSelector = 1;
        }
        //select StrongTower
        isPressed = Gdx.input.isKeyJustPressed(Keys.NUM_2);
        if(isPressed)
        {
            tempTowerDisplay = new Texture(Gdx.files.internal("StrongTower.png"));
            towerSelector = 2;
        }
        //select LongTower
        isPressed = Gdx.input.isKeyJustPressed(Keys.NUM_3);
        if(isPressed)
        {
            tempTowerDisplay = new Texture(Gdx.files.internal("LongTower.png"));
            towerSelector = 3;
        }

    }

    private class TowerInputProcessor extends com.badlogic.gdx.InputAdapter
    {
        public boolean touchDown(int screenX, int screenY, int pointer, int button)
        {
            int[][] area = map.getMap();
            int mapWidth = area.length;
            int mapHeight = area[0].length;

            // these should be set to half of the tower image
            int towerWidth = 30;
            int towerHeight = 30;

            // Check if the tower placement is within the map boundaries and does not overlap with path
            if ((screenX >= towerWidth && screenX < mapWidth - towerWidth * 2) && (screenY >= towerHeight && screenY < mapHeight - towerHeight * 2) &&
                    area[screenX][screenY] == 1 && area[screenX + towerWidth][screenY] == 1 && area[screenX - towerWidth][screenY] == 1 && area[screenX][screenY + towerHeight] == 1 && area[screenX][screenY - towerHeight] == 1)
            {
                touchPos.set(screenX, screenY, 0);
                camera.unproject(touchPos);
                Tower tower;
                switch (towerSelector){
                    case 1: tower = new BasicTower();
                            break;
                    case 2: tower = new StrongTower();
                            break;
                    case 3: tower = new LongTower();
                            break;
                    default: tower = new BasicTower();
                            break;
                }
                if(money >= tower.price) {
                    money -= tower.price;
                    tower.setPosition(touchPos.x - 32, touchPos.y - 32);
                    towers.add(tower);
                }

                // loop prevents towers from overlapping
                for(int i = screenX - towerWidth; i < screenX + towerWidth; i++)
                {
                    for(int j = screenY - towerHeight; j < screenY + towerHeight; j++)
                    {
                        area[i][j] = -1;
                    }
                }

                return true;
            }
            return false;
        }
    }



    private void spawnEnemy(int health) {
        Enemy enemy = new Enemy(pathX,pathY,directionX,directionY,health);
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
