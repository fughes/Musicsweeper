package com.TroperGames.MusicSweeper;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "MusicSweeper" + " " + MusicSweeper.VERSION;
		cfg.useGL20 = false;
		cfg.width = 800;
		cfg.height = 480;
		cfg.addIcon("data/icon.png", Files.FileType.Internal);
		cfg.addIcon("data/icon2.png", Files.FileType.Internal);
		//cfg.vSyncEnabled = false;
		//cfg.forceExit = true;
		
		new LwjglApplication(new MusicSweeper(true), cfg);
	}
}
