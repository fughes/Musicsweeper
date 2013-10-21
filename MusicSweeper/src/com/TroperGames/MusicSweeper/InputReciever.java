package com.TroperGames.MusicSweeper;

import com.TroperGames.MusicSweeper.Screens.GameScreen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

public class InputReciever implements InputProcessor{
	GameScreen gameScreen;
	public float timePassed = 0f;
	public float dragPassed = 0f;
	public float translateY = 0f;
	public float translateX = 0f;
	Vector3 translateVector;
	Vector3 oldCoordinates;
	Vector3 newCoordinates;
	Vector3 checkCoordinates;
	Vector3 currentCoordinates;
	float distanceDragged;
	boolean dragged = false;
	boolean canCheck = false;
	boolean maxDist = true;
	public InputReciever(GameScreen screen){
		gameScreen = screen;
		checkCoordinates = new Vector3();
		currentCoordinates = new Vector3();
	}

	@Override
	public boolean keyDown(int keycode) {
		switch(keycode){
		//WASD moves the camera
		case Input.Keys.W:
			translateY = 1.5f;
			break;
		case Keys.A:
			translateX = -1.5f;
			break;
		case Keys.S:
			translateY = -1.5f;
			break;
		case Keys.D:
			translateX = 1.5f;
			break;
		case Keys.RIGHT:
			gameScreen.color.nextSong();
			break;
		case Keys.LEFT:
			gameScreen.color.previousSong();
			break;
		case Keys.BACKSPACE:
		case Keys.BACK:
			//back to the menu
			gameScreen.game.setScreen(gameScreen.game.mainMenu);
			break;
		case Keys.SPACE:
			gameScreen.color.shuffle();
			break;
		case Keys.TAB:
			gameScreen.color.nextVisual();
			break;
		case Keys.R:
			gameScreen.reset();
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch(keycode){
		case Input.Keys.W:
			if(translateY!=0f)
			translateY = 0f;
			break;
		case Keys.A:
			if(translateX!=0f)
			translateX = 0f;
			break;
		case Keys.S:
			if(translateY!=0f)
			translateY = 0f;
			break;
		case Keys.D:
			if(translateX!=0f)
			translateX = 0f;
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		//If the screen isn't being moved via clicking and holding, starts a timer and records coordinates
		if(pointer==0){
		if(!gameScreen.field.getWin()&&!gameScreen.field.getLose()){
			switch(button){
			case Buttons.RIGHT:
			case Buttons.LEFT:
				timePassed = 0f;
				canCheck = true;
				maxDist = true;
				break;
			default:
				break;
			}
		}
		dragPassed = 0f;
		checkCoordinates = new Vector3(screenX, screenY, 0);
		oldCoordinates = new Vector3(screenX, screenY, 0);
		}
		return true;
	}
	public void checkHeld(){
		//helper method to see if the input is being held down
		float dst = checkCoordinates.dst(currentCoordinates);
		if(dst>=19){
			maxDist = false;
		}
		if(canCheck && timePassed>=.5f && !gameScreen.field.getWin()&&!gameScreen.field.getLose()&&(!dragged||maxDist)&&!gameScreen.pinched){
			gameScreen.cam.unproject(checkCoordinates);
			gameScreen.field.rClick((int)checkCoordinates.x, (int)checkCoordinates.y);
			canCheck = false;
		}
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		//determines what to do based on if the screen was moved and how long the input was held down.
		if(pointer==0){
			float dst = checkCoordinates.dst(screenX, screenY, 0);
			if(!gameScreen.field.getWin()&&!gameScreen.field.getLose()&&(!dragged||dst<19) && timePassed<.5f&&!gameScreen.pinched
					&& gameScreen.deltaPinched>.5&&canCheck){
				switch(button){
				case Buttons.LEFT:
					Vector3 realCoordinates = new Vector3(screenX, screenY, 0);
					gameScreen.cam.unproject(realCoordinates);
					gameScreen.field.click((int)realCoordinates.x, (int)realCoordinates.y);
					gameScreen.field.checkWin();
					break;
				case Buttons.RIGHT:
					Vector3 realCoordinatesR = new Vector3(screenX, screenY, 0);
					gameScreen.cam.unproject(realCoordinatesR);
					gameScreen.field.rClick((int)realCoordinatesR.x, (int)realCoordinatesR.y);
				default:
					break;
				}
			}
			gameScreen.pinched = false;
			dragged = false;
			canCheck = false;
		}
		return true;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		//allows the camera to move via dragging
		if(!gameScreen.pinched&&gameScreen.deltaPinched>.5f){
		dragged = true;
		gameScreen.cam.unproject(oldCoordinates);
		newCoordinates = new Vector3(screenX, screenY, 0);
		gameScreen.cam.unproject(newCoordinates);
		oldCoordinates.sub(newCoordinates);
		gameScreen.cam.translate(oldCoordinates);
		oldCoordinates = new Vector3(screenX, screenY, 0);
		currentCoordinates = new Vector3(screenX, screenY, 0);
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		//allows zooming via mouse wheel
		gameScreen.cam.zoom += .2*amount;
		return true;
	}

}
