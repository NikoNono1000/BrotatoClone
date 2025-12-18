package game.entities.player;

import java.awt.*;

/**
 * PlayerCharacter represents the user-controlled player.
 * <p>
 * Responsibilities:
 * - Stores position and size
 * - Accepts input through simple setters (`setUp`, `setDown`, ...)
 * - Moves each frame inside `update()` and stays clamped to screen bounds
 */
public class PlayerCharacter {

    private int x;
    private int y;

    // Movement state (controlled by the input layer)
    // These flags are toggled by the input handler and read by `update()`.
    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;

    // Movement speed in pixels per update. Increase for faster movement or
    // use a delta-time based movement model later if needed.
    private int speed = 5;

    private final int size = 50;

    // Keep screen bounds so we can clamp the player inside the viewport
    private final int screenW;
    private final int screenH;

    // Initialize player centered on the screen and store bounds for clamping.
    public PlayerCharacter(int screenWidth, int screenHeight) {
        this.screenW = screenWidth;
        this.screenH = screenHeight;
        this.x = screenWidth / 2 - size / 2;
        this.y = screenHeight / 2 - size / 2;
    }

    // ===== Update =====
    // Moves the player according to the active input flags and clamps
    // the position so the player stays visible inside the screen.
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
    // Draw the player as a filled rectangle (placeholder simple sprite)
    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLUE);
        g2.fillRect(x, y, size, size);
    }

    // ===== Position helpers (center-based) =====
    // getX()/getY() return the center of the player (useful for targeting / distance)
    public int getX() {
        return x + size / 2;
    }

    public int getY() {
        return y + size / 2;
    }

    // ===== Collision =====
    // Simple AABB hitbox matching the player's rectangle.
    public Rectangle getHitbox() {
        return new Rectangle(x, y, size, size);
    }

    // ===== Input API =====
    // These setters are intentionally simple so the input layer (the GamePanel
    // or keyboard listener) can toggle movement without exposing internal state.
    public void setUp(boolean active) { this.up = active; }
    public void setDown(boolean active) { this.down = active; }
    public void setLeft(boolean active) { this.left = active; }
    public void setRight(boolean active) { this.right = active; }

    // Adjust movement speed at runtime if needed
    public void setSpeed(int speed) { this.speed = Math.max(0, speed); }
}
