/**
 * Created by Jon on 4/30/15.
 */
public class Header{
    protected double gameWidth;
    protected double gameHeight;
    protected double fullHeight;
    protected Button[] buttons = new Button[3];
    protected Screen[] screens = new Screen[9];
    protected Highscores highscores = new Highscores();
    protected int[] highScores = highscores.highScores;
    protected String[] highScoreInitials = highscores.initialArray;
    protected Logo logo;
    protected boolean drawBackground;
    protected int numFlags;

    public Header(double gameWidth, double gameHeight, double fullHeight, boolean drawBackground){
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.fullHeight = fullHeight;
        this.drawBackground = drawBackground;
        initialize();
    }

    public void initialize(){
        if(drawBackground) StdDraw.picture(gameWidth/2,fullHeight-(fullHeight-gameHeight)/2,"images/template.png",gameWidth,fullHeight-gameHeight);
        buttons[0] = new Button("0","green",(gameWidth*1.29)/10,(fullHeight*11.36)/13.74545,(gameWidth*.5)/10);//maybe should be full height
        buttons[1] = new Button("1","yellow",(gameWidth*1.29)/10,(fullHeight*10.86)/13.74545,(gameWidth*.5)/10);
        buttons[2] = new Button("2","red",(gameWidth*1.29)/10,(fullHeight*10.36)/13.74545,(gameWidth*.5)/10);

        screens[0] = new Screen("main",12,gameWidth,fullHeight,gameWidth,gameHeight,(gameWidth*2.5)/10,(fullHeight*12.3)/13.74545,(gameWidth*.7)/10);
        screens[1] = new Screen("time",4,gameWidth,fullHeight,gameWidth,gameHeight,(gameWidth*5.88)/10,(fullHeight*11.55)/13.74545,(gameWidth*.7)/10);
        screens[2] = new Screen("flags",4,gameWidth,fullHeight,gameWidth,gameHeight,(gameWidth*5.88)/10,(fullHeight*10.8)/13.74545,(gameWidth*.7)/10);
        screens[3] = new Screen("init1",2,gameWidth,fullHeight,gameWidth,gameHeight,(gameWidth*7.957)/10,(fullHeight*11.55)/13.74545,(gameWidth*.45)/10);
        screens[4] = new Screen("init2",2,gameWidth,fullHeight,gameWidth,gameHeight,(gameWidth*7.957)/10,(fullHeight*11.05)/13.74545,(gameWidth*.45)/10);
        screens[5] = new Screen("init3",2,gameWidth,fullHeight,gameWidth,gameHeight,(gameWidth*7.957)/10,(fullHeight*10.55)/13.74545,(gameWidth*.45)/10);
        screens[6] = new Screen("score1",4,gameWidth,fullHeight,gameWidth,gameHeight,(gameWidth*8.64)/10,(fullHeight*11.55)/13.74545,(gameWidth*.45)/10);
        screens[7] = new Screen("score2",4,gameWidth,fullHeight,gameWidth,gameHeight,(gameWidth*8.64)/10,(fullHeight*11.05)/13.74545,(gameWidth*.45)/10);
        screens[8] = new Screen("score3",4,gameWidth,fullHeight,gameWidth,gameHeight,(gameWidth*8.64)/10,(fullHeight*10.55)/13.74545,(gameWidth*.45)/10);
        logo = new Logo(gameWidth/2,(fullHeight*13)/13.74545,gameWidth*.85,(fullHeight-gameHeight)*.313,200);
        logo.draw();
        updateHighScores();
    }

    public void update(){
        logo.update();
        for (int i = 0; i < buttons.length; i++) {
            Button tempButton = buttons[i];
            tempButton.update();
        }
        for (int i = 0; i < screens.length; i++) {
            Screen tempScreen = screens[i];
            tempScreen.update();
        }

    }

    public void updateHighScores(){
        highscores.read();
        highScores = highscores.highScores;
        highScoreInitials = highscores.initialArray;
        for (int i = 0; i < highScores.length; i++) {
            String tempScore = Integer.toString(highScores[i]);
            screens[6+i].print(tempScore);
            screens[3+i].print(highScoreInitials[i]);
        }
    }

    public int pauseTimerScreenGetCurrentTime(){
        screens[1].stopTimer();
        return screens[1].currentTime;
    }

    public void writeMainMessage(String string){
        clearMainMessage();
        //System.out.println(string);
        screens[0].print(string);
        screens[0].update();
    }
    public void clearMainMessage(){
        screens[0] = new Screen("main",12,gameWidth,fullHeight,gameWidth,gameHeight,(gameWidth*2.5)/10,(fullHeight*12.3)/13.74545,(gameWidth*.7)/10);
    }


    public void startGame(){

        screens[1].resetScreen(true);
        screens[1].print("0");
    }
    public void updateFlags(int numFlags){
        String strNumFlags = Integer.toString(numFlags);
        screens[2].print(strNumFlags);
    }

    public int checkForButtonPress(double x, double y){
        for (int i = 0; i < buttons.length; i++) {
            Button tempButton = buttons[i];
            if(tempButton.checkForClick(x,y)){
                tempButton.isClicked = true;
                tempButton.draw();
                String tempName = tempButton.name;
                int level = Integer.parseInt(tempName);
                //System.out.println(tempButton.name+" was clicked");
                return level;
            }

        }
        return -1;
    }


}
