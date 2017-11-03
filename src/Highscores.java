import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Jon on 5/1/15.
 */
class Highscores {

  private static final String SCORE_FILE = "highScores.txt";
  private static final int NUM_SCORES = 3;
  private int editCharIndex = -1;
  int editScoreIndex = -1;
  Integer[] highScores = new Integer[NUM_SCORES];
  String[] initialArray = new String[NUM_SCORES];

  Highscores() {
    readScores();
  }

  void readScores() {
    try {
      Scanner input = new Scanner(new FileInputStream(SCORE_FILE));
      for (int i = 0; i < NUM_SCORES; i++) {
        String[] splitLine = input.nextLine().split(":");
        String initials = splitLine[0];
        int score = Integer.parseInt(splitLine[1]);
        initialArray[i] = initials;
        highScores[i] = score;
      }
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }

  }

  boolean isHighscore(int time) {
    //if lower than last highscore
    return time < highScores[NUM_SCORES - 1];
  }

  void setLetter(char c){
    char[] initialsCharArr = initialArray[editScoreIndex].toCharArray();
    initialsCharArr[editCharIndex] = c;
    initialArray[editScoreIndex] = new String(initialsCharArr);
    writeData();
    if(editCharIndex > 0){
      editScoreIndex = -1;
      editCharIndex = -1;
    }
    else{
      editCharIndex++;
    }
  }

  void addHighscore(int time) {
    final List<String> initialsList = new ArrayList<String>(Arrays.asList(initialArray));
    final List<Integer> timesList = new ArrayList<Integer>(Arrays.asList(highScores));
    int index = -1;
    for(int i = 0; i < NUM_SCORES; i++){
      System.out.println(i);
      if(time <= highScores[i]){
        initialsList.add(i,"--");
        timesList.add(i,time);
        index = i;
        break;
      }
    }
    highScores = timesList.subList(0,3).toArray(new Integer[0]);
    initialArray = initialsList.subList(0,3).toArray(new String[0]);
    writeData();
    editScoreIndex = index;
    editCharIndex = 0;

  }

  private void writeData() {
    try {
      File output = new File(SCORE_FILE);
      FileWriter writer = new FileWriter(output);
      PrintWriter pWriter = new PrintWriter(writer);
      for (int i = 0; i < NUM_SCORES; i++) {
        String line = initialArray[i] + ":" + highScores[i];
        pWriter.println(line);
      }
      pWriter.close();
    }
    catch (java.io.IOException e) {
      e.printStackTrace();
    }

  }

}
