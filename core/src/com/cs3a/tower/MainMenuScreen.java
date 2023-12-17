package com.cs3a.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.files.FileHandle;

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

        FileHandle waveData = Gdx.files.local("WaveData");
        FileHandle moneyData = Gdx.files.local("MoneyData");
        FileHandle healthData = Gdx.files.local("HealthData");

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(headerImg, 630, 800);
        game.batch.draw(newGameImg, 832, 470);
        game.batch.draw(continueImg, 832, 330);
        game.batch.draw(exitImg, 832, 200);
        game.batch.end();

        if(Gdx.input.isTouched()){
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();

            if(newGameBox.contains(x, y)){
                waveData.writeString("1", false);
                moneyData.writeString("50", false);
                healthData.writeString("200", false);
                game.setScreen(new GameScreen(game));
                dispose();
            }

            if(continueBox.contains(x, y)){
                game.setScreen(new GameScreen(game));
                dispose();
            }

            if(exitBox.contains(x, y)){
                Gdx.app.exit();
            }

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
