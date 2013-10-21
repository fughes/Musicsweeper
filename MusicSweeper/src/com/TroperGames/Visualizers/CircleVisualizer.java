package com.TroperGames.Visualizers;


import com.TroperGames.MusicSweeper.MusicColor;
import com.badlogic.gdx.graphics.Color;

public class CircleVisualizer implements visualizer{
	//THIS CLASS IS UNFINISHED
	int rows, columns; MusicColor color; float FALLING_SPEED = (1.0f / 3.0f);
	float largest = 0; Color red; Color blue; Color yellow;
	int solid = 0; int blank = 0; int variant = 5;
	public CircleVisualizer(int r, int c, MusicColor col){
		rows = r; columns = c;
		color = col;
		red = new Color(.8f, 0f, 0f, .5f);
		blue = new Color(0f, .6f, 1f, .5f);
		yellow = new Color(1f, 1f, .2f, .5f);
	}

	@Override
	public void update(float delta){
		if(columns%2==0){
			updateEven();
		}
		else{
			updateOdd();
		}
	}
	
	public void updateEven(){
		boolean first; int temp;
		boolean evenRows = false;
		if(rows%2 == 0)
			evenRows = true;
		//bottom
		int i = 0;
		if(!evenRows)
			i=1;
		temp = 4+2*(columns/2-1);
		for(int r = 0; r<rows/2-1; r++){
			temp-=2;
			for(int c = (columns-temp)/2+i; c < (columns-temp)/2+temp-i; c++){
				color.colorList[r][c] = color.colorList[r+1][c];
				if(c== (columns-temp)/2+temp-1-i){
					color.colorList[r][c]= color.colorList[r][c-1];
					color.colorList[r][(columns-temp)/2+i]=color.colorList[r][c-1];
				}
			}
		}
		//top
		temp = 4+2*(columns/2-1);
		for(int r = rows-1; r>rows/2; r--){
			temp-=2;
			for(int c = (columns-temp)/2; c < (columns-temp)/2+temp; c++){
				color.colorList[r][c] = color.colorList[r-1][c];
				if(c== (columns-temp)/2+temp-1){
					color.colorList[r][c]=color.colorList[r][c-1];
					color.colorList[r][(columns-temp)/2]=color.colorList[r][(columns-temp)/2+1];
				}
			}
		}
		//left
		temp = 2+2*(rows/2-1);
		for(int c = columns-1; c>columns/2; c--){
			temp-=2;
			for(int r = (rows-temp)/2; r < (rows-temp)/2+temp; r++){
				color.colorList[r][c] = color.colorList[r][c-1];
				if(r== (rows-temp)/2+temp-1){
					color.colorList[r][c]=color.colorList[r-1][c];
					color.colorList[(rows-temp)/2][c]=color.colorList[(rows-temp)/2+1][c];
				}
			}
		}
		//right
		temp = 2+2*(rows/2-1);
		for(int c = 0; c<columns/2-1; c++){
			temp-=2;
			for(int r = (rows-temp)/2; r < (rows-temp)/2+temp; r++){
				color.colorList[r][c] = color.colorList[r][c+1];
				if(r== (rows-temp)/2+temp-1){
					color.colorList[r][c]=color.colorList[r-1][c];
					color.colorList[(rows-temp)/2][c]=color.colorList[(rows-temp)/2+1][c];
				}
			}
		}
		
		int nb = (color.samples.length / columns) / 2;
		if(solid>15){
			solid = 0;
			variant++;
			blank = 0;
		}
		else if(blank>15){
			solid = 0;
			blank = 0;
			variant--;
		}
		if(variant<0){
			variant = 0;
		}
		if(avg(variant, nb)>15){
			color.colorList[rows/2][columns/2-1] = blue;
			color.colorList[rows/2-1][columns/2-1] = blue;
			color.colorList[rows/2][columns/2] = blue;
			color.colorList[rows/2-1][columns/2] = blue;
			solid++;
			blank = 0;
		}
		else{
			color.colorList[rows/2][columns/2-1] = color.blankColor;
			color.colorList[rows/2-1][columns/2-1] = color.blankColor;
			color.colorList[rows/2][columns/2] = color.blankColor;
			color.colorList[rows/2-1][columns/2] = color.blankColor;
			solid = 0;
			blank++;
		}
		
	}
	public void updateOdd() {
		boolean first; int temp;
		boolean evenRows = false;
		if(rows%2 == 0)
			evenRows = true;
		//bottom
		for(int r = 1; r<rows/2; r++){
			temp = (columns-((rows/2-1)*2+1))/2+r-1;
			first = true;
			for(int c = temp; c < columns-temp; c++){
				if(first){
					first = false;
					if(color.field.isValid(r-1, c-1))
						color.colorList[r-1][c-1]=color.colorList[r][c];
				}
				else if(c == columns-temp-1 && color.field.isValid(r-1, c+1)){
					color.colorList[r-1][c+1]=color.colorList[r][c];
				}
				if(color.field.isValid(r-1, c)){
					color.colorList[r-1][c]=color.colorList[r][c];
				}
			}
		}
		//top
		for(int r = rows-2; r > rows/2; r--){
			if(rows%2==0)
			temp = (columns-((rows/2-1)*2+1))/2+rows-r-1;
			else
			temp = (columns-((rows/2-1)*2+1))/2+rows-r-2;
			first = true;
			for(int c = temp; c<columns-temp; c++){
				if(first){
				first = false;
				if(color.field.isValid(r+1, c-1))
					color.colorList[r+1][c-1]=color.colorList[r][c];
				}
				if(c == columns-temp-1 && color.field.isValid(r+1, c+1)){
					color.colorList[r+1][c+1]=color.colorList[r][c];
				}
				if(color.field.isValid(r+1, c)){
					color.colorList[r+1][c]=color.colorList[r][c];
				}
			}
		}
		//left
		temp = 0;
		for(int c = columns-1; c > columns/2; c--){
			temp++;
			for(int r = temp; r <rows-temp; r++){
				color.colorList[r][c]=color.colorList[r][c-1];
				if(r == rows-temp-1){
					color.colorList[r][c]=color.colorList[rows/2][c];
					color.colorList[temp][c]=color.colorList[rows/2][c];
					if(evenRows)
					color.colorList[r+1][c]=color.colorList[rows/2][c];
				}
			}
		}
		//right
		temp = 0;
		for(int c = 0; c < columns/2; c++){
			temp++;
			for(int r = temp; r <rows-temp; r++){
				color.colorList[r][c]=color.colorList[r][c+1];
				if(r == rows-temp-1){
					color.colorList[r][c]=color.colorList[rows/2][c];
					color.colorList[temp][c]=color.colorList[rows/2][c];
					if(evenRows)
					color.colorList[r+1][c]=color.colorList[rows/2][c];
				}
			}
		}
		int nb = (color.samples.length / columns) / 2;
		for(int i = 0; i < 9; i++){
			color.colorList[rows/2-1+i/3][columns/2+i%3-1] = color.colorList[rows/2][columns/2];
		}
		if(solid>15){
			solid = 0;
			variant++;
			blank = 0;
		}
		else if(blank>15){
			solid = 0;
			blank = 0;
			variant--;
		}
		if(variant<0){
			variant = 0;
		}
		if(avg(variant, nb)>15){
			color.colorList[rows/2][columns/2] = blue;
			solid++;
			blank = 0;
		}
		else{
			color.colorList[rows/2][columns/2] = color.blankColor;
			solid = 0;
			blank++;
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
