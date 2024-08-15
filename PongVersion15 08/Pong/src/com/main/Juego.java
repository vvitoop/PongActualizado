package com.main;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Juego extends Canvas implements Runnable {

    private static final long serialVersionUID = 1L;

    public final static int WIDTH = 1000;
    public final static int HEIGHT = WIDTH * 9 / 16;
    public boolean running = false;
    private Thread gameThread;
    private Pelota ball;
    private Paleta leftPaddle;
    private Paleta rightPaddle;
    private MenuPrincipal menu;

    private static final int SCORE_LIMIT = 7;
    private static final int SCORE_DIFFERENCE = 2;
    private boolean gameOver = false;
    private boolean firstHalf = true;
    private long periodStartTime;
    private static final long PERIOD_DURATION = 60000; 

    public Juego() {
        canvasSetup();
        new Ventana("Pong", this);
        initialise();
        this.addKeyListener(new KeyInput(leftPaddle, rightPaddle, this));
        this.addMouseListener(menu);
        this.addMouseMotionListener(menu);
        this.setFocusable(true);
        periodStartTime = System.currentTimeMillis();
    }

    private void initialise() {
        ball = new Pelota();
        leftPaddle = new Paleta(Color.green, true);
        rightPaddle = new Paleta(Color.red, false);
        menu = new MenuPrincipal(this);
    }

    private void canvasSetup() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
    }

    @Override
    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                update();
                delta--;
                draw();
                frames++;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
        stop();
    }

    public synchronized void start() {
        gameThread = new Thread(this);
        gameThread.start();
        running = true;
    }

    public void stop() {
        try {
            gameThread.join();
            running = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (!menu.active && !gameOver) {
            ball.update(leftPaddle, rightPaddle);
            leftPaddle.update(ball);
            rightPaddle.update(ball);
            checkGameOver();
            checkHalfTime();
        }
    }

    private void checkGameOver() {
        if ((leftPaddle.getScore() >= SCORE_LIMIT || rightPaddle.getScore() >= SCORE_LIMIT) &&
            Math.abs(leftPaddle.getScore() - rightPaddle.getScore()) >= SCORE_DIFFERENCE) {
            gameOver = true;
        }
    }

    private void checkHalfTime() {
        if (System.currentTimeMillis() - periodStartTime >= PERIOD_DURATION) {
            firstHalf = false;
            periodStartTime = System.currentTimeMillis();
            swapControls();
            ball.reset();
        }
    }

    private void swapControls() {
        KeyInput oldKeyInput = (KeyInput) this.getKeyListeners()[0];
        this.removeKeyListener(oldKeyInput);
        
        KeyInput newKeyInput = new KeyInput(rightPaddle, leftPaddle, this);
        this.addKeyListener(newKeyInput);
    }

    public void draw() {
        BufferStrategy buffer = this.getBufferStrategy();
        if (buffer == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = buffer.getDrawGraphics();
        drawBackground(g);

        if (menu.active)
            menu.draw(g);

        ball.draw(g);
        leftPaddle.draw(g);
        rightPaddle.draw(g);

        drawTime(g);
        drawHalfIndicator(g);

        if (gameOver) {
            drawGameOver(g);
        }

        g.dispose();
        buffer.show();
    }

    private void drawBackground(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.white);
        Graphics2D g2d = (Graphics2D) g;
        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 10 }, 0);
        g2d.setStroke(dashed);
        g.drawLine(WIDTH / 2, 0, WIDTH / 2, HEIGHT);
    }

    private void drawTime(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        long remainingTime = PERIOD_DURATION - (System.currentTimeMillis() - periodStartTime);
        g.drawString("Tiempo: " + (remainingTime / 1000), WIDTH / 2 - 250, 30);
    }

    private void drawHalfIndicator(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        String half = firstHalf ? "1er Tiempo" : "2do Tiempo";
        g.drawString(half, WIDTH / 2 - 250, 60);
    }

    private void drawGameOver(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        String winner = (leftPaddle.getScore() > rightPaddle.getScore()) ? "Jugador 1" : "Jugador 2";
        String message = winner + " gana!";
        int strWidth = g.getFontMetrics().stringWidth(message);
        g.drawString(message, WIDTH / 2 - strWidth / 2, HEIGHT / 2);

        g.setFont(new Font("Arial", Font.PLAIN, 30));
        message = "Presiona ENTER para reiniciar";
        strWidth = g.getFontMetrics().stringWidth(message);
        g.drawString(message, WIDTH / 2 - strWidth / 2, HEIGHT / 2 + 50);
    }

    public static int ensureRange(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    public static int sign(double d) {
        if (d <= 0)
            return -1;
        return 1;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void resetGame() {
        leftPaddle.resetScore();
        rightPaddle.resetScore();
        ball.reset();
        gameOver = false;
        firstHalf = true;
        periodStartTime = System.currentTimeMillis();
        swapControls();
    }

    public static void main(String[] args) {
        new Juego();
    }
}
