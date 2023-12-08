package com.cs3a.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Texture;

import java.awt.*;

public class MainMenuScreen implements Screen{

    final TowerDefence game;

    OrthographicCamera camera;

    public Rectangle newGameBox;

    public Rectangle continueBox;

    public Rectangle exitBox;

    Texture newGameImg;

    Texture continueImg;

    Texture exitImg;

    Texture headerImg;




    public MainMenuScreen(final TowerDefence game){
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false,1920,1080);

        this.newGameBox = new Rectangle();
        this.newGameBox.width = 256;
        this.newGameBox.height = 96;
        this.newGameBox.x = 832;
        this.newGameBox.y = 470;

        this.continueBox = new Rectangle();
        this.continueBox.width = 256;
        this.continueBox.height = 96;
        this.continueBox.x = 832;
        this.continueBox.y = 330;

        this.exitBox = new Rectangle();
        this.exitBox.width = 256;
        this.exitBox.height = 96;
        this.exitBox.x = 832;
        this.exitBox.y = 200;

        newGameImg = new Texture(Gdx.files.internal("NewGame.png"));

        continueImg = new Texture(Gdx.files.internal("Continue.png"));

        exitImg = new Texture(Gdx.files.internal("Exit.png"));

        headerImg = new Texture(Gdx.files.internal("Header.png"));


    }

    @Override
    public void render(float delta){
        ScreenUtils.clear(0,0,0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(headerImg, 630, 800);
        game.batch.draw(newGameImg, 832, 470);
        game.batch.draw(continueImg, 832, 330);
        game.batch.draw(exitImg, 832, 200);
        game.batch.end();

        if(Gdx.input.isTouched() && Gdx.input.getX() > 831 && Gdx.input.getX() < 1087
            && Gdx.input.getY() > 500 && Gdx.input.getY() < 630){
            game.setScreen(new GameScreen(game));
            dispose();

        }
        if(Gdx.input.isTouched() && Gdx.input.getX() > 831 && Gdx.input.getX() < 1087
                && Gdx.input.getY() > 770 && Gdx.input.getY() < 870){
            Gdx.app.exit();
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
