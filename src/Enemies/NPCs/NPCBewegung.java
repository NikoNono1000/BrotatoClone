package Enemies.NPCs;

import PlayerCharacters.PlayerCharacter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class NPCBewegung {

    // ------------ Innere NPC-Klasse ---------------
    private class NPC {
        double x, y;
        double speed = 10;
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
            g2.drawImage(img, (int)x - img.getWidth() / 2, (int)y - img.getHeight() / 2, null);
        }

        // --- NEU: Hitbox ---
        Rectangle getHitbox() {
            return new Rectangle(
                (int)x - img.getWidth() / 2,
                (int)y - img.getHeight() / 2,
                img.getWidth(),
                img.getHeight()
            );
        }
    }

    // ------------ NPC Manager Felder ----------------
    private ArrayList<NPC> npcs = new ArrayList<>();
    private long lastSpawnTime = 0;
    private long spawnInterval = 50;

    private BufferedImage npcTexture;

    public NPCBewegung() {
        npcTexture = loadTexture();
    }

    // ------------ Textur sicher laden ----------------
    private BufferedImage loadTexture() {
        String filename = "Melee_Enemy.png";

        try {
            // 1) Entwickeln unter /src
            File srcFile = new File("src/Enemies/NPCs/" + filename);
            if (srcFile.exists()) {
                System.out.println("NPC Textur geladen aus src/: " + srcFile.getAbsolutePath());
                return ImageIO.read(srcFile);
            }

            // 2) Kompiliert unter /bin
            File binFile = new File("bin/Enemies/NPCs/" + filename);
            if (binFile.exists()) {
                System.out.println("NPC Textur geladen aus bin/: " + binFile.getAbsolutePath());
                return ImageIO.read(binFile);
            }

            // 3) Tiefensuche im gesamten Projektverzeichnis
            File found = recursiveSearch(new File("."), filename);
            if (found != null) {
                System.out.println("NPC Textur gefunden unter: " + found.getAbsolutePath());
                return ImageIO.read(found);
            }

            System.out.println("FEHLER: NPC Textur NICHT gefunden!");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ------------ Rekursive Suche ----------------
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

    // ------------ NPC-Aktualisierung + Kollision ----------------
    public void update(PlayerCharacter player, int screenW, int screenH) {
        long current = System.currentTimeMillis();

        // Gegner spawnen
        if (current - lastSpawnTime >= spawnInterval) {
            spawnEnemy(screenW, screenH);
            lastSpawnTime = current;
        }

        // Gegner bewegen + Kollision prüfen
        Iterator<NPC> iterator = npcs.iterator();
        while (iterator.hasNext()) {
            NPC n = iterator.next();

            n.update(player);

            // --- KOLLISION: Gegner despawnt ---
            if (n.getHitbox().intersects(player.getHitbox())) {
                iterator.remove();
            }
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
