package game.entities.player;

import java.awt.*;

public class PlayerCharacter {

    private int x;
    private int y;

    private final int size = 50;

    public PlayerCharacter(int screenWidth, int screenHeight) {
        this.x = screenWidth / 2 - size / 2;
        this.y = screenHeight / 2 - size / 2;
    }

    // ===== Update =====
    public void update() {
        // Player movement will be added later
    }

    // ===== Render =====
    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLUE);
        g2.fillRect(x, y, size, size);
    }

    // ===== Position helpers (center-based) =====
    public int getX() {
        return x + size / 2;
    }

    public int getY() {
        return y + size / 2;
    }

    // ===== Collision =====
    public Rectangle getHitbox() {
        return new Rectangle(x, y, size, size);
    }
}
