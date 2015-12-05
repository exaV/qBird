package main.learnUtils;

/**
 *
 * tuple (s,a,r,s')
 * Created by P on 03.12.2015.
 */
public class Experience {
    State s;
    boolean action;     //true if jumped
    int reward; //+1 if alive, -1000 if dead
    State posts;

    public Experience(State s,boolean a,int reward,State posts){
        this.s = s;
        this.action = a;
        this.reward = reward;
        this.posts = posts;
    }
}
