package com.TroperGames.MusicSweeper;

import com.TroperGames.MusicSweeper.Screens.GameScreen;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class GestureReciever implements GestureListener{
	
	float initialScale = 1;
	GameScreen game;
	public GestureReciever(GameScreen screen){
		game = screen;
	}
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		initialScale = game.cam.zoom;
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		//enables pinch zooming
		float scale = distance/initialDistance;
		game.cam.zoom = initialScale/scale;
		game.pinched = true;
		return true;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		game.pinched = true;
		return true;
	}

}
