import java.awt.*;

/**
 * Created by Jon on 4/29/15.
 */
public class Screen {
    protected String name;
    protected int screenSize;
    protected double windowWidth, windowHeight, gameWidth, gameHeight,screenHeight,xLoc,yLoc;
    protected Stopwatch timer;
    protected int currentTime = -1;
    protected String lastText = "";
    protected String currentText = "";
    protected String fullText = "";
    protected boolean scrollingText = false;
    protected int scrollingStep = 0;
    protected boolean timerRunning = false;
    protected boolean showTime = false;
    protected char[] lastScreen;
    protected  double digitHeight;
    protected  double digitWidth;

    public Screen(String name, int screenSize, double windowWidth, double windowHeight, double gameWidth, double gameHeight,double xLoc, double yLoc,double digitHeight){
        this.name = name;
        this.xLoc = xLoc;
        this.yLoc = yLoc;
        this.digitHeight = digitHeight;
        this.digitWidth = (digitHeight*193)/321;
        this.screenSize = screenSize;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.screenHeight = windowHeight-gameHeight;
        this.lastScreen = addSpacing("",screenSize).replace(" ","-").toCharArray();
        print(addSpacing("",screenSize));

    }

    public void update(){
        int tempTime = -1;
        if(timerRunning){
            tempTime = timer.getWholeSeconds();
        }
        if (showTime && timerRunning && currentTime != tempTime && !scrollingText){
            currentTime = tempTime;
            String strTime = Integer.toString(currentTime);
            print(strTime);
        }
        if (scrollingText){
            lastText = currentText;
            scrollText();
            if(!lastText.equals(currentText)){
                print(currentText);
            }

        }
    }

    public void restartTimer(){
        timer = new Stopwatch();
        timerRunning = true;
        currentTime = -1;
    }

    public void stopTimer(){
        timerRunning = false;
    }

    public void resetScreen(boolean doShowTime){
        String tempString = "";
        for (int i = 0; i < screenSize; i++) {
            tempString += " ";
        }
        print(tempString);

        restartTimer();

        if(doShowTime){
            showTime = true;
        }
        else{
            showTime = false;
        }

        currentTime = -1;
        currentText = "";
        lastText = "";
        fullText = "";
        scrollingText = false;
        scrollingStep = 0;

    }

    private String addSpacing(String string, int targetLength){
        int strLen = string.length();
        int neededSpaces = targetLength - strLen;
        for (int i = 0; i < neededSpaces; i++) {
            string = " "+string;
        }
        return string;
    }



    private void scrollText(){
        double currentTime = timer.getMils();
        int speed = 200;
        if(currentTime%speed == 0){
            scrollingStep+=1;
            int strLen = fullText.length();
            int steps = strLen-screenSize+1;
            if(scrollingStep >= steps) scrollingStep=0;
            int currentStep = scrollingStep;
            currentText = fullText.substring(currentStep,currentStep+screenSize);
        }
    }

    public void print(String string){
        int numChars = string.length();
        if(numChars < screenSize){
            string = addSpacing(string,screenSize);
            try {
                print(string);
            }
            catch (StackOverflowError e){

            }

        }
        else if (numChars > screenSize){
            scrollingText = true;
            String tempBlankString = "";
            for (int i = 0; i < screenSize; i++) {
                tempBlankString += " ";
            }
            string = tempBlankString+string+tempBlankString;
            fullText = string;
            restartTimer();
            update();
        }
        else{
            char[] charArray = string.toCharArray();
            for (int i = 0; i < numChars; i++) {
                char tempChar = charArray[i];
                if(tempChar != lastScreen[i]){
                    String imageName = "images/chars/"+tempChar+".jpg";
                    StdDraw.picture(xLoc+digitWidth/2+digitWidth*i,yLoc-(digitHeight/2),imageName,digitWidth,digitHeight);
                }


            }
            lastScreen = charArray;
        }


    }
}
