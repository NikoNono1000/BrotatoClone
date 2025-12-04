package PlayerCharacters;

import java.awt.*;

public class PlayerCharacter {

    // Position und Größe
    private int x;
    private int y;
    private int size = 50;

    public PlayerCharacter(int screenWidth, int screenHeight) {
        // Spieler in der Mitte positionieren
        this.x = screenWidth / 2 - size / 2;
        this.y = screenHeight / 2 - size / 2;
    }

    public void update() {
        // Spieler bleibt statisch – vorerst leer
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLUE);
        g2.fillRect(x, y, size, size);
    }

    public int getX() { return x + size / 2; }
    public int getY() { return y + size / 2; }
}
