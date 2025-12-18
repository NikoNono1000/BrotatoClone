package game.entities.enemies;

import game.entities.player.PlayerCharacter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * NPCBewegung manages enemy spawning, updates, and rendering.
 * - Spawns enemies at the screen edges at a fixed interval
 * - Each enemy moves toward the player
 * - Removes enemies on collision with the player
 */
public class NPCBewegung { 

    // ================= CONFIG =================
    // ENEMY_SCALE: scale factor applied to the source texture to reduce its size
    private static final double ENEMY_SCALE = 0.3;
    // SPAWN_INTERVAL_MS: how often (ms) a new enemy is spawned
    private static final long SPAWN_INTERVAL_MS = 2000;
    // ENEMY_SPEED: movement speed in pixels per update for enemies
    private static final double ENEMY_SPEED = 1.5;

    // ================= INNER NPC =================
    // Represents a single enemy instance. Enemies track a position and image
    // and can update themselves to move toward the player's center.
    private static class NPC {
        double x, y;
        BufferedImage img;

        NPC(int x, int y, BufferedImage img) {
            this.x = x;
            this.y = y;
            this.img = img;
        }

        // Move the NPC toward the player's center using a simple normalized vector.
        void update(PlayerCharacter player) {
            double dx = player.getX() - x;
            double dy = player.getY() - y;
            double dist = Math.sqrt(dx * dx + dy * dy);

            if (dist > 0) {
                x += (dx / dist) * ENEMY_SPEED;
                y += (dy / dist) * ENEMY_SPEED;
            }
        }

        // Draw the NPC image centered on (x, y)
        void draw(Graphics2D g2) {
            g2.drawImage(
                    img,
                    (int) x - img.getWidth() / 2,
                    (int) y - img.getHeight() / 2,
                    null
            );
        }

        // Axis-aligned bounding box used for simple collision checks
        Rectangle getHitbox() {
            return new Rectangle(
                    (int) x - img.getWidth() / 2,
                    (int) y - img.getHeight() / 2,
                    img.getWidth(),
                    img.getHeight()
            );
        }
    }

    // ================= FIELDS =================
    private final CopyOnWriteArrayList<NPC> npcs = new CopyOnWriteArrayList<>();
    private final BufferedImage npcTexture;
    private long lastSpawnTime = 0;

    // ================= CONSTRUCTOR =================
    public NPCBewegung() {
        npcTexture = loadEnemyTexture();
    }

    // ================= TEXTURE LOADING =================
    // Loads the enemy texture from the classpath and scales it down by ENEMY_SCALE.
    // Using the class resource path means the image must be available on the runtime classpath
    private BufferedImage loadEnemyTexture() {
        try {
            InputStream is = NPCBewegung.class.getResourceAsStream(
                    "/textures/enemies/melee_enemy.png"
            );

            if (is == null) {
                throw new RuntimeException(
                        "Enemy texture NOT found: /textures/enemies/melee_enemy.png"
                );
            }

            BufferedImage original = ImageIO.read(is);

            int newW = (int) (original.getWidth() * ENEMY_SCALE);
            int newH = (int) (original.getHeight() * ENEMY_SCALE);

            Image scaled = original.getScaledInstance(
                    newW, newH, Image.SCALE_SMOOTH
            );

            BufferedImage result = new BufferedImage(
                    newW, newH, BufferedImage.TYPE_INT_ARGB
            );

            Graphics2D g2 = result.createGraphics();
            g2.drawImage(scaled, 0, 0, null);
            g2.dispose();

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return null; // unreachable, but Java wants it
        }
    }

    // ================= UPDATE =================
    // Spawns new enemies periodically and updates existing ones.
    // Also performs simple collision detection against the player and removes
    // enemies that intersect the player's hitbox.
    public void update(PlayerCharacter player, int screenW, int screenH) {
        long now = System.currentTimeMillis();

        if (now - lastSpawnTime >= SPAWN_INTERVAL_MS) {
            spawnEnemy(screenW, screenH);
            lastSpawnTime = now;
        }

        List<NPC> toRemove = new ArrayList<>();

        for (NPC npc : npcs) {
            npc.update(player);

            if (npc.getHitbox().intersects(player.getHitbox())) {
                toRemove.add(npc);
            }
        }

        if (!toRemove.isEmpty()) {
            npcs.removeAll(toRemove);
        }
    }

    // ================= SPAWN =================
    // Spawn an enemy at a random point on one of the four screen edges.
    private void spawnEnemy(int screenW, int screenH) {
        int side = (int) (Math.random() * 4);
        int x = 0, y = 0;

        switch (side) {
            case 0 -> { x = 0; y = (int) (Math.random() * screenH); }
            case 1 -> { x = screenW; y = (int) (Math.random() * screenH); }
            case 2 -> { x = (int) (Math.random() * screenW); y = 0; }
            case 3 -> { x = (int) (Math.random() * screenW); y = screenH; }
        }

        npcs.add(new NPC(x, y, npcTexture));
    }

    // ================= DRAW =================
    // Draw all active NPCs
    public void draw(Graphics2D g2) {
        for (NPC npc : npcs) {
            npc.draw(g2);
        }
    }
}
