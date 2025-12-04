package BrotatoClone.Spielbauer;
import BrotatoClone.Spielbauer.Movable;
import BrotatoClone.Spielbauer.Drawable;
import java.awt.Color;
//package Java.Helikopter game;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Vector;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
//implements Runnable, KeyListener
public class GamePanel extends JPanel implements Runnable, KeyListener {
    
    private static final long serialVersionUID = 1L;
    boolean game_running = true;

    long delta = 0;
    long last = 0;
    long fps = 0;

    Sprite copter;
    Vector<Sprite> actors;

    boolean up = false;
    boolean down = false;
    boolean left = false;
    boolean right = false;
    int speed = 50;

    public static void main(String[] args) {
        new GamePanel(800,600);
    }


    public GamePanel(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        JFrame frame = new JFrame("GameDemo");
        frame.setLocation(100, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(this);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
        doInitializations();
    }

    private void doInitializations() {

        last = System.nanoTime();

        actors = new Vector<Sprite>();
        BufferedImage[] heli = this.loadPics("pics/heli.gif", 4);
        copter = new Sprite(heli, 100, 100, 100, this);
        actors.add(copter);

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

    private void checkKeys() {
        // placeholder: implement keyboard handling here
    }

    private void doLogic() {
        for (Movable move:actors) {
            move.doLogic(delta);
        }
    }

    private void moveObjects() {
        for (Movable move:actors) {
            move.move(delta);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.red);
        g.drawString("FPS: " + Long.toString(fps), 20, 10);

        if(actors != null) {
            for (Drawable draw:actors) {
                draw.drawObjects(g);
            }
        }
    }  

    private BufferedImage[] loadPics(String path, int pics) {

        BufferedImage[] anim = new BufferedImage[pics];
        BufferedImage source = null;

        URL pic_url = getClass().getClassLoader().getResource(path);

        try {
            source = ImageIO.read(pic_url); 
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int x=0;x<pics;x++) {
            anim[x] = source.getSubimage(x*source.getWidth()/pics, 0, 
            source.getWidth()/pics, source.getHeight());
        }
        return anim;
    }

    public void keyPressed(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {

    }

    public void keyTyped(KeyEvent e) {

    }

 
}




/// Page 25 
