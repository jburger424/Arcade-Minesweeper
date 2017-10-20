import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Created by Jon on 4/28/15.
 */
public class Grid{
    protected boolean didWin = false;
    protected int width, height;
    protected double fullHeight;
    protected int gameWidthPX = 550;
    protected int gameHeightPX = 550;
    protected double headerHeight = 206;
    protected Cell[][] cells;
    protected double gameWHRatio;
    protected int sysWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    protected int sysHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    protected double sysWHRatio = (double) sysWidth / (double) sysHeight;
    protected boolean hasStarted = false;
    protected boolean gameOver = false;
    protected double heightFactor = ((gameHeightPX+headerHeight)/(double)gameHeightPX);
    protected int numBombs = 10;
    protected int numFlags = 0;
    protected boolean startTime = false;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.fullHeight = ((double) height * heightFactor);
        this.cells = new Cell[width][height];
        this.gameWHRatio = ((double) width / (double) height);
    }

    public void initializeGrid() {
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(); // doesn't have bomb
            }
        }
        draw();
    }

    public void leftClick(double x, double y){
        if(!gameOver){
            int intX = (int)x;
            int intY = (int)y;
            if(!hasStarted) startGame(intX,intY);
            if(cells[intX][intY].hasBomb) gameOver(intX,intY);
            cascade(intX,intY);
            draw();
        }

    }

    public void rightClick(double x, double y){
        int intX = (int) StdDraw.mouseX();
        int intY = (int) StdDraw.mouseY();
        Cell tempCell = cells[intX][intY];
        tempCell.toggleFlag();
        if(tempCell.hasFlag) numFlags+= 1;
        else numFlags -=1;
        //
        // System.out.println(numFlags);
        draw();
    }
    public boolean didWin(){
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell tempCell = cells[x][y];
                if(!tempCell.hasBomb && !tempCell.isClicked){
                    return false;
                }
            }

        }
        gameOver = true;
        didWin = true;
        return true;
    }
    //x and y where bomb killed
    public void gameOver(int x, int y){
        gameOver = true;
        cells[x][y].isLooser = true;
        //flips all bombs
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if(cells[i][j].hasBomb) cells[i][j].wasClicked();
                if(cells[i][j].hasFlag) cells[i][j].wasClicked();
            }
        }
        draw();
    }

    public void draw(){
        if(didWin()){
            didWin = true;
        }
        else didWin = false;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell tempCell = cells[x][y];
                if(!tempCell.isDrawn){
                    if(tempCell.isLooser){
                        StdDraw.picture(x + .5, y + .5, "images/looser.jpg", 1, 1);
                    }
                    else if(gameOver && tempCell.hasFlag && !tempCell.hasBomb){
                        StdDraw.picture(x + .5, y + .5, "images/noBomb.jpg", 1, 1);
                        tempCell.isDrawn = true;
                    }
                    else if (tempCell.hasFlag){
                        StdDraw.picture(x + .5, y + .5, "images/flag.jpg", 1, 1);
                        tempCell.isDrawn = true;
                    }
                    else if (tempCell.isClicked && !tempCell.hasBomb){
                        String imageName = "images/"+tempCell.surroundingBombs+".jpg";
                        //System.out.println(cells[x][y].surroundingBombs);
                        StdDraw.picture(x + .5, y + .5, imageName, 1, 1);
                        tempCell.isDrawn = true;
                    }
                    else if(tempCell.isClicked && tempCell.hasBomb){
                        //game over
                        StdDraw.picture(x + .5, y + .5, "images/bomb.jpg", 1, 1);
                        tempCell.isDrawn = true;
                        gameOver = true;
                    }

                    else{
                        StdDraw.picture(x + .5, y + .5, "images/unclicked.jpg", 1, 1);
                        tempCell.isDrawn = true;
                    }

                }

            }

        }
    }

    public void getSurroundingValues(){
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int dX = -1; dX < 2; dX++) {
                    for (int dY = -1; dY < 2; dY++) {
                        if((dX != 0 || dY != 0) && x+dX >= 0 && x+dX < width && y+dY >= 0 && y+dY < height && cells[x+dX][y+dY].hasBomb){
                            cells[x][y].surroundingBombs += 1;
                        }

                    }
                }

            }

        }
    }

    public void makeBombs(int startX, int startY){
        for (int i = 0; i < numBombs; i++) {
            boolean plantedBomb = false;
            while (!plantedBomb){
                int randX = (int)(Math.random()*width);
                int randY = (int)(Math.random()*height);
                boolean nextToClick = false;
                if(Math.abs(randX-startX) <= 1 && Math.abs(randY-startY) <= 1){
                    nextToClick = true;
                }
                if(!(randX == startX && randY == startY) && !cells[randX][randY].hasBomb && !nextToClick){
                    plantedBomb = true;
                    cells[randX][randY].addBomb();
                }
            }
        }
    }


    public void resetGame(){
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y].reset();
            }

        }
        hasStarted = false;
        gameOver = false;
        didWin = false;
        draw();

    }
    public void startGame(int x, int y){
        makeBombs(x,y);
        getSurroundingValues();
        cascade(x, y);
        hasStarted = true;
        startTime = true;
    }

    public boolean isInBounds(int x, int y){
        if(x >= 0 && x < width && y >= 0 && y < height) return true;
        else return false;
    }

    public void cascade(int x, int y){
        if(isInBounds(x,y) && !cells[x][y].isClicked){
            cells[x][y].wasClicked();
            Cell tempCell = cells[x][y];
            if(tempCell.surroundingBombs == 0){
                //System.out.println(x+" "+y);
                for (int dX = -1; dX < 2; dX++) {
                    for (int dY = -1; dY < 2; dY++) {
                        if(dX != 0 || dY != 0){
                            if(isInBounds(x+dX,y+dY)){
                                cascade(x+dX,y+dY);
                            }

                        }

                    }

                }
            }
        }
    }



}


