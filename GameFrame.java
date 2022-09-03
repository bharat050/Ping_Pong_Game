import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    GamePanel panel;

    GameFrame()
    {
        panel = new GamePanel();
        this.setTitle("Ping Pong");
        this.add(panel);
        this.pack();
        this.setResizable(false);
        this.setBackground(Color.black);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
