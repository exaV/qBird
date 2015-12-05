package main.game;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Game {

    public static final int PIPE_DELAY = 100;

    protected Boolean paused;

    protected int pauseDelay;
    protected int restartDelay;
    protected int pipeDelay;

    protected Bird bird;
    protected ArrayList<Pipe> pipes;
    protected Keyboard keyboard;

    public int score;
    public int oldscore;
    public Boolean gameover;
    public Boolean started;

    protected boolean requestStart = false;
    protected boolean requestRestart = false;
    protected boolean justReset = false;


    public Game() {
        keyboard = Keyboard.getInstance();
        restart();

    }

    public void restart() {
        justReset = true;
        paused = false;
        started = false;
        gameover = false;


        score = 0;
        pauseDelay = 0;
        restartDelay = 0;
        pipeDelay = 0;

        bird = new Bird();
        pipes = new ArrayList<Pipe>();
    }

    public void update() {
        watchForStart();

        if (!started)
            return;

        watchForPause();
        watchForReset();

        if (paused)
            return;

        bird.update();

        if (gameover)
            return;

        movePipes();
        checkForCollisions();


    }

    public ArrayList<Render> getRenders() {
        ArrayList<Render> renders = new ArrayList<Render>();
        renders.add(new Render(0, 0, "lib/background.png"));
        for (Pipe pipe : pipes)
            renders.add(pipe.getRender());
        renders.add(new Render(0, 0, "lib/foreground.png"));
        renders.add(bird.getRender());
        return renders;
    }

    protected void watchForStart() {
        if (!started && (requestStart || keyboard.isDown(KeyEvent.VK_SPACE))) {
            started = true;
            requestStart = false;
        }
    }

    protected void watchForPause() {
        if (pauseDelay > 0)
            pauseDelay--;

        if (keyboard.isDown(KeyEvent.VK_P) && pauseDelay <= 0) {
            paused = !paused;
            pauseDelay = 10;
        }
    }

    protected void watchForReset() {
        if (restartDelay > 0)
            restartDelay--;

        if ((requestRestart || keyboard.isDown(KeyEvent.VK_R)) && restartDelay <= 0) {
            restart();
            restartDelay = 10;
            requestRestart = false;
            return;
        }
    }

    protected void movePipes() {
        pipeDelay--;

        if (pipeDelay < 0) {
            pipeDelay = PIPE_DELAY;
            Pipe northPipe = null;
            Pipe southPipe = null;

            // Look for pipes off the screen
            for (Pipe pipe : pipes) {
                if (pipe.x - pipe.width < 0) {
                    if (northPipe == null) {
                        northPipe = pipe;
                    } else if (southPipe == null) {
                        southPipe = pipe;
                        break;
                    }
                }
            }

            if (northPipe == null) {
                Pipe pipe = new Pipe("north");
                pipes.add(pipe);
                northPipe = pipe;
            } else {
                northPipe.reset();
            }

            if (southPipe == null) {
                Pipe pipe = new Pipe("south");
                pipes.add(pipe);
                southPipe = pipe;
            } else {
                southPipe.reset();
            }

            northPipe.y = southPipe.y + southPipe.height + 175;
        }

        for (Pipe pipe : pipes) {
            pipe.update();
        }
    }

    protected void checkForCollisions() {

        for (Pipe pipe : pipes) {
            if (pipe.collides(bird.x, bird.y, bird.width, bird.height)) {
                gameover = true;
                bird.dead = true;
            } else if (pipe.x == bird.x && pipe.orientation.equalsIgnoreCase("south")) {
                score++;
            }
        }

        // Ground + Bird collision
        if (bird.y + bird.height > App.HEIGHT - 80) {
            gameover = true;
            bird.dead = true;
            bird.y = App.HEIGHT - 80 - bird.height;
        }

        // SkyLimit + Bird collision
        if(bird.y < 0){
            gameover = true;
            bird.dead = true;
            bird.y = 0;
        }
    }

    public ArrayList<Pipe> getPipes(){ return pipes;}
    public Bird getBird() {return bird;}
}
