/**
 * Created by Jon on 4/30/15.
 */
class Header {

  double gameWidth;
  double gameHeight;
  double fullHeight;
  Button[] buttons = new Button[3];
  Logo logo;
  private Screen[] screens = new Screen[9];
  private Highscores highscores = new Highscores();
  private Integer[] scoreArr = highscores.highScores;
  private String[] initialsArr = highscores.initialArray;
  private boolean drawBackground;
  private double buttonX;
  private double buttonRad;

  private double largeScreenHeight;
  private double smallScreenHeight;

  Header(double gameWidth, double gameHeight, double fullHeight, boolean drawBackground) {
    this.gameWidth = gameWidth;
    this.gameHeight = gameHeight;
    this.fullHeight = fullHeight;
    this.drawBackground = drawBackground;

    this.buttonX = (gameWidth * 1.29) / 10;
    this.buttonRad = (gameWidth * .5) / 10;
    this.largeScreenHeight = (gameWidth * .7) / 10;
    this.smallScreenHeight = (gameWidth * .45) / 10;
    initialize();
  }

  private void initialize() {
    if (drawBackground) {
      StdDraw
          .picture(gameWidth / 2, fullHeight - (fullHeight - gameHeight) / 2,
              "images/template.png",
              gameWidth, fullHeight - gameHeight);
    }
    buttons[0] = new Button("0", "green", buttonX, (fullHeight * 11.36) / 13.74545,
        buttonRad);
    buttons[1] = new Button("1", "yellow", buttonX, (fullHeight * 10.86) / 13.74545,
        buttonRad);
    buttons[2] = new Button("2", "red", buttonX, (fullHeight * 10.36) / 13.74545,
        buttonRad);

    //main
    screens[0] = new Screen( 12,
        (gameWidth * 2.5) / 10, (fullHeight * 12.3) / 13.74545, largeScreenHeight);
    //time
    screens[1] = new Screen(4,
        (gameWidth * 5.88) / 10, (fullHeight * 11.55) / 13.74545, largeScreenHeight);
    //flags
    screens[2] = new Screen(4,
        (gameWidth * 5.88) / 10, (fullHeight * 10.8) / 13.74545, largeScreenHeight);
    //initials1
    screens[3] = new Screen(2,
        (gameWidth * 7.957) / 10, (fullHeight * 11.55) / 13.74545, smallScreenHeight);
    //initials2
    screens[4] = new Screen(2,
        (gameWidth * 7.957) / 10, (fullHeight * 11.05) / 13.74545, smallScreenHeight);
    //initials3
    screens[5] = new Screen(2,
        (gameWidth * 7.957) / 10, (fullHeight * 10.55) / 13.74545, smallScreenHeight);
    //score1
    screens[6] = new Screen(4,
        (gameWidth * 8.64) / 10, (fullHeight * 11.55) / 13.74545, smallScreenHeight);
    //score2
    screens[7] = new Screen( 4,
        (gameWidth * 8.64) / 10, (fullHeight * 11.05) / 13.74545, smallScreenHeight);
    //score3
    screens[8] = new Screen( 4,
        (gameWidth * 8.64) / 10, (fullHeight * 10.55) / 13.74545, smallScreenHeight);
    logo = new Logo(gameWidth / 2, (fullHeight * 13) / 13.74545, gameWidth * .85,
        (fullHeight - gameHeight) * .313, 200);
    logo.draw();
    updateHighScores();
  }

  void update() {
    logo.update();
    for (Button button : buttons) {
      button.update();
    }
    for (Screen screen : screens) {
      screen.update();
    }

  }

  void updateHighScores() {
    highscores.readScores();
    scoreArr = highscores.highScores;
    initialsArr = highscores.initialArray;
    for (int i = 0; i < scoreArr.length; i++) {
      String tempScore = Integer.toString(scoreArr[i]);
      screens[6 + i].print(tempScore);
      screens[3 + i].print(initialsArr[i]);
    }
  }

  int pauseTimerScreenGetCurrentTime() {
    screens[1].stopTimer();
    return screens[1].currentTime;
  }

  void writeMainMessage(String string) {
    clearMainMessage();
    screens[0].print(string);
    screens[0].update();
  }

  void clearMainMessage() {
    screens[0] = new Screen(12,
        (gameWidth * 2.5) / 10, (fullHeight * 12.3) / 13.74545, largeScreenHeight);
  }


  void startGame() {

    screens[1].resetScreen(true);
    screens[1].print("0");
  }

  void updateFlags(int numFlags) {
    String strNumFlags = Integer.toString(numFlags);
    screens[2].print(strNumFlags);
  }

  int checkForButtonPress(double x, double y) {
    for (Button button : buttons) {
      if (button.checkForClick(x, y)) {
        button.isClicked = true;
        button.draw();
        return Integer.parseInt(button.name);
      }

    }
    return -1;
  }


}
