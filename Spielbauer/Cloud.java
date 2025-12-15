package BrotatoClone.Spielbauer;

import java.awt.image.BufferedImage;

public class Cloud extends Sprite {

    final int SPEED = 20;

    public Cloud(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
        super(i, x, y, delay, p);

        if ((int) (Math.random() * 2) > 1) {
            setHorizontalSpeed(-SPEED);
        } else {
            setHorizontalSpeed(SPEED);
        }
    }


    @Override
    public void doLogic(long delta) {
        super.doLogic(delta);
        // Additional logic specific to Cloud can be added here

        if (getHorizontalSpeed() > 0 && getX() > parent.getWidth()) {
            setX(-getWidth());
        }   

        if (getHorizontalSpeed() < 0 && getX() + getWidth() < 0) {
            setX(parent.getWidth() + getWidth());
        }
    }

    
}
