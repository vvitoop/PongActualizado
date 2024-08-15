package com.main;

import java.awt.Color;
import java.awt.Graphics;

public class Pelota {

    public static final int SIZE = 16;

    private int x, y;
    private int xVel, yVel;
    private int speed = 5;

    public Pelota() {
        reset();
    }

    void reset() {
        x = Juego.WIDTH / 2 - SIZE / 2;
        y = Juego.HEIGHT / 2 - SIZE / 2;

        xVel = Juego.sign(Math.random() * 2.0 - 1);
        yVel = Juego.sign(Math.random() * 2.0 - 1);
    }

    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(x, y, SIZE, SIZE);
    }

    public void update(Paleta lp, Paleta rp) {
        x += xVel * speed;
        y += yVel * speed;

        if (y + SIZE >= Juego.HEIGHT || y <= 0)
            changeYDir();

        if (x + SIZE >= Juego.WIDTH) {
            lp.addPoint();
            reset();
        }
        if (x <= 0) {
            rp.addPoint();
            reset();
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void changeXDir() {
        xVel *= -1;
    }

    public void changeYDir() {
        yVel *= -1;
    }
}
