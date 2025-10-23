import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel{
    private static final long serialVersionUID = 1L;

    public GamePanel(int w, int h) {
        this.setPreferredSize(new Dimension(w,h));
        JFrame frame = new JFrame("Brotato Clone");
        frame.setLocation(100,100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);

    }

}
