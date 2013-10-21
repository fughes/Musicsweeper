package com.TroperGames.MusicSweeper.Screens;

import com.TroperGames.MusicSweeper.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;

public class GameScreen implements Screen{
	public MusicSweeper game;
	public MineField field;
	SpriteBatch batch;
	public MusicColor color;
	public OrthographicCamera cam;
	float width, height;
	InputReciever input;
	GestureReciever gestures;
	public boolean pinched = false;
	public float deltaPinched = 0f;
	float accelX;
    float accelY;
    float accelZ;
    float accel;
    boolean available;
    int rows;
    int columns;
    BitmapFont font;
    Integer minesLeft;
	
	public GameScreen(MusicSweeper game){
		font = new BitmapFont();
		font.setColor(0, 0, 0, 1);
		available = Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer);
		input = new InputReciever(this);
		gestures = new GestureReciever(this);
		rows = game.options.rows;
		columns = game.options.columns;
		this.game = game;
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		field = new MineField(0, 0, rows*64, columns*64, rows, columns, game.options.mines, this);
		color = new MusicColor(rows, columns, field, this);
		batch = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, width, height);
		cam.position.set(width, height, 0);
		batch.setProjectionMatrix(cam.combined);
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(new GestureDetector(gestures));
		multiplexer.addProcessor(input);
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void render(float delta) {
		accelerationCheck();
		if(!field.getLose()){
			minesLeft = (field.getNumMines()-field.getNumFlags());
		}
		if(field.getWin()){
			minesLeft = 0;
		}
	    font.setScale(cam.zoom);
		if(pinched){
			deltaPinched=0;
		}
		else{
			deltaPinched += .5;
		}
		input.timePassed +=delta;
		input.dragPassed =+ delta;
		input.checkHeld();
		Gdx.gl.glClearColor(1, 1, 1, 1);
	    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	    cameraUpdate(delta);
	    batch.setProjectionMatrix(cam.combined);
	    color.update(delta);
		batch.begin();
		field.render(delta, batch, color);
		font.draw(batch, minesLeft.toString() + " mines left", cam.position.x-cam.zoom*cam.viewportWidth/2+cam.zoom*20, cam.position.y-cam.zoom*cam.viewportHeight/2+cam.zoom*25);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		cam.setToOrtho(false, width, height);
		cam.position.set(width/2, height/2, 0);
		batch.setProjectionMatrix(cam.combined);
	}

	@Override
	public void show() {
		Gdx.input.setCatchBackKey(true);
		Thread.currentThread().setPriority(7);
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
	
	public void accelerationCheck(){
		if(available){
			accelX = Gdx.input.getAccelerometerX();
			accelY = Gdx.input.getAccelerometerY();
			accelZ = Gdx.input.getAccelerometerZ();
			accel = accelX+accelY+accelZ;
			if(accel>23){
				color.shuffle();
				if(field.getLose()||field.getWin()){
					reset();
				}
			}
		}
	}
	
	public void cameraUpdate(float delta){
		if(cam.zoom < 0.4){
			cam.zoom = 0.4f;
		}
		cam.translate(input.translateX*260*delta, input.translateY*delta*260);
		if(cam.position.x < 0){
			cam.position.x = 0;
		}
		else if(cam.position.x > columns * 64){
			cam.position.x = columns*64;
		}
		if(cam.position.y < 0){
			cam.position.y = 0;
		}
		else if(cam.position.y > rows*64){
			cam.position.y = rows * 64;
		}
		cam.update();
	    cam.apply(Gdx.graphics.getGL10());
	}

	@Override
	public void dispose() {
		batch.dispose();
		field.dispose();
		color.dispose();
		
	}
	
	public void reset(){
		field.dispose();
		field = new MineField(0, 0, rows*64, columns*64, rows, columns, game.options.mines, this);
	}

}
