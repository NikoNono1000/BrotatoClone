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
    boolean started = false;
    boolean once = false;

    long delta = 0;
    long last = 0;
    long fps = 0;

    Heli copter;
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
        this.setFocusable(true);
        this.addKeyListener(this);
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

        actors = new Vector<Sprite>();
        BufferedImage[] heli = this.loadPics("pics/heli.gif", 4);
        copter = new Heli(heli, 100, 100, 100, this);
        actors.add(copter);

        if(!once) {
            once = true;
            Thread t = new Thread(this);
            t.start();
        }
    }

    public void run() {
        
        while (game_running) {

            computeDelta();

            if (isStarted()) {
                checkKeys();
                doLogic();
                moveObjects();
            }


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

        if (!isStarted()) {
            return;
        }

        if(actors != null) {
            for (Drawable draw:actors) {
                draw.drawObjects(g);
            }
        }
    }  

    private BufferedImage[] loadPics(String path, int pics) {

        BufferedImage[] anim = new BufferedImage[pics];
        BufferedImage source = null;

        // Try multiple ways to resolve the resource using the provided `path`:
        // 1) package-relative: getResource("pics/heli.gif") -> looks under BrotatoClone/Spielbauer/
        // 2) absolute classpath: getResource("/pics/heli.gif") or getResource("/BrotatoClone/Spielbauer/pics/heli.gif")
        // 3) classloader lookup
        // 4) filesystem fallback (useful during development)

        URL pic_url = null;

        // 1) package-relative (if path does NOT start with '/')
        try {
            pic_url = getClass().getResource(path);
        } catch (Exception ignored) {
        }

        // 2) absolute classpath (leading '/')
        if (pic_url == null) {
            try {
                if (!path.startsWith("/")) {
                    pic_url = getClass().getResource("/" + path);
                }
            } catch (Exception ignored) {
            }
        }

        // 3) classloader lookup
        if (pic_url == null) {
            pic_url = getClass().getClassLoader().getResource(path.startsWith("/") ? path.substring(1) : path);
        }

        // 4) filesystem fallback
        if (pic_url == null) {
            java.io.File f = new java.io.File(path);
            if (f.exists()) {
                try {
                    source = ImageIO.read(f);
                } catch (IOException e) {
                    System.err.println("ERROR: Failed to load image from file " + path);
                    e.printStackTrace();
                    return anim;
                }
            } else {
                System.err.println("ERROR: Image file not found (classpath+filesystem) for path: " + path);
                return anim;
            }
        } else {
            try {
                source = ImageIO.read(pic_url);
            } catch (IOException e) {
                System.err.println("ERROR: Failed to load image from resource " + pic_url + " (original path: " + path + ")");
                e.printStackTrace();
                return anim;
            }
            if (source == null) {
                System.err.println("ERROR: ImageIO.read returned null for resource " + pic_url + " (original path: " + path + ")");
                return anim;
            }
        }

        for (int x=0;x<pics;x++) {
            anim[x] = source.getSubimage(x*source.getWidth()/pics, 0, 
            source.getWidth()/pics, source.getHeight());
        }
        return anim;
    }

    public void keyPressed(KeyEvent e) {

        if(e.getKeyCode() == KeyEvent.VK_UP) {
            up = true;
        }

        if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            down = true;
        }

        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = true;
        }

        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = true;
        }   

    }

    public void keyReleased(KeyEvent e) {

        if(e.getKeyCode() == KeyEvent.VK_UP) {
            up = false;
        }

        if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            down = false;
        }

        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = false;
        }

        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = false;
        }

        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (isStarted()) {
                setStarted(false);
            } else {
                setStarted(true);
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (isStarted()) {
                setStarted(false);
            } else {
                setStarted(false);
                System.exit(0);
            }
        }

    }

    public void checkKeys() {

        if(up) {
            copter.setVerticalSpeed(-speed);
        } else if(down) {
            copter.setVerticalSpeed(speed);
        } else {
            copter.setVerticalSpeed(0);
        }

        if(left) {
            copter.setHorizontalSpeed(-speed);
        } else if(right) {
            copter.setHorizontalSpeed(speed);
        } else {
            copter.setHorizontalSpeed(0);
        }

    }

    public void keyTyped(KeyEvent e) {

    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

 
}




/// Page 27 
