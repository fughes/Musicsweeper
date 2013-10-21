package com.TroperGames.MusicSweeper;

import java.util.Random;

import com.TroperGames.MusicSweeper.Screens.GameScreen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class MineField {
	
	private int xLL; private int yLL; Texture tempTexture = null; int totalSelect = 0;
	private int rows; private int columns; Sprite[][] toRender;
	private int numSquares; private int numMines; private int numMinesLeft; private int numFlags;
	private int height; private int width; private int boxSide; private int parentRow; private int parentColumn;
	private boolean lose = false; private boolean isActive; private boolean win = false; boolean first = true;
	private int[][] field;
	private boolean[][] selected; boolean[][] update;
	private MineField parentField = null; boolean firstLose = false;
	GameScreen game;
	public MineField(int x1, int y1, int h, int w, int r, int c, int mines, GameScreen game){
		this.game = game;
		toRender = new Sprite[r][c];
		field = new int[r][c];
		setActive(false);
		selected = new boolean[r][c];
		update = new boolean[r][c];
		setNumFlags(0);
		numMines = mines;
		numMinesLeft = numMines;
		rows = r;
		columns = c;
		numSquares = rows*columns;
		int numSquaresLeft = numSquares;
		xLL = x1; yLL = y1;
		height = h;
		width = w;
		//places the mines
		Random gen = new Random();
		int percent = 100*numMinesLeft/(numSquaresLeft);
		for(int i = 0; i<rows; i++){
			for(int q = 0; q<columns; q++){
				if(gen.nextInt(100)<percent){
					field[i][q] = 1;
					numMinesLeft--;
				}
				else{
					field[i][q] = 0;
				}
				selected[i][q]=false;
				update[i][q]=true;
				numSquaresLeft--;
				if(numSquaresLeft!=0){
					percent = 100*numMinesLeft/(numSquaresLeft);
				}
			}
		}
		numMinesLeft = numMines;
		boxSide = height/rows;
	}
	public int sum(int r, int c){
		//returns the number of surrounding mines, or 9 if the square is a mine
		int total = 0;
		if(field[r][c]==1||field[r][c]==3){
			return 9;
		}
		for(int i = 0; i<3; i++){
			for(int q = 0; q<3; q++){
				if(isValid(r-1+i, c-1+q)){
					if(field[r-1+i][c-1+q]==1||field[r-1+i][c-1+q]==3)
						total++;
				}
			}
		}
		return total;
	}
	public void flag(int r, int c){
		//flags and unflags a square
		if(field[r][c]==0&&!selected[r][c]){
			field[r][c]=2;
			selected[r][c]=true;
			update[r][c]=true;
			numFlags++;
		}
		else if(field[r][c]==1&&!selected[r][c]){
			field[r][c]=3;
			selected[r][c]=true;
			update[r][c]=true;
			numFlags++;
		}
		else if(field[r][c]==3){
			field[r][c]=1;
			selected[r][c]=false;
			update[r][c]=true;
			numFlags--;
		}
		else if(field[r][c]==2){
			field[r][c]=0;
			selected[r][c]=false;
			update[r][c]=true;
			numFlags--;
		}
	}
	public boolean isValid(int r, int c){
		//checks if a square exists
		if(r<0||r>=rows||c<0||c>=columns){
			return false;
		}
		return true;
	}
	public void select(int r, int c){
		//selects a square and all surrounding squares. Crashes were common if more than 5000 squares are selected, so it also stops at that number too.
		if(selected[r][c] || totalSelect > 5000){}
		else if(sum(r, c)==9){
			if(first){
				game.reset();
			}
			lose=true;
			firstLose = true;
			isActive = false;
			selected[r][c]=true;
			update[r][c]=true;
		}
		else if(sum(r, c)==0){
			selected[r][c]=true;
			update[r][c]=true;
			for(int i = 0; i<3; i++){
				for(int q = 0; q<3; q++){
					if(isValid(r-1+i, c-1+q) && !selected[r-1+i][c-1+q]){
						totalSelect++;
						select(r-1+i,c-1+q);
					}
				}
			}
		}
		else{
			selected[r][c]=true;
			update[r][c]=true;
		}
		if(first){
			first = false;
		}
	}
	//The following methods can be used to move and scale the minefield but have not been implemented
	public void setXLL(int x){
		xLL = x;
	}
	public int getXLL(){
		return xLL;
	}
	public void setYLL(int y){
		yLL = y;
	}
	public int getYLL(){
		return yLL;
	}
	public void setheight(int l){
		height = l;
	}
	public int getheight(){
		return height;
	}
	public void update(){
		if(parentField!=null){
			xLL = parentField.getCX(parentColumn);
			yLL = parentField.getRY(parentRow);
			height = parentField.getboxSide();
			boxSide = height/rows;
		}
	}
	//a few getters and setters
	public boolean getLose() {
		return lose;
	}
	public int getRows(){
		return rows;
	}
	public int getColumns(){
		return columns;
	}
	public boolean isSelected(int r, int c){
		return selected[r][c];
	}
	public int getValue(int r, int c){
		return field[r][c];
	}
	public void setNumFlags(int n) {
		numFlags = n;
	}
	public int getNumFlags() {
		return numFlags;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public boolean isActive() {
		return isActive;
	}
	public boolean inField(int x, int y){
		//checks if a click is withing the minefield
		if(x>=xLL&&x<=xLL+width&&y>=yLL&&y<=yLL+height){
			return true;
		}
		return false;
	}
	public void click(int x, int y){
		if(boxSide!=0&&inField(x,y)){
			int r = (y-yLL)/boxSide;
			int c = columns - ((x-xLL)/boxSide) - 1;
			totalSelect = 0;
			if(!selected[r][c]){
				select(r, c);
			}
			else if(getSurroundingFlags(r, c) == sum(r, c)){
				for(int i = 0; i<3; i++){
					for(int q = 0; q<3; q++){
						if(isValid(r-1+i, c-1+q) && !selected[r-1+i][c-1+q]){
							select(r-1+i, c-1+q);
						}
					}
				}
			}
		}
	}
	public int getSurroundingFlags(int r, int c){
		//checks the number of flags in order to allow clicking on a square with the proper number of surrounding flags to open up surrounding unflagged squares
		int total = 0;
		for(int i = 0; i<3; i++){
			for(int q = 0; q<3; q++){
				if(isValid(r-1+i, c-1+q)){
					if(field[r-1+i][c-1+q]==2||field[r-1+i][c-1+q]==3)
						total++;
				}
			}
		}
		return total;
	}
 	public void rClick(int x, int y){
 		//right click
		if(inField(x,y)){
			flag((y-yLL)/boxSide, columns - ((x-xLL)/boxSide) - 1);
		}
	}
 	//more getters
	public int getboxSide() {
		return boxSide;
	}
	public int getNumMines(){
		return numMines;
	}
	public int getRY(int r){
		return yLL+(r)*boxSide;
	}
	public int getCX(int c){
		return xLL+(columns-c-1)*boxSide;
	}
	public boolean getWin(){
		return win;
	}
	public void checkWin(){
		//checks if the game has been won
		boolean w = true;
		for(int r = 0; r<rows; r++){
			for(int c = 0; c<columns; c++){
				if(field[r][c]==0&&!selected[r][c]){
					w = false;
				}
				else if(field[r][c]==2){
					w = false;
				}
			}
		}
		win = w;
	}
	public void parentNull(){
		parentField = null;
	}
	public void render(float delta, SpriteBatch batch, MusicColor color) {
		Sprite temp;
		for(int r = 0; r < rows; r++){
			for(int c = 0; c < columns; c++){
				int sum = sum(r, c);
				if(update[r][c]){
					if(!selected[r][c]){
						tempTexture = new Texture("data/MineArt/NotSelected.png");				
					}
					else if(field[r][c]==3||field[r][c]==2){
						tempTexture = new Texture("data/MineArt/flag.png");
					}
					else{
						if(sum == 9){
							tempTexture = new Texture("data/MineArt/mine.png");
						}
						else{
							tempTexture = new Texture("data/MineArt/" + sum + ".png");
						}
					}
					tempTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
					temp = new Sprite(tempTexture);
					temp.setX(getCX(c));
					temp.setY(getRY(r));
					toRender[r][c]=new Sprite(temp);
					update[r][c]=false;
				}
				else if (firstLose){
					//if the game was lost, shows mines, even if they were flagged
					if(field[r][c]==3){
						flag(r,c);
					}
					if(sum == 9){
						select(r, c);
					}
				}
				toRender[r][c].setColor(color.getColor(r, c));
				toRender[r][c].draw(batch);
			}
		}
		if(firstLose){
			firstLose = false;
		}
	}
	public void dispose(){
		tempTexture.dispose();
	}
}
