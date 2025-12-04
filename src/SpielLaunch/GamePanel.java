package SpielLaunch;

import javax.swing.*;
import java.awt.*;

import PlayerCharacters.PlayerCharacter;
import Enemies.NPCs.NPCBewegung;

public class GamePanel extends JPanel implements Runnable {

    private static final long serialVersionUID = 1L;
    private boolean running = true;

    private int targetFPS;
    private int fps = 0;

    private long lastFpsTime = 0;
    private int frames = 0;

    // Spiellogik
    private PlayerCharacter player;
    private NPCBewegung npcManager;

    private int screenW;
    private int screenH;

    public GamePanel(int w, int h, int targetFPS) {
        this.targetFPS = targetFPS;
        this.screenW = w;
        this.screenH = h;

        setPreferredSize(new Dimension(w, h));

        // Spieler & Gegner-System erzeugen
        player = new PlayerCharacter(w, h);
        npcManager = new NPCBewegung();

        new Thread(this).start();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long optimalTime = (targetFPS > 0) ? 1_000_000_000 / targetFPS : 0;

        lastFpsTime = System.currentTimeMillis();

        while (running) {

            long now = System.nanoTime();
            long updateLength = now - lastTime;
            lastTime = now;

            // Spiellogik
            updateGame();

            // Render
            repaint();

            // FPS berechnen
            frames++;
            if (System.currentTimeMillis() - lastFpsTime >= 1000) {
                fps = frames;
                frames = 0;
                lastFpsTime += 1000;
            }

            // FPS-Limiter
            if (targetFPS > 0) {
                long elapsed = System.nanoTime() - now;
                long sleepTime = (optimalTime - elapsed) / 1_000_000;
                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException ignored) {}
                }
            }
        }
    }

    private void updateGame() {
        player.update();
        npcManager.update(player, screenW, screenH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Hintergrund
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Spieler & Gegner zeichnen
        player.draw(g2);
        npcManager.draw(g2);

        // FPS anzeigen
        g2.setColor(Color.RED);
        g2.setFont(new Font("Consolas", Font.PLAIN, 16));
        g2.drawString("FPS: " + fps, 20, 30);
    }
}
