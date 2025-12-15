package game.core;

import game.entities.enemies.NPCBewegung;
import game.entities.player.PlayerCharacter;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    private static final long serialVersionUID = 1L;

    private boolean running = true;

    // FPS / Timing
    private final int targetFPS;
    private int fps = 0;

    private long lastFpsTime;
    private int frames;

    // Screen
    private final int screenW;
    private final int screenH;

    // Game objects
    private final PlayerCharacter player;
    private final NPCBewegung npcManager;

    public GamePanel(int width, int height, int targetFPS) {
        this.screenW = width;
        this.screenH = height;
        this.targetFPS = targetFPS;

        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        requestFocusInWindow();

        // Entities
        player = new PlayerCharacter(width, height);
        npcManager = new NPCBewegung();

        Thread gameThread = new Thread(this, "GameLoop");
        gameThread.start();
    }

    @Override
    public void run() {
        final long optimalTime = (targetFPS > 0)
                ? 1_000_000_000L / targetFPS
                : 0;

        long lastTime = System.nanoTime();
        lastFpsTime = System.currentTimeMillis();

        while (running) {
            long now = System.nanoTime();
            long delta = now - lastTime;
            lastTime = now;

            updateGame();
            repaint();

            calculateFPS();

            // FPS limiter (disabled if Unlimited)
            if (targetFPS > 0) {
                long elapsed = System.nanoTime() - now;
                long sleepTime = (optimalTime - elapsed) / 1_000_000;

                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }
    }

    private void updateGame() {
        player.update();
        npcManager.update(player, screenW, screenH);
    }

    private void calculateFPS() {
        frames++;
        if (System.currentTimeMillis() - lastFpsTime >= 1000) {
            fps = frames;
            frames = 0;
            lastFpsTime += 1000;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Background
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Draw entities
        player.draw(g2);
        npcManager.draw(g2);

        // FPS display
        g2.setColor(Color.RED);
        g2.setFont(new Font("Consolas", Font.PLAIN, 16));
        g2.drawString("FPS: " + fps, 20, 30);
    }

    public void stop() {
        running = false;
    }
}
