package com.TroperGames.MusicSweeper.Screens;


import com.TroperGames.MusicSweeper.MusicSweeper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen implements Screen{
	
	Texture splashTexture;
	Sprite splashSprite;
	SpriteBatch batch;
	MusicSweeper game;
	float deltaColor = 0f;
	
	public SplashScreen(MusicSweeper m){
		game = m;
		Gdx.input.setCatchMenuKey(true);
	}

	@Override
	public void render(float delta) {
		splashSprite.setColor(1,1,1, deltaColor);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
		splashSprite.draw(batch);
		batch.end();
		if(deltaColor < 1){
			deltaColor += delta/2;
		}
		else{
			game.setScreen(game.mainMenu);
		}
		
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		splashTexture = new Texture("data/MusicSweeperSplash.png");
		splashTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		splashSprite = new Sprite(splashTexture);
		splashSprite.setColor(1,1,1,0);
		splashSprite.setX(Gdx.graphics.getWidth()/ 2-(splashSprite.getWidth() / 2));
		splashSprite.setY(Gdx.graphics.getHeight() / 2 - (splashSprite.getHeight()/2));
		batch = new SpriteBatch();
		
		
	}
	
	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		splashTexture.dispose();
		batch.dispose();
	}
	
}
