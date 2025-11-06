package SpielLaunch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartScreen extends JFrame {

    private JCheckBox fullscreenCheck;
    private JButton startButton;

    public StartScreen() {
        super("Brotato Clone – Start");

        // Fenster-Grundlayout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("BROTATO CLONE", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0;

        // Fullscreen-Option
        fullscreenCheck = new JCheckBox("Fullscreen");
        fullscreenCheck.setFont(new Font("Arial", Font.PLAIN, 16));
        centerPanel.add(fullscreenCheck, gbc);

        // Start-Button
        gbc.gridy++;
        startButton = new JButton("START GAME");
        startButton.setFont(new Font("Arial", Font.BOLD, 18));
        startButton.setFocusPainted(false);
        centerPanel.add(startButton, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Button-Aktion
        startButton.addActionListener(e -> startGame());

        setVisible(true);
    }

    private void startGame() {
        boolean fullscreen = fullscreenCheck.isSelected();
        dispose(); // Menü schließen

        if (fullscreen) {
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            JFrame frame = new JFrame(gd.getDefaultConfiguration());
            frame.setUndecorated(true);
            gd.setFullScreenWindow(frame);
            frame.add(new GamePanel(frame.getWidth(), frame.getHeight()));
        } else {
            new GamePanel(1000, 800);
        }
    }
}
