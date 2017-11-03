import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * Created by Jon on 4/29/15.
 */
public class Screen {
  int currentTime = -1;
  private int screenSize;
  private double xLoc, yLoc;
  private Stopwatch timer;
  private String lastText = "";
  private String currentText = "";
  private String fullText = "";
  private boolean scrollingText = false;
  private int scrollingStep = 0;
  private boolean timerRunning = false;
  private boolean showTime = false;
  private char[] lastScreen;
  private double digitHeight;
  private double digitWidth;

  public Screen(int screenSize, double xLoc, double yLoc, double digitHeight) {
    this.xLoc = xLoc;
    this.yLoc = yLoc;
    this.digitHeight = digitHeight;
    this.digitWidth = (digitHeight * 193) / 321;
    this.screenSize = screenSize;
    this.lastScreen = addSpacing("", screenSize).replace(" ", "-").toCharArray();
    print(addSpacing("", screenSize));

  }

  public void update() {
    int tempTime = -1;
    if (timerRunning) {
      tempTime = timer.getWholeSeconds();
    }
    if (showTime && timerRunning && currentTime != tempTime && !scrollingText) {
      currentTime = tempTime;
      String strTime = Integer.toString(currentTime);
      print(strTime);
    }
    if (scrollingText) {
      lastText = currentText;
      scrollText();
      if (!lastText.equals(currentText)) {
        print(currentText);
      }

    }
  }

  public void restartTimer() {
    timer = new Stopwatch();
    timerRunning = true;
    currentTime = -1;
  }

  public void stopTimer() {
    timerRunning = false;
  }

  public void resetScreen(boolean doShowTime) {
    String tempString = "";
    for (int i = 0; i < screenSize; i++) {
      tempString += " ";
    }
    print(tempString);

    restartTimer();

    showTime = doShowTime;

    currentTime = -1;
    currentText = "";
    lastText = "";
    fullText = "";
    scrollingText = false;
    scrollingStep = 0;

  }

  private String addSpacing(String string, int targetLength) {
    int strLen = string.length();
    int neededSpaces = targetLength - strLen;
    for (int i = 0; i < neededSpaces; i++) {
      string = " " + string;
    }
    return string;
  }


  private void scrollText() {
    double currentTime = timer.getMils();
    int speed = 200;
    if (currentTime % speed == 0) {
      scrollingStep += 1;
      int strLen = fullText.length();
      int steps = strLen - screenSize + 1;
      if (scrollingStep >= steps) {
        scrollingStep = 0;
      }
      int currentStep = scrollingStep;
      currentText = fullText.substring(currentStep, currentStep + screenSize);
    }
  }

  public void print(String string) {
    int numChars = string.length();
    if (numChars < screenSize) {
      string = addSpacing(string, screenSize);
      try {
        print(string);
      } catch (StackOverflowError e) {

      }

    } else if (numChars > screenSize) {
      scrollingText = true;
      String tempBlankString = "";
      for (int i = 0; i < screenSize; i++) {
        tempBlankString += " ";
      }
      string = tempBlankString + string + tempBlankString;
      fullText = string;
      restartTimer();
      update();
    } else {
      char[] charArray = string.toCharArray();
      for (int i = 0; i < numChars; i++) {
        char tempChar = charArray[i];
        if (tempChar != lastScreen[i]) {
          String imageName = "images/chars/" + tempChar + ".jpg";
          if(!new File(imageName).isFile()){
            imageName = "images/chars/ .jpg";
          }
          StdDraw
              .picture(xLoc + digitWidth / 2 + digitWidth * i, yLoc - (digitHeight / 2), imageName,
                  digitWidth, digitHeight);
        }


      }
      lastScreen = charArray;
    }


  }
}
