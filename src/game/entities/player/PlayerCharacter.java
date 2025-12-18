package game.entities.player;

import java.awt.*;

public class PlayerCharacter {

    private int x;
    private int y;

    // Movement state (controlled by the input layer)
    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;

    // Movement speed in pixels per update
    private int speed = 5;

    private final int size = 50;

    // Keep screen bounds so we can clamp the player inside the viewport
    private final int screenW;
    private final int screenH;

    public PlayerCharacter(int screenWidth, int screenHeight) {
        this.screenW = screenWidth;
        this.screenH = screenHeight;
        this.x = screenWidth / 2 - size / 2;
        this.y = screenHeight / 2 - size / 2;
    }

    // ===== Update =====
    public void update() {
        if (up) {
            y = Math.max(0, y - speed);
        }
        if (down) {
            y = Math.min(screenH - size, y + speed);
        }
        if (left) {
            x = Math.max(0, x - speed);
        }
        if (right) {
            x = Math.min(screenW - size, x + speed);
        }
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

    // ===== Input API =====
    public void setUp(boolean active) { this.up = active; }
    public void setDown(boolean active) { this.down = active; }
    public void setLeft(boolean active) { this.left = active; }
    public void setRight(boolean active) { this.right = active; }

    public void setSpeed(int speed) { this.speed = Math.max(0, speed); }
}
