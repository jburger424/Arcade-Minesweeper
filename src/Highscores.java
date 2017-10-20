import java.io.*;
import java.util.Scanner;

/**
 * Created by Jon on 5/1/15.
 */
public class Highscores {

  protected String dataFile = "highScores.txt";
  protected int numScores = 3;
  protected int[] highScores = new int[numScores];
  protected String[] initialArray = new String[numScores];

  public Highscores() {
    read();
  }

  public void read() {
    try {
      Scanner input = new Scanner(new FileInputStream(dataFile));
      for (int i = 0; i < numScores; i++) {
        String[] splitLine = input.nextLine().split(":");
        String initials = splitLine[0].toString();
        int score = Integer.parseInt(splitLine[1]);
        initialArray[i] = initials;
        highScores[i] = score;
      }
    } catch (FileNotFoundException e) {
    }

  }

  public boolean isHighscore(int time) {
    //if lower than last highscore
    if (time < highScores[numScores - 1]) {
      return true;
    } else {
      return false;
    }
  }

  public void addHighscore(String userInitials, int time) {
    if (time < highScores[0]) {
      highScores[2] = highScores[1];
      initialArray[2] = initialArray[1];
      highScores[1] = highScores[0];
      initialArray[1] = initialArray[0];
      highScores[0] = time;
      initialArray[0] = userInitials;
    } else if (time < highScores[1]) {
      highScores[2] = highScores[1];
      initialArray[2] = initialArray[1];
      highScores[1] = time;
      initialArray[1] = userInitials;
    } else if (time < highScores[2]) {
      highScores[2] = time;
      initialArray[2] = userInitials;
    }
    writeData();

  }

  public void writeData() {
    try {
      File output = new File(this.dataFile);
      FileWriter writer = new FileWriter(output);
      PrintWriter pWriter = new PrintWriter(writer);
      for (int i = 0; i < numScores; i++) {
        String line = initialArray[i] + ":" + highScores[i];
        pWriter.println(line);
      }
      pWriter.close();
    } catch (java.io.IOException e) {

    }

  }

}
