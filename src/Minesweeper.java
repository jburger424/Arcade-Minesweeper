import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Jon on 4/21/15.
 */
public class Minesweeper {
    protected Grid grid;
    protected double fullHeight;
    protected int gameWidthPX = 470;
    protected int gameHeightPX = 470;
    protected double headerHeight = 176;
    protected boolean hasStarted = false;
    protected boolean gameOver = false;
    protected boolean didWin = false;
    protected double heightFactor = (gameHeightPX+headerHeight)/(double)gameHeightPX;
    protected Header header;
    protected int width = 10;
    protected int height = 10;
    protected boolean gameEnded = false;
    protected int remainingFlags = 10;
    protected Highscores highscores = new Highscores();
    protected int endTime = -1;

    public Minesweeper(){
        //System.out.println("instance created");
        //draw grid and header
        this.fullHeight = ((double)height*heightFactor);
        initializeWindow();
        run();
    }
    public void update(){
        fullHeight = ((double)height*heightFactor);
        didWin = grid.didWin;
        hasStarted = grid.hasStarted;
        gameOver = grid.gameOver;

        if(hasStarted && !gameOver){
            header.logo.changeFlashingSpeed(-1);
            header.clearMainMessage();
        }
        if(gameOver && hasStarted && !gameEnded){
            gameOver();
            gameEnded = true;
        }
        if(grid.numBombs-grid.numFlags != remainingFlags){
            remainingFlags = grid.numBombs - grid.numFlags;
            header.updateFlags(remainingFlags);
        }

        if(grid.startTime){
            header.startGame();
            grid.startTime = false;
        }


        header.update();
    }
    public void initializeWindow(){
        //System.out.println("initializing");
        StdDraw.setCanvasSize(gameWidthPX, (int)(gameHeightPX*heightFactor));
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, fullHeight);
        header = new Header(width,height,fullHeight,true);
        grid = new Grid(width,height);
        header.writeMainMessage("Choose level to begin");
    }
    //0 easy 1 med 2 hard
    public void startGame(int level){
        header.clearMainMessage();
        endTime = -1;
        int numBombs;
        if(level == 0){
            width = height = 9;
            numBombs = 10;
        }
        else if(level == 1){
            width = height = 16;
            numBombs = 40;
        }
        else{
            width = height = 22;
            numBombs = 99;
        }

        header.gameWidth = width;
        header.gameHeight = height;
        fullHeight = ((double)height*heightFactor);
        header.fullHeight = fullHeight;
        StdDraw.setXscale(0,width);
        StdDraw.setYscale(0,fullHeight);
        grid = new Grid(width,height);
        header = new Header(width,height,fullHeight,false);
        header.updateFlags(numBombs);
        //header.initialize();
        grid.numBombs = numBombs;
        grid.initializeGrid();
        header.writeMainMessage("Click any cell to begin");
        gameOver = false;
        gameEnded = false;

    }

    public void doWinGame(){
        if(highscores.isHighscore(endTime)){
            String timeStr = Integer.toString(endTime);
            header.writeMainMessage("You Win with a high score of "+timeStr+"   choose level to restart");
            highscores.addHighscore("dd", endTime);
            header.updateHighScores();
        }
        else{
            header.writeMainMessage("You Win   choose level to restart");
        }
    }

    public boolean onGrid(double x, double y){
        if(x >= 0 && x < width && y >= 0 && y < height) return true;
        else return false;
    }
    public boolean nearButtons(double x, double y){
        double leftBound = header.buttons[0].x - header.buttons[0].radius;
        double rightBound = header.buttons[2].x + header.buttons[2].radius;
        double upperBound = header.buttons[0].y + header.buttons[0].radius;
        double lowerBound = header.buttons[2].y - header.buttons[2].radius;
        if(x > leftBound && x < rightBound && y > lowerBound && y < upperBound) return true;
        else return false;
    }
    public void gameOver(){
        header.logo.changeFlashingSpeed(100);
        endTime = header.pauseTimerScreenGetCurrentTime();
        if(grid.didWin()){
            doWinGame();
        }
        else if(!gameEnded){
            header.writeMainMessage("game over you lose   choose level to restart");
        }
    }

    public void run() {
        boolean rightDown = false;
        boolean leftDown = false;
        while (true) {
            update(); // add
            if (StdDraw.leftPressed() && leftDown == false) {
                //starting game
                leftDown = true;
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                //
                // System.out.println(x+" "+y);

                if(nearButtons(x, y) && header.checkForButtonPress(x,y) != -1){
                    startGame(header.checkForButtonPress(x,y));
                }
                else if (onGrid(x,y)) grid.leftClick(x,y);
            }
            else if (StdDraw.leftPressed() == false) {
                leftDown = false;
            }
            if(StdDraw.rightPressed() && rightDown == false && !gameOver){
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                rightDown = true;
                if(onGrid(x,y)) grid.rightClick(x,y);

            }

            else if (StdDraw.rightPressed() == false) {
                rightDown = false;
            }

        }
    }
}
