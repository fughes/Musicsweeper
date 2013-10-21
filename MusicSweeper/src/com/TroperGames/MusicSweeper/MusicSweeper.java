package com.TroperGames.MusicSweeper;

import com.TroperGames.MusicSweeper.Screens.MainMenu;
import com.TroperGames.MusicSweeper.Screens.Options;
import com.TroperGames.MusicSweeper.Screens.SplashScreen;
import com.badlogic.gdx.Game;

public class MusicSweeper extends Game {
	
	
	public static final String VERSION = "1.0";
	public static final String LOG = "MusicSweeper";
	public MainMenu mainMenu;
	public Options options;
	boolean desktop;
	public MusicSweeper(boolean desktop){
		this.desktop = desktop;
	}
	
	@Override
	public void create() {
		mainMenu = new MainMenu(this, desktop);
		options = new Options(this, desktop);
		setScreen(new SplashScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {		
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
