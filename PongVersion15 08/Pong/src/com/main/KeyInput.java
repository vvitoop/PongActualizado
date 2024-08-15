package com.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

    private Paleta lp;
    private boolean lup = false;
    private boolean ldown = false;

    private Paleta rp;
    private boolean rup = false;
    private boolean rdown = false;

    private Juego game;

    public KeyInput(Paleta p1, Paleta p2, Juego game) {
        lp = p1;
        rp = p2;
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP) {
            rp.switchDirections(-1);
            rup = true;
        }
        if (key == KeyEvent.VK_DOWN) {
            rp.switchDirections(1);
            rdown = true;
        }
        if (key == KeyEvent.VK_W) {
            lp.switchDirections(-1);
            lup = true;
        }
        if (key == KeyEvent.VK_S) {
            lp.switchDirections(1);
            ldown = true;
        }

        if (key == KeyEvent.VK_ESCAPE) {
            System.exit(1);
        }

        if (key == KeyEvent.VK_ENTER && game.isGameOver()) {
            game.resetGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP) {
            rup = false;
        }
        if (key == KeyEvent.VK_DOWN) {
            rdown = false;
        }
        if (key == KeyEvent.VK_W) {
            lup = false;
        }
        if (key == KeyEvent.VK_S) {
            ldown = false;
        }

        if (!lup && !ldown)
            lp.stop();
        if (!rup && !rdown)
            rp.stop();
    }
}
