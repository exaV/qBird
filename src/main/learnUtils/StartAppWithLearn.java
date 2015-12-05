package main.learnUtils;

import main.game.GamePanel;
import main.game.Keyboard;

import javax.swing.*;
import java.time.LocalTime;

/**
 * Created by P on 29.11.2015.
 */
public class StartAppWithLearn {
    public static int WIDTH = 500;
    public static int HEIGHT = 500;
    public static final String startValuesForQFile = "q_knowledge/Q_save2015_12_05__14_53_33_336__587.dat";

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);

        Keyboard keyboard = Keyboard.getInstance();
        frame.addKeyListener(keyboard);

        GamePanel panel = new LearnGamePanel(startValuesForQFile);
        frame.add(panel);
    }
}
