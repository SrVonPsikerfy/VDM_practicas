package com.example.nonogram;

import com.example.engine_common.interfaces.IInput;
import com.example.engine_common.interfaces.IScene;
import com.example.engine_common.interfaces.IRender;
import com.example.engine_common.interfaces.IEngine;
import com.example.engine_common.shared.FontType;

//Clase interna que representa la escena que queremos pintar
public class MyScene implements IScene {
    private float x;
    private float y;
    private int radius;
    private int speed;
    private Board board;

    private IEngine engRef;

    @Override
    public void init(IEngine eng) {
        this.x=50;
        this.y=50;
        this.radius = 50;
        this.speed = 150;
        board = new Board();
        board.init(5,7, eng);

        engRef = eng;
    }

    @Override
    public void update(double deltaTime) {
        int maxX = this.engRef.getRender().getWidth() - this.radius;

        this.x += this.speed * deltaTime;
        this.y += 2*deltaTime;
        while(this.x < 0 || this.x > maxX-this.radius) {
            // Vamos a pintar fuera de la pantalla. Rectificamos.
            if (this.x < 0) {
                // Nos salimos por la izquierda. Rebotamos.
                this.x = -this.x;
                this.speed *= -1;
            } else if (this.x > maxX-this.radius) {
                // Nos salimos por la derecha. Rebotamos
                this.x = 2 * (maxX-this.radius) - this.x;
                this.speed *= -1;
            }
        }
    }

    @Override
    public void render(IRender renderMng) {
        //this.engRef.getRender().drawCircle((int)this.x, (int)this.y, this.radius);
        board.render(renderMng);
    }

    @Override
    public void handleInput(IInput input) {

    }
}