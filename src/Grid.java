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
public class Grid {

  private static final int CELL_WIDTH = 1;
  private static final int CELL_HEIGHT = 1;
  private static final String LOOSER_IMG = "images/looser.jpg";
  private static final String NO_BOMB_IMG = "images/noBomb.jpg";
  private static final String FLAG_IMG = "images/flag.jpg";
  private static final String BOMB_IMG = "images/bomb.jpg";
  private static final String UNCLICKED_IMG = "images/unclicked.jpg";

  private Cell[][] cells;
  boolean didWin = false;
  boolean hasStarted = false;
  boolean gameOver = false;
  int numBombs = 10;
  int numFlags = 0;
  boolean startTime = false;
  protected int width, height;

  Grid(int width, int height) {
    this.width = width;
    this.height = height;
    this.cells = new Cell[width][height];
  }

  void initializeGrid() {
    cells = new Cell[width][height];
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        cells[x][y] = new Cell(); // without bomb
      }
    }
    draw();
  }

  void leftClick(double x, double y) {
    if (!gameOver) {
      int intX = (int) x;
      int intY = (int) y;
      if (!hasStarted) {
        startGame(intX, intY);
      }
      if (cells[intX][intY].hasBomb) {
        gameOver(intX, intY);
      }
      cascade(intX, intY);
      draw();
    }

  }

  void rightClick(double x, double y) {
    Cell tempCell = cells[(int)x][(int)y];
    tempCell.toggleFlag();
    if (tempCell.hasFlag) {
      numFlags += 1;
    }
    else {
      numFlags -= 1;
    }
    draw();
  }

  boolean didWin() {
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        Cell tempCell = cells[x][y];
        if (!tempCell.hasBomb && !tempCell.isClicked) {
          return false;
        }
      }

    }
    gameOver = true;
    didWin = true;
    return true;
  }

  //x and y where bomb killed
  private void gameOver(int x, int y) {
    gameOver = true;
    cells[x][y].isLooser = true;
    //flips all bombs
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        if (cells[i][j].hasBomb) {
          cells[i][j].wasClicked();
        }
        if (cells[i][j].hasFlag) {
          cells[i][j].wasClicked();
        }
      }
    }
    draw();
  }

  private void draw() {
    didWin = didWin();

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        Cell tempCell = cells[x][y];
        double xPos = x + .5;
        double yPos = y + .5;
        String image = UNCLICKED_IMG;

        if (!tempCell.isDrawn) {
          if (tempCell.isLooser) {
            image = LOOSER_IMG;
          }
          else if (tempCell.hasFlag) {
            if (gameOver && !tempCell.hasBomb) {
              image = NO_BOMB_IMG;
            }
            else {
              image = FLAG_IMG;
            }
          }
          else if (tempCell.isClicked) {
            if (tempCell.hasBomb) {
              image = BOMB_IMG;
              gameOver = true;
            }
            else {
              image = "images/" + tempCell.surroundingBombs + ".jpg";
            }
          }
          StdDraw.picture(xPos, yPos, image, CELL_WIDTH, CELL_HEIGHT);
          tempCell.isDrawn = true;
        }

      }

    }
  }

  private void getSurroundingValues() {
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        for (int dX = -1; dX <= 1; dX++) {
          for (int dY = -1; dY <= 1; dY++) {
            if ((dX != 0 || dY != 0) && x + dX >= 0 && x + dX < width && y + dY >= 0
                && y + dY < height && cells[x + dX][y + dY].hasBomb) {
              cells[x][y].surroundingBombs += 1;
            }

          }
        }

      }

    }
  }

  private void makeBombs(int startX, int startY) {
    for (int i = 0; i < numBombs; i++) {
      boolean plantedBomb = false;
      while (!plantedBomb) {
        int randX = (int) (Math.random() * width);
        int randY = (int) (Math.random() * height);
        boolean nextToClick = false;
        if (Math.abs(randX - startX) <= 1 && Math.abs(randY - startY) <= 1) {
          nextToClick = true;
        }
        if (!(randX == startX && randY == startY) && !cells[randX][randY].hasBomb && !nextToClick) {
          plantedBomb = true;
          cells[randX][randY].addBomb();
        }
      }
    }
  }

  private void startGame(int x, int y) {
    makeBombs(x, y);
    getSurroundingValues();
    cascade(x, y);
    hasStarted = true;
    startTime = true;
  }

   private boolean isInBounds(int x, int y) {
    return (x >= 0 && x < width && y >= 0 && y < height);
  }

  private void cascade(int x, int y) {
    if (isInBounds(x, y) && !cells[x][y].isClicked) {
      cells[x][y].wasClicked();
      Cell tempCell = cells[x][y];
      if (tempCell.surroundingBombs == 0) {
        for (int dX = -1; dX < 2; dX++) {
          for (int dY = -1; dY < 2; dY++) {
            if (dX != 0 || dY != 0) {
              if (isInBounds(x + dX, y + dY)) {
                cascade(x + dX, y + dY);
              }
            }
          }
        }
      }
    }
  }


}


