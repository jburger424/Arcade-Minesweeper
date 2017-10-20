/**
 * Created by Jon on 4/30/15.
 */
public class Logo {
    protected double x,y,logoWidth,logoHeight,frequency;
    protected boolean lightOn = false;
    protected  boolean flashing = true;

    public Logo(double x, double y, double logoWidth, double logoHeight, double frequency){
        this.x = x;
        this.y = y;
        this.logoWidth = logoWidth;
        this.logoHeight = logoHeight;
        this.frequency = frequency;
        draw();
    }

    public void update(){

        double currentTime = System.currentTimeMillis();
        if(flashing && currentTime % frequency*2 == 0){
            lightOn ^= true;
            draw();
        }


    }
    public void draw(){
        if(lightOn) StdDraw.picture(x,y,"images/logoOn.png",logoWidth,logoHeight);
        else StdDraw.picture(x,y,"images/logoOff.png",logoWidth,logoHeight);
    }
    public void changeFlashingSpeed(int speed){
        frequency = speed;
        if(speed == -1){
            flashing = false;
            lightOn = false;
            draw();
        }
        else  flashing = true;
    }
}
