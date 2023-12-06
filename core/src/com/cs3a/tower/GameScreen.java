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
    TiledMap map;
    Vector3 touchPos;
    SpriteBatch batch;

    Array<Enemy> enemies;
    long lastEnemySpawnTime;
    Array<Tower> towers;

    Array<Bullet> bullets;
    boolean isPlacing;

    int[] pathX = new int[]{-64, 1062 + 32, 1062, 658 - 32, 658, 1664 + 64};
    int[] pathY = new int[]{647, 647, 937 + 32, 937, 129 - 32, 129};
    int[] directionX = new int[]{300, 0, -300, 0, 300};
    int[] directionY = new int[]{0, 300, 0, -300, 0};

    Random rand;

    boolean shown = false;

    int enemySpawnNumbers;

    OrthographicCamera camera;

    public GameScreen(final TowerDefence game) {
        this.game = game;

        background = new Texture(Gdx.files.internal("LevelBackground.png"));
        menuBackground = new Texture(Gdx.files.internal("MenuBackground.png"));
        map = new TiledMap();
        rand = new Random();
        touchPos = new Vector3();
        batch = new SpriteBatch();

        //Setup Default Enemy
        enemies = new Array<Enemy>();
        spawnEnemy(rand.nextInt(3) + 1);
        enemySpawnNumbers = 100;
        bullets = new Array<Bullet>();

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

        // begin a new batch and draw the bucket and
        // all drops
        game.batch.begin();
        game.batch.draw(background, 0, 0, 1920, 1080);
        for (Enemy enemy : enemies) {
            game.batch.draw(enemy.enemyImage, enemy.interactionBox.x, enemy.interactionBox.y, enemy.interactionBox.width, enemy.interactionBox.height);
        }

        for (Tower tower : towers) {
            game.batch.draw(tower.getTowerTexture(), tower.interactionBox.x, tower.interactionBox.y,tower.interactionBox.width,tower.interactionBox.height);
        }
        for(Bullet bullet : bullets)
        {
            game.batch.draw(bullet.bulletTexture, bullet.interactionBox.x, bullet.interactionBox.y,  bullet.interactionBox.width,  bullet.interactionBox.height);
        }

        game.batch.draw(menuBackground, 1664, 0);
        //game.font.draw(game.batch, "Drops Collected: " + enemy.whatPoint, enemy.pathX[enemy.whatPoint + 1], enemy.pathY[enemy.whatPoint + 1]);
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
                    //bulletIterator.next();
                }
                if (bullet.checkHit(enemy) && bullet.canDamage) {
                    enemy.removeHealth(bullet.damage);
                    bullet.hide();
                }
                if (enemy.getHealth() <= 0) {
                    enemyIterator.remove();
                    break;
                }
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

            if(TimeUtils.nanoTime() - lastEnemySpawnTime > 100000000 && enemySpawnNumbers > 0){
                spawnEnemy(rand.nextInt(3) + 1);
            }

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
            int towerWidth = 32;
            int towerHeight = 48;

            // Check if the tower placement is within the map boundaries and does not overlap with path
            if ((screenX >= towerWidth && screenX < mapWidth - towerWidth * 2) && (screenY >= towerHeight && screenY < mapHeight - towerHeight * 2) &&
                    area[screenX][screenY] == 1 && area[screenX + towerWidth][screenY] == 1 && area[screenX - towerWidth][screenY] == 1 && area[screenX][screenY + towerHeight] == 1 && area[screenX][screenY - towerHeight] == 1)
            {
                touchPos.set(screenX, screenY, 0);
                camera.unproject(touchPos);
                Tower tower = new LongTower();
                tower.setPosition(touchPos.x-32, touchPos.y-32);
                towers.add(tower);

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
        enemySpawnNumbers--;
    }

    private void spawnBullet(Enemy enemy, Tower tower)
    {
        Bullet bullet = new Bullet(tower.interactionBox.x + 10, tower.interactionBox.y + 10,10f, tower.getDamage(), enemy.interactionBox.x + 32,enemy.interactionBox.y + 32, tower);
        bullets.add(bullet);
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
