package main.learnUtils.noGraphics;

import main.game.Game;
import main.game.GamePanel;
import main.game.Keyboard;
import main.learnUtils.LearnGame;
import main.learnUtils.LearnGamePanel;

import javax.swing.*;

/**
 * Created by P on 05.12.2015.
 */
public class FastLearner {
    public static int WIDTH = 500;
    public static int HEIGHT = 500;

    public static final String startValuesForQFile = "q_knowledge/Q_save2015_12_05__15_16_02_331__637.dat";

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);

        Keyboard keyboard = Keyboard.getInstance();
        frame.addKeyListener(keyboard);

        FastGamePanel panel = new FastGamePanel(new LearnGame(startValuesForQFile));
        frame.add(panel);
    }

}
