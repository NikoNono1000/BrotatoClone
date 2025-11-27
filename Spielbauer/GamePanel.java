package BrotatoClone.Spielbauer;
import BrotatoClone.Spielbauer.Movable;
import BrotatoClone.Spielbauer.Drawable;
import java.awt.Color;
//package Java.Helikopter game;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
//implements Runnable, KeyListener
public class GamePanel extends JPanel implements Runnable {
    
    private static final long serialVersionUID = 1L;
    boolean game_running = true;

    long delta = 0;
    long last = 0;
    long fps = 0;

    public static void main(String[] args) {
        new GamePanel(800,600);
    }


    public GamePanel(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        JFrame frame = new JFrame("GameDemo");
        frame.setLocation(100, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
        doInitializations();
    }

    private void doInitializations() {

        last = System.nanoTime();

        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        
        while (game_running) {

            computeDelta();
            checkKeys();
            doLogic();
            moveObjects();


            repaint();


            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }     
    
    private void computeDelta() {
        long now = System.nanoTime();

        delta = now - last;
        last = now;

        fps = ((long) 1e9) /  delta;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.red);
        g.drawString("FPS: " + Long.toString(fps), 20, 10);
    }  


}





