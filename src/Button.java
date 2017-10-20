/**
 * Created by Jon on 4/30/15.
 */
public class Button {
    protected String name;
    protected String color;
    protected double x;
    protected double y;
    protected double radius;
    protected boolean isClicked = false;
    protected boolean updated;

    public Button(String name, String color, double x, double y, double radius){
        this.name = name;
        this.color = color;
        this.x = x;
        this.y = y;
        this.radius = radius;
        draw();
    }
    public void update(){
        if(!updated) draw();
        updated = true;
    }
    public void draw(){
        //01 unclicked 02 clicked yellow_01.png
        String imageName;
        if(isClicked){
            imageName = "images/buttons/"+color+"_01.png";

        }
        else{
            imageName = "images/buttons/"+color+"_02.png";
        }
        StdDraw.picture(x,y,imageName,radius,radius);
    }
    public double getDistance(double x1, double y1, double x2, double y2){
        double distance = Math.sqrt(Math.pow((x2-x1),2)+Math.pow((y2-y1),2));
        return distance;
    }
    public boolean checkForClick(double xLoc, double yLoc){
        double tempDistance = getDistance(x,y,xLoc,yLoc);
        if(tempDistance <= radius*.7){
            isClicked = true;
            return true;
        }
        else{
            isClicked = false;
            return false;
        }

    }
}

