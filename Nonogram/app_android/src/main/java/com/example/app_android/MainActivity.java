package com.example.app_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;

import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.example.engine_android.EngineAndroid;
import com.example.nonogram.BootScene;
import com.example.nonogram.MainMenu;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set surface view
        SurfaceView renderView = new SurfaceView(this);
        setContentView(renderView);

        // fullscreen and remove support action bar
        if (Build.VERSION.SDK_INT < 16)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        getSupportActionBar().hide();

        // create engine and scene
        AssetManager aMan = this.getBaseContext().getAssets();
        EngineAndroid engine = new EngineAndroid(renderView, aMan, 4.0f/6.0f, 0xFFFFFFFF);
        BootScene bootScene = new BootScene();

        // start up
        engine.getSceneManager().pushScene(bootScene, engine);
        engine.getAudio().playMusic();
        engine.resume();
    }
}