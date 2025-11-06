package SpielLaunch;
import java.awt.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable{
    private static final long serialVersionUID = 1L;
    boolean game_running = true;

    long delta;
    long last;
    long fps;

    public GamePanel(int w, int h) {
        this.setPreferredSize(new Dimension(w,h));
        JFrame frame = new JFrame("Brotato Clone");
        frame.setLocation(100,100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
        doInitializations();

    }

    private void doInitializations() {
        // Initialization code here

        last = System.nanoTime();

        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        // Game loop code here

        computeDelta();

        repaint();

        while(game_running) {
           
            try {
                Thread.sleep(10); // Approx 60 FPS
            } catch (InterruptedException e) {}

        }

    }

    private void computeDelta() {
        delta = System.nanoTime() - last;
        last = System.nanoTime();

        fps = ((long) 1e9)/delta;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Rendering code here

        g.setColor(Color.RED);
        g.drawString("FPS: " + Long.toString(fps), 20 , 10);
    }

}