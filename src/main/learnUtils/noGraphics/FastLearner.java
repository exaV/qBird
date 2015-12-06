package main.learnUtils.noGraphics;

import main.game.Keyboard;
import main.learnUtils.LearnGame;

import javax.swing.*;

/**
 * Created by P on 05.12.2015.
 */
public class FastLearner {
    public static int WIDTH = 500;
    public static int HEIGHT = 500;

    public static final String startValuesForQFile = "q_knowledge1000/Q_save2015_12_06__00_42_52_406__951.dat";

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);

        Keyboard keyboard = Keyboard.getInstance();
        frame.addKeyListener(keyboard);

        FastGamePanel panel = new FastGamePanel(new LearnGame());
        frame.add(panel);
    }

}
