package SpielLaunch;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    private static final long serialVersionUID = 1L;
    private boolean running = true;

    private int targetFPS; // 0 = unlimited
    private int fps = 0;

    private long lastFpsTime = 0;
    private int frames = 0;

    public GamePanel(int w, int h, int targetFPS) {
        this.targetFPS = targetFPS;

        setPreferredSize(new Dimension(w, h));

        JFrame frame = new JFrame("Brotato Clone");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

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

            // --- Update + Render ---
            repaint();

            frames++;
            if (System.currentTimeMillis() - lastFpsTime >= 1000) {
                fps = frames;
                frames = 0;
                lastFpsTime += 1000;
            }

            // --- FPS Limitierung ---
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Hintergrund
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // FPS Anzeige
        g.setColor(Color.GREEN);
        g.setFont(new Font("Consolas", Font.PLAIN, 16));
        g.drawString("FPS: " + fps, 20, 30);
    }
}
