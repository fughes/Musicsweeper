package com.TroperGames.Visualizers;

import com.TroperGames.MusicSweeper.MusicColor;
import com.badlogic.gdx.graphics.Color;

public class BarsVisualizer implements visualizer{
	int rows, columns; MusicColor color; float FALLING_SPEED = (1.0f / 3.0f);
	float largest = 0; Color red; Color blue; Color yellow;
	public BarsVisualizer(int r, int c, MusicColor col){
		rows = r; columns = c;
		color = col;
		red = new Color(.8f, 0f, 0f, .5f);
		blue = new Color(0f, .6f, 1f, .5f);
		yellow = new Color(1f, 1f, .2f, .5f);
	}

	@Override
	public void update(float delta) {
		for(int r = 0; r < rows; r++){
			for(int c = 0; c < columns; c++){
				if(!color.colorList[r][c].equals(color.blankColor)){
					color.colorList[r][c]=color.blankColor;
				}
			}
		}
		for(int r = 0; r < columns; r++){
			int histoX = 0;
			if (r < columns / 2) {
				histoX = columns / 2 - r;
			} else {
				histoX = r - columns / 2;
			}

			int nb = (color.samples.length / rows) / 2;
			if (avg(histoX, nb) > color.maxValues[histoX]) {
				color.maxValues[histoX] = avg(histoX, nb);
			}

			if (avg(histoX, nb) > color.topValues[histoX]) {
				color.topValues[histoX] = avg(histoX, nb);
			}
			for(int c = 0; c < scale(avg(histoX, nb)); c++){
				if(color.field.isValid(c, r))
				color.colorList[c][r] = blue;
			}
			if(color.field.isValid((int) scale(color.maxValues[histoX]), r))
				color.colorList[(int) scale(color.maxValues[histoX])][r] = red;
			if(color.field.isValid((int) scale(color.topValues[histoX]), r))
				color.colorList[(int) scale(color.topValues[histoX])][r] = yellow;
			color.topValues[histoX] -= FALLING_SPEED;
			
			
		}
	}
	private float scale(float x) {
		//scales the results from the FFT
		if(x>largest){
			largest = x;
		}
		return x/3;
	}

	private float avg(int pos, int nb) {
		//average
		int sum = 0;
		for (int i = 0; i < nb; i++) {
			sum += color.spectrum[pos + i];
		}
		return (float) (sum / nb);
	}
	
}
