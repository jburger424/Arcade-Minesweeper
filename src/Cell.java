/**
 * Created by Jon on 4/28/15.
 */
class Cell {

  boolean hasBomb = false;
  boolean hasFlag = false;
  boolean isClicked = false;
  boolean isDrawn = false;
  boolean isLooser = false;
  int surroundingBombs = 0;

  void addBomb() {
    isDrawn = false;
    hasBomb = true;
  }

  void toggleFlag() {
    isDrawn = false;
    hasFlag = !hasFlag;
  }

  void wasClicked() {
    isDrawn = false;
    isClicked = true;
  }
}
