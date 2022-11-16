import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class chat extends JFrame {

    private JPanel mainChat;
    private JTextField text;
    private JPanel view;
    private JButton button1;

    public chat(String title) throws HeadlessException {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainChat);
        this.pack();
    }

    public static void main(String[] args) throws IOException {
          JFrame frame = new chat("my chat");
          frame.setVisible(true);
    }
}