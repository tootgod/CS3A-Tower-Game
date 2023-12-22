/*
Kyler Geesink
Gregory Shane
William Woods
Daniel Roberts
Garron Grim????(I'm sorry i never got your last name)
*/

package com.cs3a.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOverScreen implements Screen

{
    private TowerDefence game;
    OrthographicCamera camera;
    Rectangle newGameBox;
    Rectangle mainMenuBox;
    Rectangle exitBox;

    Texture gameOverImg;
    Texture newGameImg;
    Texture mainMenuImg;

    Texture exitImg;

    public GameOverScreen(TowerDefence game)
    {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

        newGameBox = new Rectangle();
        this.newGameBox.width = 256;
        this.newGameBox.height = 96;
        this.newGameBox.x = 832;
        this.newGameBox.y = 470;

        mainMenuBox = new Rectangle();
        this.mainMenuBox.width = 256;
        this.mainMenuBox.height = 96;
        this.mainMenuBox.x = 832;
        this.mainMenuBox.y = 330;

        exitBox = new Rectangle();
        this.exitBox.width = 256;
        this.exitBox.height = 96;
        this.exitBox.x = 832;
        this.exitBox.y = 200;

        gameOverImg = new Texture(Gdx.files.internal("GameOver.png"));
        newGameImg = new Texture(Gdx.files.internal("NewGame.png"));
        mainMenuImg = new Texture(Gdx.files.internal("MainMenuImg.png"));
        exitImg = new Texture(Gdx.files.internal("Exit.png"));

    }


    public void render(float delta)
    {
        ScreenUtils.clear(0, 0, 0, 0);

        // tell the camera to update its matrices.
        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        // newGame can be adjusted to y = 330 if no MainMenu added
        game.batch.begin();
        game.batch.draw(gameOverImg, 630, 650);
        game.batch.draw(newGameImg, 832, 470);
        game.batch.draw(mainMenuImg, 832, 330);
        game.batch.draw(exitImg, 832, 200);
        game.batch.end();

        if(Gdx.input.justTouched())
        {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();

            if(newGameBox.contains(x,y))
            {
                game.setScreen(new GameScreen(game));
                dispose();
            }

            if(mainMenuBox.contains(x,y))
            {
                game.setScreen(new MainMenuScreen(game));
            }

            if(exitBox.contains(x,y))
            {
                Gdx.app.exit();
            }
        }
    }

    @Override
    public void show()
    {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        mainMenuImg.dispose();
        gameOverImg.dispose();
        exitImg.dispose();
        newGameImg.dispose();

    }
}
