package com.TroperGames.Visualizers;

import java.util.Random;

import com.TroperGames.MusicSweeper.MusicColor;
import com.badlogic.gdx.graphics.Color;

public class LineVisualizer implements visualizer{
	int rows, columns; MusicColor color; float FALLING_SPEED = (1.0f / 3.0f);
	float largest = 0; Color red; Color blue; Color yellow;
	int solid = 0; int blank = 0; int variant = 5;
	Random gen; boolean change = false;
	boolean bottom = false; boolean right = false; boolean left = false; boolean top = false;
	public LineVisualizer(int r, int c, MusicColor col){
		gen = new Random();
		rows = r; columns = c;
		color = col;
		red = new Color(.8f, 0f, 0f, .5f);
		blue = new Color(0f, .6f, 1f, .5f);
		yellow = new Color(1f, 1f, .2f, .5f);
	}

	
	public void update(float delta) {
		setDirection();
		//moves the existing colors
		if(right){
			for(int c = columns; c > 0; c--){
				for(int r = 0; r<rows; r++){
					if(color.field.isValid(r, c))
						color.colorList[r][c]=color.colorList[r][c-1];
				}
			}
		}
		if(left){
			for(int c = 0; c <columns; c++){
				for(int r = 0; r<rows; r++){
					if(color.field.isValid(r, c+1))
						color.colorList[r][c]=color.colorList[r][c+1];
				}
			}
		}
		if(bottom){
			for(int r = rows; r > 0; r--){
				for(int c = 0; c<columns; c++){
					if(color.field.isValid(r, c))
						color.colorList[r][c]=color.colorList[r-1][c];
				}
			}
		}
		if(top){
			for(int r = 0; r < rows-1; r++){
				for(int c = 0; c<columns; c++){
					if(color.field.isValid(r+1, c))
					color.colorList[r][c]=color.colorList[r+1][c];
				}
			}
		}
		int nb = (color.samples.length / columns) / 2;
		//adjusts the effect of the soundtrack on the visualization to ensure it is not a solid color
		if(solid>15){
			solid = 0;
			variant++;
			blank = 0;
			change = true;
		}
		else if(blank>15){
			solid = 0;
			blank = 0;
			variant--;
			change = true;
		}
		if(variant<0){
			variant = 0;
		}
		if(avg(variant, nb)>15){
			if(right){
				for(int r = 0; r < rows; r++){
					color.colorList[r][0] = blue;
				}
			}
			if(left){
				for(int r = 0; r < rows; r++){
					color.colorList[r][columns-1] = blue;
				}
			}
			if(bottom){
				for(int c = 0; c<columns; c++){
					color.colorList[0][c] = blue;
				}
			}
			if(top){
				for(int c = 0; c<columns; c++){
					color.colorList[rows-1][c] = blue;
				}
			}
			solid++;
			blank = 0;
		}
		else{
			if(right){
				for(int r = 0; r < rows; r++){
					color.colorList[r][0] = color.blankColor;
				}
			}
			if(left){
				for(int r = 0; r < rows; r++){
					color.colorList[r][columns-1] = color.blankColor;
				}
			}
			if(bottom){
				for(int c = 0; c<columns; c++){
					color.colorList[0][c] = color.blankColor;
				}
			}
			if(top){
				for(int c = 0; c<columns; c++){
					color.colorList[rows-1][c] = color.blankColor;
				}
			}
			solid = 0;
			blank++;
		}
		
	}
	
	private void setDirection(){
		//sets the direction, up to two nonopposite directions at once
		if(change){
			change = false;
			top = true;
			bottom = true;
			left = true;
			right = true;
			if(gen.nextBoolean()){
				left = false;
			}
			if(gen.nextBoolean()){
				right = false;
			}
			if(left && right){
				if(gen.nextBoolean()){
					left = false;
				}
				else{
					right = false;
				}
			}
			if(gen.nextBoolean()){
				top = false;
			}
			if(gen.nextBoolean()){
				bottom = false;
			}
			if(top && bottom){
				if(gen.nextBoolean()){
					top = false;
				}
				else{
					bottom = false;
				}
			}
			if(!top && !bottom && !left && !right){
				int temp = gen.nextInt(4);
				if(temp == 0){
					left = true;
				}
				else if(temp == 1){
					right =true;
				}
				else if(temp == 2){
					top = true;
				}
				else{
					bottom = true;
				}
			}
		}
	}

	private float avg(int pos, int nb) {
		int sum = 0;
		for (int i = 0; i < nb; i++) {
			sum += color.spectrum[pos + i];
		}

		return (float) (sum / nb);
	}
	
}
