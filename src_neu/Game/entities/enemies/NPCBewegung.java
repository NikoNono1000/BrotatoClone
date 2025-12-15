package game.entities.enemies;

import game.entities.player.PlayerCharacter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NPCBewegung {

    // ======= Enemy scaling =======
    private static final double ENEMY_SCALE = 0.2;

    // ======= Spawn settings =======
    private static final long SPAWN_INTERVAL_MS = 5000;

    // ======= Internal NPC class =======
    private class NPC {
        double x, y;
        double speed = 1.5;
        BufferedImage img;

        NPC(int spawnX, int spawnY, BufferedImage img) {
            this.x = spawnX;
            this.y = spawnY;
            this.img = img;
        }

        void update(PlayerCharacter player) {
            double dx = player.getX() - x;
            double dy = player.getY() - y;
            double dist = Math.sqrt(dx * dx + dy * dy);

            if (dist > 0) {
                x += (dx / dist) * speed;
                y += (dy / dist) * speed;
            }
        }

        void draw(Graphics2D g2) {
            g2.drawImage(
                    img,
                    (int) x - img.getWidth() / 2,
                    (int) y - img.getHeight() / 2,
                    null
            );
        }

        Rectangle getHitbox() {
            return new Rectangle(
                    (int) x - img.getWidth() / 2,
                    (int) y - img.getHeight() / 2,
                    img.getWidth(),
                    img.getHeight()
            );
        }
    }

    // ======= NPC storage (thread-safe) =======
    private final CopyOnWriteArrayList<NPC> npcs = new CopyOnWriteArrayList<>();

    private long lastSpawnTime = 0;
    private final BufferedImage npcTexture;

    public NPCBewegung() {
        npcTexture = loadAndScaleTexture(
                "/textures/enemies/melee_enemy.png",
                ENEMY_SCALE
        );
    }

    // ======= Resource loading =======
    private BufferedImage loadAndScaleTexture(String path, double scale) {
        try {
            BufferedImage original = ImageIO.read(
                    getClass().getResourceAsStream(path)
            );

            int newW = (int) (original.getWidth() * scale);
            int newH = (int) (original.getHeight() * scale);

            Image scaled = original.getScaledInstance(
                    newW, newH, Image.SCALE_SMOOTH
            );

            BufferedImage buffered = new BufferedImage(
                    newW, newH, BufferedImage.TYPE_INT_ARGB
            );

            Graphics2D g2 = buffered.createGraphics();
            g2.drawImage(scaled, 0, 0, null);
            g2.dispose();

            return buffered;

        } catch (IOException | NullPointerException e) {
            throw new RuntimeException(
                    "Failed to load enemy texture: " + path, e
            );
        }
    }

    // ======= Update =======
    public void update(PlayerCharacter player, int screenW, int screenH) {
        long now = System.currentTimeMillis();

        if (now - lastSpawnTime >= SPAWN_INTERVAL_MS) {
            spawnEnemy(screenW, screenH);
            lastSpawnTime = now;
        }

        List<NPC> toRemove = new java.util.ArrayList<>();

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

    // ======= Spawn logic =======
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

    // ======= Draw =======
    public void draw(Graphics2D g2) {
        for (NPC npc : npcs) {
            npc.draw(g2);
        }
    }
}
