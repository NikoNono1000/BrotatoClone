//package Java.Helikopter game;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
//implements Runnable, KeyListener
public class GamePanel extends JPanel implements Runnable {
    
    private static final long serialVersionUID = 1L;
    boolean game_running = true;

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
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        
        while (game_running) {

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }       

}
