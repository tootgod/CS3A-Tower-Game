/*
Kyler Geesink
Gregory Shane
William Woods
Daniel Roberts
Garron Grim????(I'm sorry i never got your last name)
*/

package com.cs3a.tower;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TowerDefence extends Game{
	public SpriteBatch batch;
	public BitmapFont font;



	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this));
	}

	public void render(){
		super.render();
	}

	public void dispose(){
		batch.dispose();
		font.dispose();
	}
}
