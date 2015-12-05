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
    boolean requestedAction = false;

    final int REMIND_OF_SAVE_IN_TICKS = 1000;
    int checkSaveTicker = REMIND_OF_SAVE_IN_TICKS;

    public LearnGame(){
        log = new QLearning();
    }
    public LearnGame(String qValueFile){
        log = new QLearning(qValueFile);
    }

    @Override
    public void update() {
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

            //prestate
            actionTaken = bird.jumpDelay==10; //jumpDelay is set to 10 if jumped bird has jumped this tick :)
            prestate = logState();

            bird.update();

            movePipes();
            checkForCollisions();

            //after state

            if(bird.jumpDelay==0 && !justReset){
                log.logExperience(new Experience(prestate, actionTaken,bird.dead?-1000:1, logState()));
                if(log.getAction()) requestForJump();

            }


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


}
