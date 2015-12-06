package main.learnUtils;

import main.game.*;

import java.awt.event.KeyEvent;

/**
 * Created by P on 29.11.2015.
 *
 * important for logging capabilities
 */
public class LearnGame extends Game {
    QLearning log;

    State prestate;
    State postState;

    boolean actionTaken;
    boolean requestedAction = true;

    final int REMIND_OF_SAVE_IN_TICKS = 1000;
    int checkSaveTicker = REMIND_OF_SAVE_IN_TICKS;

    public LearnGame(){
        super();
        prestate = logState();
        //1.
        log = new QLearning(prestate);

    }
    public LearnGame(String qValueFile){
        super();
        prestate = logState();
        log = new QLearning(prestate,qValueFile);
    }

    @Override
    public void update() {
        actionTaken = false;
        if(keyboard.isDown(KeyEvent.VK_S)){
            log.requestSave();
        }

        watchForStart();
        requestForStart();

        if (started) {
            watchForPause();

            if (paused){
                return;
            }

            if (gameover){
                requestForRestart();
            }

            //3. (note: jumping may fail if bird.jumpDelay isn't 0
            if(log.getAction() && bird.jumpDelay ==0){
                requestForJump();
                actionTaken = true;
            }

            bird.update();

            movePipes();
            checkForCollisions();

            log.logExperience(logState(),actionTaken,bird.dead?-10000:1);

            justReset = false;

            if(score != oldscore){
                log.logScore(score);
                oldscore = score;
            }

            if(--checkSaveTicker <0){
                checkSaveTicker = REMIND_OF_SAVE_IN_TICKS;
                log.remindOfSaving();
            }
            watchForReset();

        }
    }

    public void requestForRestart(){
        requestRestart = true;
    }

    public void requestForStart(){
        requestStart = true;
    }

    public void requestForJump(){
        bird.requestJump = true;
    }

    private State logState(){

        int currentHorizontalDist = Integer.MAX_VALUE;
        int currentVerticalDist = Integer.MAX_VALUE;

        for (int i = 0; i < pipes.size(); i++) {
            Pipe p = pipes.get(i);

            //only check south pipes in front of player
            if(p.x+p.width >= bird.x && p.orientation.equals("north")){

                //check whether this pipe is the closest
                if(p.x - bird.x < currentHorizontalDist) {
                    currentHorizontalDist = p.x+p.width - bird.x;
                    currentVerticalDist = p.y - (bird.y+bird.height)+165;
                    if(currentVerticalDist<0){
                        currentVerticalDist = 0;
                        System.out.println("small Value!!!");
                    }
                    if(currentVerticalDist>539){
                        currentVerticalDist = 539;
                        System.out.println("large Value!!!");
                    }
                }
            }
        }

        return new State(currentHorizontalDist,currentVerticalDist,!bird.dead);

    }

    @Override
    public void restart() {
        super.restart();
        movePipes(); //otherwise pipes will be too far away and fuck up the learning
        prestate = logState();

    }
}
