package com.cs3a.tower;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {
    final TowerDefence game;

    Texture background;
    Texture menuBackground;

    Enemy enemy;

    int[] pathX = new int[]{-64,1062+32,1062,658-32,658,1664+64};
    int[] pathY = new int[]{647,647,937+32,937,129-32,129};
    int[] directionX = new int[]{300,0,-300,0,300};
    int[] directionY = new int[]{0,300,0,-300,0};

    OrthographicCamera camera;
    public GameScreen(final TowerDefence game) {
        this.game = game;

        background = new Texture(Gdx.files.internal("LevelBackground.png"));

        menuBackground = new Texture(Gdx.files.internal("MenuBackground.png"));

        //Setup Default Enemy
        enemy = new Enemy(pathX,pathY,directionX,directionY);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false,1920,1080);

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
        game.batch.draw(background,0,0, 1920,1080);
        game.batch.draw(enemy.enemyImage, enemy.interactionBox.x, enemy.interactionBox.y,  enemy.interactionBox.width,  enemy.interactionBox.height);
        game.batch.draw(menuBackground,1664,0);
        game.font.draw(game.batch, "Drops Collected: " + enemy.whatPoint, enemy.pathX[enemy.whatPoint + 1], enemy.pathY[enemy.whatPoint + 1]);
        game.batch.end();

        enemy.interactionBox.x += enemy.directionX[enemy.whatPoint] * Gdx.graphics.getDeltaTime();
        enemy.interactionBox.y += enemy.directionY[enemy.whatPoint] * Gdx.graphics.getDeltaTime();

        if(enemy.interactionBox.contains(enemy.pathX[enemy.whatPoint + 1],enemy.pathY[enemy.whatPoint + 1]))
        {
            if(enemy.whatPoint + 1 == enemy.directionY.length)
            {
                enemy.whatPoint = 0;
                enemy.interactionBox.x = enemy.pathX[enemy.whatPoint] - 32;
                enemy.interactionBox.y = enemy.pathY[enemy.whatPoint] - 32;
            }
            else
            enemy.whatPoint++;

        }

        // process user input
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if(enemy.interactionBox.contains(touchPos.x, touchPos.y))
            {

            }
            //bucket.x = touchPos.x - 64 / 2;
        }

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

    }
}
