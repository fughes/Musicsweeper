package com.TroperGames.MusicSweeper;

import android.os.Bundle;
import android.os.Environment;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        cfg.useCompass=false;
        initialize(new MusicSweeper(false), cfg);
        Environment.getExternalStorageDirectory().getAbsolutePath();
    }
    
}