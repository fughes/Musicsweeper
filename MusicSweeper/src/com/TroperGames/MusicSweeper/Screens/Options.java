package com.TroperGames.MusicSweeper.Screens;

import com.TroperGames.MusicSweeper.MusicSweeper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class Options implements Screen{
	MusicSweeper game;
	Stage stage;
	Skin skin;
	SpriteBatch batch;
	TextButton startButton;
	TextButton backButton;
	Table table;
	public int rows;
	public int columns;
	public int mines;
	public boolean visual;
	public boolean music;
	CheckBox musicCheckBox;
	CheckBox visualCheckBox;
	TextField minesText;
	TextField rowsText;
	TextField columnsText;
	boolean first = true;
	boolean desktop;
	Preferences prefs;
	
	public Options(MusicSweeper game, boolean desktop){
		this.game = game;
		this.desktop = desktop;
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		prefs = Gdx.app.getPreferences("Options");
		rows = prefs.getInteger("Rows", 15);
		columns = prefs.getInteger("Columns", 15);
		mines = prefs.getInteger("NumMines", 25);
		music = prefs.getBoolean("PlayMusic", true);
		visual = prefs.getBoolean("Visuals", true);
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		table.debug();
		batch.begin();
		stage.draw();
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		if(!first){
			rows = Integer.parseInt(rowsText.getText());
			columns = Integer.parseInt(columnsText.getText());
			mines = Integer.parseInt(minesText.getText());
		}
		else{
			first = false;
		}
		if(stage!=null)
			stage.dispose();
		stage = new Stage(width, height, true);
		Gdx.input.setInputProcessor(stage);
		
		
		startButton = new TextButton("Start", skin);
		startButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y,
								int pointer, int button){
				updateValues();
				game.setScreen(new GameScreen(game));
			}
		});
		backButton = new TextButton("Back", skin);
		backButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y,
								int pointer, int button){
				updateValues();
				game.setScreen(game.mainMenu);
			}
		});
		table = new Table();
		table.add(startButton).padBottom(5).colspan(2);
		table.row();
		String rows = ""+this.rows;
		Label rowsLabel = new Label("Rows:", skin);
		rowsLabel.setColor(.5f,.5f,.5f,1);
		rowsText = new TextField(rows, skin);
		table.add(rowsLabel).padBottom(5).padTop(5);
		table.add(rowsText).width(100);
		table.row();
		String columns = ""+this.columns;
		Label columnsLabel = new Label("Columns:  ", skin);
		columnsLabel.setColor(.5f,.5f,.5f,1);
		columnsText = new TextField(columns, skin);
		table.add(columnsLabel).padBottom(5).padTop(5);
		table.add(columnsText).width(100);
		table.row();
		Label minesLabel = new Label("Mines:", skin);
		minesLabel.setColor(.5f,.5f,.5f,1);
		String mines = ""+this.mines;
		minesText = new TextField(mines, skin);
		table.add(minesLabel).padBottom(5).padTop(5);
		table.add(minesText).width(100);
		table.row();
		Label musicLabel = new Label("Enable music", skin);
		musicLabel.setColor(.5f,.5f,.5f,1);
		musicCheckBox = new CheckBox("", skin);
		musicCheckBox.setChecked(music);
		table.add(musicLabel).padBottom(5).padTop(5);
		table.add(musicCheckBox);
		table.row();
		Label visualLabel = new Label("Enable visualizers", skin);
		visualLabel.setColor(.5f,.5f,.5f,1);
		visualCheckBox = new CheckBox("", skin);
		visualCheckBox.setChecked(visual);
		table.add(visualLabel).padBottom(5).padTop(5);
		table.add(visualCheckBox);
		table.row();
		table.add(backButton).colspan(2);
		table.setFillParent(true);
		stage.addActor(table);
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void hide() {
		updateValues();
		first = true;
		batch.dispose();
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
		batch.dispose();
		skin.dispose();
		stage.dispose();
	}

	public void updateValues(){
		try{
			rows = Integer.parseInt(rowsText.getText());
		}
		catch(NumberFormatException e){
			
		}
		try{
			columns = Integer.parseInt(columnsText.getText());
		}
		catch(NumberFormatException e){
			
		}
		try{
			mines = Integer.parseInt(minesText.getText());
		}
		catch(NumberFormatException e){
			
		}
		if(mines > rows*columns){
			mines = (int) (rows*columns*.8);
		}
		visual = visualCheckBox.isChecked();
		music = musicCheckBox.isChecked();
		prefs.putBoolean("PlayMusic", music);
		prefs.putBoolean("Visuals", visual);
		prefs.putInteger("NumMines", mines);
		prefs.putInteger("Rows", rows);
		prefs.putInteger("Columns", columns);
		prefs.flush();
	}
}