package main.learnUtils;

import java.io.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

/**
 * Created by P on 29.11.2015.
 */
public class QLearning {
    //actions
    private final int JUMP = 0;
    private final int NOJUMP = 1;
    private final int maxHdiff = 500;
    private final int maxVdiff = 540;
    private final String savePrefix = "q_knowledge_fixed/Q_save";

    //learn parameters
    private final double exploreFactor = 0f;
    private final float alpha = 0.7f;

    // experience during one tick
    State s;
    int reward;
    boolean action;
    State post_s;     //aka s'

    private Instant lastSave = Instant.now();
    private int highestScore = 0;
    private final int goal = 100;
    private boolean hasReachedGoal = false;

    //debugging vars
    private int lastHdist;

    /**
     * stores knowledge
     * specifically the reward of taking action jump ([0]) or doing nothing [1]
     */
    int Q[][][] = new int[maxHdiff][maxVdiff][2];

    public QLearning(State prestate){
        this.s = prestate;
    }

    public QLearning(State prestate,String qValuesFile){
        readQfromFile(qValuesFile);
        this.s = prestate;


    }

    public void logExperience(State post_s, boolean actualAction, int reward) {
        this.post_s = post_s;
        this.action = actualAction;
        this.reward = reward;

        updateQ();
    }
    public boolean getAction() {
        action = Q[s.hdist][s.vdist][JUMP] > Q[s.hdist][s.vdist][NOJUMP];
        return action; //returns true only if confidence to jump is higher
    }
    public void logScore(int score) {
        if(score >highestScore){
            highestScore = score;
            if(score == goal){
                saveQtoDisk();
            }
        }
    }
    public void remindOfSaving(){
        if(lastSave.plusSeconds(30*60).compareTo(Instant.now())<0){
            if(saveQtoDisk()) lastSave = Instant.now(); //remember last save only if successful
        }
    }
    public boolean requestSave(){
        return saveQtoDisk(); //don't remember the save
    }
    private void updateQ(){
        /*
        update process works like this

        1. Q is constructed with initials state saved in s
        2. forever do
            3. game asks what action to take (is logged in field action)
            4. game is updated and state after update is logged in field post_s
            5. s is updated with the knowledge gained from taking action
            6. s becomes post_s
         */

        int learnedValueOfLastAction = Q[s.hdist][s.vdist][action?JUMP:NOJUMP];

        int estimatedValueOfFutureAction = Math.max( Q[post_s.hdist][post_s.vdist][JUMP], Q[post_s.hdist][post_s.vdist][NOJUMP]);
        int v_posts_posta = (int)(alpha * (reward + estimatedValueOfFutureAction - learnedValueOfLastAction ));

        int calculatedValue = learnedValueOfLastAction + v_posts_posta==0?1:v_posts_posta;

        //update Q of prestate with the value of the taken action
        Q[s.hdist][s.vdist][action?JUMP:NOJUMP] = calculatedValue;

        s = post_s;
    }
    private boolean saveQtoDisk() {
        BufferedWriter outputWriter = null;
        try {
            outputWriter = new BufferedWriter(
                    new FileWriter(savePrefix + LocalDate.now().toString().replace("-","_")+"__"+LocalTime.now().toString().replace(":", "_").replace(".", "_")+"__"+highestScore+ ".dat"));

            for (int i = 0; i < Q.length; i++) {
                for (int j = 0; j < Q[0].length; j++) {
                    outputWriter.write(Integer.toString(Q[i][j][JUMP]));
                    outputWriter.write(" ");
                    outputWriter.write(Integer.toString(Q[i][j][NOJUMP]));
                    outputWriter.newLine();
                }
            }
            outputWriter.flush();
            outputWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void randomInitQ(){
        for (int i = 0; i < Q.length; i++) {
            for (int j = 0; j < Q[0].length; j++) {
                Q[i][j][JUMP] = (int)(Math.random()*(6-1)+1);
                Q[i][j][NOJUMP] = (int)(Math.random()*(6-1)+1);
            }
        }
    }
    private void resetQ(){
        for (int i = 0; i < Q.length; i++) {
            for (int j = 0; j < Q[0].length; j++) {
                Q[i][j][JUMP] = 0;
                Q[i][j][NOJUMP] = 0;
            }
        }
    }
    private void readQfromFile(String filename) {
        Scanner s = null;
        try {
            s = new Scanner(new File(filename));
            for (int i = 0; i < Q.length; i++) {
                for (int j = 0; j < Q[0].length; j++) {
                    Q[i][j][JUMP] = s.nextInt();
                    Q[i][j][NOJUMP] = s.nextInt();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("couldn't read file");
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.err.println("corrupt file!");
        }
    }

}
