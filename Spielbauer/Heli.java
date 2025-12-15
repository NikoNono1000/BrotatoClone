package BrotatoClone.Spielbauer;

import java.awt.image.BufferedImage;

public class Heli extends Sprite {

    public Heli(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
        super(i, x, y, delay, p);
    }
    
    @Override
    public void doLogic(long delta) {
        super.doLogic(delta);
        // Additional logic specific to Heli can be added here

        if(getX() < 0) {
            setHorizontalSpeed(0);
            x = 0;
        }

        if(getY() + getWidth() > parent.getWidth()) {
            setX(parent.getWidth() - getWidth());
            setHorizontalSpeed(0);
        }

        if(getY() < 0) {
            setY(0);
            setVerticalSpeed(0);
        }

        if(getX() + getWidth() > parent.getWidth()) {
            setX(parent.getWidth() - getWidth());
            setHorizontalSpeed(0);
        }


    }

}
