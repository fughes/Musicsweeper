package com.TroperGames.MusicSweeper;

import java.util.ArrayList;
import java.util.Random;

import com.TroperGames.MusicSweeper.Screens.GameScreen;
import com.TroperGames.Visualizers.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.audio.analysis.*;
import com.badlogic.gdx.audio.io.*;

public class MusicColor {
	Mpg123Decoder decoder;
	AudioDevice device;
	int visualIndex = 0;
	int rows, columns;
	GameScreen game;
	visualizer blank;
	float totalDelta;
	public Color blankColor;
	boolean playing = false;
	boolean nextSong = false;
	public short[] samples = new short[2048];
	FileHandle[] music; ArrayList<FileHandle> musicList;
	int index; boolean canUpdate = true;
	KissFFT fft;
	public float[] spectrum = new float[2048];
	public float[] maxValues = new float[2048];
	public float[] topValues = new float[2048];
	public MineField field;
	boolean threadDone = false;
	public Color[][] colorList;
	ArrayList<visualizer> visualizers;
	public MusicColor(int r, int c, MineField field, GameScreen game){
		
		this.game = game;
		blank = new NoVisual();
		musicList = new ArrayList<FileHandle>();
		visualizers = new ArrayList<visualizer>();
		visualizers.add(new LineVisualizer(r, c, this));
		visualizers.add(new BarsVisualizer(r, c, this));
		this.field = field;
		blankColor = new Color(1f, 1f, 1f, 1f);
		rows = r; columns = c;
		fft = new KissFFT(2048);
		for (int i = 0; i < maxValues.length; i++) {
			maxValues[i] = 0;
			topValues[i] = 0;
		}
		FileHandle externalFile = Gdx.files.external("/music/");
		music = externalFile.list();
		for(FileHandle e : music){
			musicList.add(e);
		}
		for(int i = 0; i < musicList.size(); i++){
			if(musicList.get(i).isDirectory()){
				music = musicList.get(i).list();
				for(FileHandle e : music){
					musicList.add(e);
				}
				musicList.remove(i);
				i--;
			}
		}
		String compare = "";
		for(int i = 0; i < musicList.size(); i++){
			if(musicList.get(i).extension().equals("mp3")){
				compare = musicList.get(i).nameWithoutExtension();
				for(int q = i+1; q < musicList.size(); q++){
					if(musicList.get(q).nameWithoutExtension().equals(compare)){
						musicList.remove(q);
						q--;
					}
				}
			}
		}
		boolean isMusic = false;
		index = -1;
		while(!isMusic && musicList.size()>0){
			index++;
			if(musicList.get(index).extension().equals("mp3")){
				isMusic = true;
			}
			else{
				musicList.remove(index);
				index--;
			}
		}
		playing = true;
		startThread();
		colorList = new Color[r][c];
		for(int q = 0; q < r; q++){
			for(int i = 0; i < c; i++){
				colorList[q][i]=blankColor;
			}
		}
		if(!game.game.options.music || musicList.size()==0){
			threadDone = true;
		}
	}
	public void reset(){
		for(int r = 0; r < rows; r++){
			for(int c = 0; c<columns; c++){
				colorList[r][c]=blankColor;
			}
		}
	}
	public void update(float delta){
		totalDelta += delta;
		if(totalDelta >=.05){
			if(canUpdate){
				getVisual().update(delta);
			}
			totalDelta-=.05;
		}
		
	}
	public visualizer getVisual(){
		if(game.game.options.visual && game.game.options.music){
			return visualizers.get(visualIndex);
		}
		return blank;
	}
	public void nextVisual(){
		visualIndex++;
		if(visualIndex == visualizers.size()){
			visualIndex = 0;
		}
	}
	public Color getColor(int r, int c){
		return colorList[r][c];
	}
	public void dispose(){
	// synchronize with the thread
			playing = false;
			while(!threadDone){
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			fft.dispose();
			// delete the temp file
	}
	public void nextSong(){
		canUpdate = false;
		nextSong = true;
		while(!threadDone){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		nextSong=false;
		reset();
		randomVisual();
		for (int i = 0; i < maxValues.length; i++) {
			maxValues[i] = 0;
			topValues[i] = 0;
		}
		canUpdate = true;
		boolean isMusic = false;
		while(!isMusic && musicList.size()>0){
			index++;
			if(index>=musicList.size()){
				index = 0;
			}
			if(musicList.get(index).extension().equals("mp3")){
				isMusic = true;
			}
		}
		threadDone = false;
		startThread();
	}
	public void previousSong(){
		canUpdate = false;
		nextSong = true;
		while(!threadDone){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		nextSong=false;
		reset();
		randomVisual();
		for (int i = 0; i < maxValues.length; i++) {
			maxValues[i] = 0;
			topValues[i] = 0;
		}
		canUpdate = true;
		boolean isMusic = false;
		while(!isMusic && musicList.size()>0){
			index--;
			if(index<0){
				index = musicList.size()-1;
			}
			if(musicList.get(index).extension().equals("mp3")){
				isMusic = true;
			}
		}
		threadDone = false;
		startThread();
	}
	public void shuffle(){
		Random gen = new Random();
		index = gen.nextInt(music.length)-1;
		nextSong();
	}
	public void randomVisual(){
		Random gen = new Random();
		visualIndex = gen.nextInt(visualizers.size());
	}
	public void startThread(){
		if(game.game.options.music && musicList.size()>0){
			decoder = new Mpg123Decoder(musicList.get(index));
			device = Gdx.audio.newAudioDevice(decoder.getRate(),
					decoder.getChannels() == 1 ? true : false);
			Thread playbackThread = new Thread(new Runnable() {
				@Override
				public void run() {
					int readSamples = 0;
					// read until we reach the end of the file
					while (playing
							&& (readSamples = decoder.readSamples(samples, 0,
									samples.length)) > 0 && !nextSong) {
						// get audio spectrum
						fft.spectrum(samples, spectrum);
						// write the samples to the AudioDevice
						device.writeSamples(samples, 0, readSamples);
					}
					if(nextSong!=true && playing == true){
						device.dispose();
						decoder.dispose();
						threadDone = true;
						nextSong();
					}
					else{
						device.dispose();
						decoder.dispose();
						threadDone = true;
					}
				}			
			});
			playbackThread.setDaemon(true);
			playbackThread.setPriority(10);
			playbackThread.start();
		}
	}
}
