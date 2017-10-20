/**
 * Created by Jon on 4/28/15.
 */
public class Cell {
    protected boolean hasBomb = false;
    protected boolean hasFlag = false;
    protected boolean isClicked = false;
    protected boolean isDrawn = false;
    protected boolean isLooser = false;
    protected int surroundingBombs = 0;

    //asCupHolders occurs with 1/2 probability, has4WD (four wheel drive) with 2/3 probability, and towCapacity should be a random number between 2 and 8

    public Cell(){
    }
    public void addBomb(){
        isDrawn = false;
        hasBomb = true;
    }
    public void toggleFlag(){
        isDrawn = false;
        if(hasFlag) hasFlag = false;
        else hasFlag = true;
    }

    public void wasClicked(){
        isDrawn = false;
        isClicked = true;
    }
    public void reset(){
        hasBomb = false;
        hasFlag = false;
        isClicked = false;
        isLooser = false;
        surroundingBombs = 0;
        isDrawn = false;
    }
}
