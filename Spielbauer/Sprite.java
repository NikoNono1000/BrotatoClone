package BrotatoClone.Spielbauer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;

public class Sprite extends Rectangle2D.Double implements Drawable, Movable {

    long delay;
    long animation = 0;
    GamePanel parent;
    BufferedImage[] pics;
    int currentpic = 0;
    protected double dx;
    protected double dy;

    public Sprite(BufferedImage[] i, double x, double y, long delay, GamePanel p) {

        pics = i;
        this.x = x;
        this.y = y;
        this.delay = delay;
        this.width = pics[0].getWidth();
        this.height = pics[0].getHeight();
        parent = p;

    }

    public void drawObjects(Graphics g) {
        if (pics == null || pics.length == 0) return;
        g.drawImage(pics[currentpic], (int) this.x, (int) this.y, null);
    }

    public void doLogic(long delta) {
        
        animation += (delta/1000000);
        if (animation > delay) {
            animation = 0;
            computerAnimation();
        }
    }

    private void computerAnimation() {

        currentpic++;

        if(currentpic>=pics.length) {
            currentpic = 0;
        }
    }

    public void setVerticalSpeed(double d) {
        dy = d;
    }

    public void setHorizontalSpeed(double d) {
        dx = d;
    }

    public double getVerticalSpeed() {
        return dy;
    }

    public double getHorizontalSpeed() {
        return dx;
    }

    public void move(long Delta) {

        if(dx != 0) {
            x += dx * (Delta / 1e9);
        }

        if(dy != 0) {
            y += dy * (Delta / 1e9);
        }
    }
    
}

/// Page 17 stoped 
