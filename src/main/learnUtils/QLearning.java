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

    public QLearning(){

    }

    public QLearning(String qValuesFile){
        readQfromFile(qValuesFile);

    }

    public void logExperience(Experience e) {
        s = e.s;
        post_s = e.posts;
        reward = e.reward;
        action = e.action;

        updateQ();
    }
    public boolean getAction() {
        return Q[post_s.hdist][post_s.vdist][JUMP] > Q[post_s.hdist][post_s.vdist][NOJUMP]; //returns true only if confidence to jump is higher
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

        int learnedValueOfLastAction = Q[s.hdist][s.vdist][action?JUMP:NOJUMP];

        int estimatedValueOfFutureAction = Math.max( Q[post_s.hdist][post_s.vdist][JUMP], Q[post_s.hdist][post_s.vdist][NOJUMP]);
        int v_posts_posta = (int)(alpha * (reward + estimatedValueOfFutureAction - learnedValueOfLastAction ));

        int calculatedValue = learnedValueOfLastAction + v_posts_posta==0?1:v_posts_posta;

        //update Q of prestate with the value of the taken action
        Q[s.hdist][s.vdist][action?JUMP:NOJUMP] += calculatedValue;

        //s = post_s; is implicitly done in learngame. The next pre state that is logged is actually the old post state
    }
    private boolean saveQtoDisk() {
        BufferedWriter outputWriter;
        try {
            outputWriter = new BufferedWriter(
                    new FileWriter("q_knowledge/Q_save" + LocalDate.now().toString().replace("-","_")+"__"+LocalTime.now().toString().replace(":", "_").replace(".", "_")+"__"+highestScore+ ".dat"));

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
        Scanner s;
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
