package Enemies.NPCs;

import PlayerCharacters.PlayerCharacter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NPCBewegung {

    // ======= Gegnergröße skalieren =======
    private static final double ENEMY_SCALE = 0.2;

    // ------------ Innere NPC-Klasse ---------------
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

            if (dist != 0) {
                x += (dx / dist) * speed;
                y += (dy / dist) * speed;
            }
        }

        void draw(Graphics2D g2) {
            g2.drawImage(img, (int)x - img.getWidth() / 2,
                    (int)y - img.getHeight() / 2, null);
        }

        Rectangle getHitbox() {
            return new Rectangle(
                    (int)x - img.getWidth() / 2,
                    (int)y - img.getHeight() / 2,
                    img.getWidth(),
                    img.getHeight()
            );
        }
    }

    // ------------ Thread-sichere Liste ----------------
    private CopyOnWriteArrayList<NPC> npcs = new CopyOnWriteArrayList<>();

    private long lastSpawnTime = 0;
    private long spawnInterval = 5000;

    private BufferedImage npcTexture;

    public NPCBewegung() {
        npcTexture = loadTexture();
        npcTexture = scaleImage(npcTexture, ENEMY_SCALE);
    }

    // ------------ Textur laden ----------------
    private BufferedImage loadTexture() {
        String filename = "Melee_Enemy.png";

        try {
            File srcFile = new File("src/Enemies/NPCs/" + filename);
            if (srcFile.exists()) return ImageIO.read(srcFile);

            File binFile = new File("bin/Enemies/NPCs/" + filename);
            if (binFile.exists()) return ImageIO.read(binFile);

            File found = recursiveSearch(new File("."), filename);
            if (found != null) return ImageIO.read(found);

            System.out.println("FEHLER: NPC Textur NICHT gefunden!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ======= Skalierungsmethode =======
    private BufferedImage scaleImage(BufferedImage original, double scale) {
        if (original == null) return null;

        int newW = (int) (original.getWidth() * scale);
        int newH = (int) (original.getHeight() * scale);

        Image scaled = original.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);

        BufferedImage buffered = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = buffered.createGraphics();
        g2.drawImage(scaled, 0, 0, null);
        g2.dispose();

        return buffered;
    }

    private File recursiveSearch(File dir, String name) {
        File[] files = dir.listFiles();
        if (files == null) return null;

        for (File f : files) {
            if (f.isDirectory()) {
                File result = recursiveSearch(f, name);
                if (result != null) return result;
            } else if (f.getName().equals(name)) {
                return f;
            }
        }
        return null;
    }

    // ------------ Update + Collision ----------------
    public void update(PlayerCharacter player, int screenW, int screenH) {
        long current = System.currentTimeMillis();

        if (current - lastSpawnTime >= spawnInterval) {
            spawnEnemy(screenW, screenH);
            lastSpawnTime = current;
        }

        // Sicher: wir sammeln zu entfernende Gegner in einer Liste
        List<NPC> toRemove = new ArrayList<>();

        for (NPC n : npcs) {
            n.update(player);

            if (n.getHitbox().intersects(player.getHitbox())) {
                toRemove.add(n);
            }
        }

        if (!toRemove.isEmpty()) {
            npcs.removeAll(toRemove);
        }
    }

    private void spawnEnemy(int screenW, int screenH) {
        int side = (int)(Math.random() * 4);

        int x = 0, y = 0;
        switch (side) {
            case 0 -> { x = 0; y = (int)(Math.random() * screenH); }
            case 1 -> { x = screenW; y = (int)(Math.random() * screenH); }
            case 2 -> { x = (int)(Math.random() * screenW); y = 0; }
            case 3 -> { x = (int)(Math.random() * screenW); y = screenH; }
        }

        npcs.add(new NPC(x, y, npcTexture));
    }

    public void draw(Graphics2D g2) {
        for (NPC n : npcs) {
            n.draw(g2);
        }
    }
}
