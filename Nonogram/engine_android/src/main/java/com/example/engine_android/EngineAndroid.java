package com.example.engine_android;

import android.content.res.AssetManager;
import android.media.AudioManager;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import com.example.engine_common.interfaces.IAudio;
import com.example.engine_common.interfaces.IEngine;
import com.example.engine_common.interfaces.IInput;
import com.example.engine_common.interfaces.IRender;
import com.example.engine_common.interfaces.IScene;
import com.example.engine_common.shared.InputManager;
import com.example.engine_common.shared.InputType;
import com.example.engine_common.shared.SceneManager;

public class EngineAndroid implements IEngine, Runnable {
    private Thread renderThread;
    private boolean running;
    private RenderAndroid render;
    private IScene currentScene;
    private SurfaceView renderView;
    private  AssetManager assetManager;
    private SceneManager mySceneManager;
    private InputManager myInputManager;
    private AudioAndroid myAudioManager;

    public void setScene(IScene s) {
        currentScene = s;
        currentScene.init(this);
    }

    @Override
    public IRender getRender() {
        return render;
    }

    @Override
    public IAudio getAudio() {
        return myAudioManager;
    }

    @Override
    public InputManager getInputManager() {return myInputManager; }

    @Override
    public SceneManager getSceneManager() {
        return mySceneManager;
    }

    public void init(SurfaceView s, AssetManager aM) {
        //Creamos el SurfaceView que "contendrá" nuestra escena
        this.renderView = s;
        this.renderView.setOnTouchListener(new myTouchListener());
        this.assetManager = aM;
        this.myInputManager = new InputManager();
        this.render = new RenderAndroid();
        this.render.init(this.renderView, assetManager);
        this.myAudioManager = new AudioAndroid();
        this.myAudioManager.init(assetManager);
    }

    @Override
    public void run() {
        if (renderThread != Thread.currentThread()) {
            // Evita que cualquiera que no sea esta clase llame a este Runnable en un Thread
            // Programación defensiva
            throw new RuntimeException("run() should not be called directly");
        }

        // Si el Thread se pone en marcha
        // muy rápido, la vista podría todavía no estar inicializada.
        while(this.running && render.getWidth() == 0);
        // Espera activa. Sería más elegante al menos dormir un poco.

        long currentTime = System.currentTimeMillis();
        // Bucle de juego principal.
        while(running) {
            System.out.printf("Conche su madre que significa chainshaw man");
            long deltaTime = System.currentTimeMillis() - currentTime;
            currentTime += deltaTime;

            // handle input
            while(!myInputManager.empty()){
                IInput input = myInputManager.getInput();
                this.currentScene.handleInput(input);
            }
    
            currentScene.update(deltaTime);

            while (!this.render.surfaceValid());

            this.render.clear();
            this.currentScene.render(this.render);
            this.render.present();
        }
    }

    public void resume() {
        if (!this.running) {
            // Solo hacemos algo si no nos estábamos ejecutando ya
            // (programación defensiva)
            this.running = true;
            // Lanzamos la ejecución de nuestro método run() en un nuevo Thread.
            this.renderThread = new Thread(this);
            this.renderThread.start();
        }
    }

    public void pause() {
        if (this.running) {
            this.running = false;
            while (true) {
                try {
                    this.renderThread.join();
                    this.renderThread = null;
                    break;
                } catch (InterruptedException ie) {
                    // Esto no debería ocurrir nunca...
                }
            }
        }
    }
    private class myTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            InputAndroid iA = new InputAndroid((int)motionEvent.getX(), (int)motionEvent.getY(), InputType.values()[motionEvent.getActionMasked()], motionEvent.getActionIndex());
            switch(iA.getType()) {
                case TOUCH_DOWN:
                    myInputManager.addInput(iA);
                    break;
                case TOUCH_UP:
                    break;
                case TOUCH_MOVE:
                    break;
            }
            return true;
        }
    }
}