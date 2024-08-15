package com.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Paleta {
    private int x, y;
    private int vel = 0;
    private int speed = 10;
    private int width = 22, height = 85;
    private int score = 0;
    private Color color;
    private boolean left;

    public Paleta(Color c, boolean left) {
        color = c;
        this.left = left;
        if (left)
            x = 0;
        else
            x = Juego.WIDTH - width;
        y = Juego.HEIGHT / 2 - height / 2;
    }

    public void addPoint() {
        score++;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
        int sx;
        int padding = 25;
        String scoreText = Integer.toString(score);
        Font font = new Font("Roboto", Font.PLAIN, 50);
        if (left) {
            int strWidth = g.getFontMetrics(font).stringWidth(scoreText);
            sx = Juego.WIDTH / 2 - padding - strWidth;
        } else {
            sx = Juego.WIDTH / 2 + padding;
        }
        g.setFont(font);
        g.drawString(scoreText, sx, 50);
    }

    public void update(Pelota b) {
        y = Juego.ensureRange(y + vel, 0, Juego.HEIGHT - height);
        int ballX = b.getX();
        int ballY = b.getY();
        if (left) {
            if (ballX <= width + x && ballY + Pelota.SIZE >= y && ballY <= y + height)
                b.changeXDir();
        } else {
            if (ballX + Pelota.SIZE >= x && ballY + Pelota.SIZE >= y && ballY <= y + height)
                b.changeXDir();
        }
    }

    public int getScore() {
        return score;
    }

    public void resetScore() {
        score = 0;
    }

    public void switchDirections(int direction) {
        vel = speed * direction;
    }

    public void stop() {
        vel = 0;
    }
}