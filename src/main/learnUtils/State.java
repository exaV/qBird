package main.learnUtils;

/**
 * Created by P on 03.12.2015.
 */
public class State {

    public State(int hdist,int vdist,boolean alive){
        this.hdist = hdist;
        this.vdist = vdist;
        this.alive = alive;
    }

    //horizontal distance to posts pair of pipes
    int hdist;
    //vertical distance to posts lower pipe
    int vdist;
    //true if alive
    boolean alive;
}
