/**
 * Created by Jon on 4/28/15.
 */
public class Stopwatch {
    protected double startTime;
    public Stopwatch(){
        startTime = System.currentTimeMillis();
    }
    public int getWholeSeconds(){
        int elapsedSecs = (int)((System.currentTimeMillis()-startTime)/1000);
        return elapsedSecs;
    }
    public double getMils(){
        return System.currentTimeMillis()-startTime;
    }

}
