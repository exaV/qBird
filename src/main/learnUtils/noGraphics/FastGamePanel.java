package main.learnUtils.noGraphics;

import main.game.Game;
import main.game.Render;
import main.game.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Created by P on 05.12.2015.
 */
public class FastGamePanel extends JPanel implements Runnable {

    protected Game game;


    public FastGamePanel(Game game) {
        this.game = game;
        new Thread(this).start();
    }

    public void update() {
        game.update();
    }

    protected void paintComponent(Graphics g) {

        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.BLUE);

        g2D.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g2D.drawString("Learning, press S to save", 100, 270);

        Image bird = Util.loadImage("lib/bird.png");
        Render r = new Render();
        r.transform = new AffineTransform();
        r.transform.translate(50,150);

        g2D.drawImage(bird,r.transform,null);
    }

    public void run() {
        repaint();
        try {
            while (true) {
                update();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
