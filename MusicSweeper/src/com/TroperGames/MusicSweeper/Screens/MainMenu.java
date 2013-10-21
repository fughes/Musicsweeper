package com.TroperGames.MusicSweeper.Screens;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;

import com.TroperGames.MusicSweeper.MusicSweeper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MainMenu implements Screen{
	
	MusicSweeper game;
	Stage stage;
	Skin skin;
	SpriteBatch batch;
	TextButton startButton;
	TextButton optionsButton;
	TextButton purchaseButton;
	Texture splashTexture;
	Sprite splashSprite;
	Table table;
	float deltaColor = 1f;
	boolean desktop;
	boolean splashScreen = true;
	
	public MainMenu(MusicSweeper game, boolean desktop){
		this.game = game;
		this.desktop = desktop;
	}

	@Override
	public void render(float delta) {
		splashSprite.setColor(1,1,1, deltaColor);
		stage.act(delta);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
		stage.draw();
		batch.end();
		if(splashScreen){
			batch.begin();
			splashSprite.draw(batch);
			batch.end();
		}
		deltaColor -= delta/2;
		if(deltaColor < 0){
			deltaColor = 0;
			splashScreen = false;
		}
	}

	@Override
	public void resize(int width, int height) {
		if(stage!=null)
			stage.dispose();
		stage = new Stage(width, height, true);
		
		Gdx.input.setInputProcessor(stage);
		
		
		
		startButton = new TextButton("Quick Start ", skin);
		startButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y,
								int pointer, int button){
				if(!splashScreen)
				game.setScreen(new GameScreen(game));
			}
		});
		optionsButton = new TextButton(" Options ", skin);
		optionsButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y,
								int pointer, int button){
				if(!splashScreen)
				game.setScreen(game.options);
			}
		});
		table = new Table();
		table.add(startButton).padBottom(10);
		table.row();
		table.add(optionsButton).padBottom(10);
		if(desktop){
			purchaseButton = new TextButton(" Get the android edition! ", skin);
			purchaseButton.addListener(new InputListener(){
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
					return true;
				}
			
				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button){
					if(!splashScreen){
						try {
							URL url = new URL("https://play.google.com/store/apps/details?id=com.TroperGames.MusicSweeper&feature=nav_result");
							URI uri = new URI(url.getProtocol(), url.getAuthority(), url.getPath(), 
									url.getQuery(), null);
							Desktop.getDesktop().browse(uri);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
			table.row();
			table.add(purchaseButton);
		}
		table.setFillParent(true);
		stage.addActor(table);
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		splashTexture = new Texture("data/MusicSweeperSplash.png");
		splashTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		splashSprite = new Sprite(splashTexture);
		splashSprite.setX(Gdx.graphics.getWidth()/ 2-(splashSprite.getWidth() / 2));
		splashSprite.setY(Gdx.graphics.getHeight() / 2 - (splashSprite.getHeight()/2));
		Gdx.input.setCatchBackKey(false);
	}

	@Override
	public void hide() {
		batch.dispose();
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		batch.dispose();
		skin.dispose();
		stage.dispose();
		splashTexture.dispose();
	}

}
