package SpielLaunch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartScreen extends JFrame {

    // Display mode
    private final String[] displayModes = {"Windowed", "Borderless Windowed", "Fullscreen"};
    private int currentModeIndex = 0;
    private JLabel modeLabel;

    // FPS options
    private final String[] fpsOptions = {"30", "60", "120", "144", "Unlimited"};
    private int currentFpsIndex = 1; // default = 60
    private JLabel fpsLabel;

    // Buttons
    private JButton leftArrowMode, rightArrowMode;
    private JButton leftArrowFps, rightArrowFps;
    private JButton startButton;

    public StartScreen() {
        super("Brotato Clone – Start");

        // === Fenster-Basis ===
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === Titel ===
        JLabel title = new JLabel("BROTATO CLONE", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        add(title, BorderLayout.NORTH);

        // === Hauptpanel ===
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.gridx = 0; gbc.gridy = 0;

        // === Display Mode Auswahl ===
        leftArrowMode = new JButton("←");
        rightArrowMode = new JButton("→");
        styleArrow(leftArrowMode);
        styleArrow(rightArrowMode);
        leftArrowMode.addActionListener(e -> changeMode(-1));
        rightArrowMode.addActionListener(e -> changeMode(1));

        modeLabel = new JLabel(displayModes[currentModeIndex], SwingConstants.CENTER);
        modeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        modeLabel.setPreferredSize(new Dimension(250, 40));

        centerPanel.add(leftArrowMode, gbc);
        gbc.gridx++;
        centerPanel.add(modeLabel, gbc);
        gbc.gridx++;
        centerPanel.add(rightArrowMode, gbc);

        // === FPS Auswahl ===
        gbc.gridx = 0;
        gbc.gridy++;
        leftArrowFps = new JButton("←");
        rightArrowFps = new JButton("→");
        styleArrow(leftArrowFps);
        styleArrow(rightArrowFps);
        leftArrowFps.addActionListener(e -> changeFps(-1));
        rightArrowFps.addActionListener(e -> changeFps(1));

        fpsLabel = new JLabel("FPS Limit: " + fpsOptions[currentFpsIndex], SwingConstants.CENTER);
        fpsLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        fpsLabel.setPreferredSize(new Dimension(250, 40));

        centerPanel.add(leftArrowFps, gbc);
        gbc.gridx++;
        centerPanel.add(fpsLabel, gbc);
        gbc.gridx++;
        centerPanel.add(rightArrowFps, gbc);

        // === Start Button ===
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 3;
        startButton = new JButton("START GAME");
        startButton.setFont(new Font("Arial", Font.BOLD, 22));
        startButton.setFocusPainted(false);
        startButton.addActionListener(e -> startGame());
        centerPanel.add(startButton, gbc);

        add(centerPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void styleArrow(JButton btn) {
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(60, 40));
    }

    private void changeMode(int dir) {
        currentModeIndex += dir;
        if (currentModeIndex < 0) currentModeIndex = displayModes.length - 1;
        if (currentModeIndex >= displayModes.length) currentModeIndex = 0;
        modeLabel.setText(displayModes[currentModeIndex]);
    }

    private void changeFps(int dir) {
        currentFpsIndex += dir;
        if (currentFpsIndex < 0) currentFpsIndex = fpsOptions.length - 1;
        if (currentFpsIndex >= fpsOptions.length) currentFpsIndex = 0;
        fpsLabel.setText("FPS Limit: " + fpsOptions[currentFpsIndex]);
    }

    private int getSelectedFpsValue() {
        String val = fpsOptions[currentFpsIndex];
        if (val.equals("Unlimited")) return 0;
        return Integer.parseInt(val);
    }

    private void startGame() {
        String selectedMode = displayModes[currentModeIndex];
        int selectedFps = getSelectedFpsValue();
        dispose();

        switch (selectedMode) {
            case "Windowed" -> new GamePanel(1000, 800, selectedFps);
            case "Borderless Windowed" -> launchBorderlessWindowed(selectedFps);
            case "Fullscreen" -> launchFullscreen(selectedFps);
        }
    }

    private void launchBorderlessWindowed(int fps) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame("Brotato Clone");
        frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GamePanel((int) screen.getWidth(), (int) screen.getHeight(), fps));
        frame.setVisible(true);
        addEscapeToExit(frame);
    }

    private void launchFullscreen(int fps) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        JFrame frame = new JFrame(gd.getDefaultConfiguration());
        frame.setUndecorated(true);
        frame.setResizable(false);

        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();

        frame.add(new GamePanel(width, height, fps));
        gd.setFullScreenWindow(frame);
        addEscapeToExit(frame);
    }

    private void addEscapeToExit(JFrame frame) {
        JRootPane root = frame.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("ESCAPE"), "exit");
        root.getActionMap().put("exit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .setFullScreenWindow(null);
                frame.dispose();
                System.exit(0);
            }
        });
    }
}
