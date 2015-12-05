package main.learnUtils;

import main.game.GamePanel;

/**
 * Created by P on 29.11.2015.
 */
public class LearnGamePanel extends GamePanel {

    public LearnGamePanel(){
        super(new LearnGame());
    }
    public LearnGamePanel(String qValueFile){
        super(new LearnGame(qValueFile));
    }
}
