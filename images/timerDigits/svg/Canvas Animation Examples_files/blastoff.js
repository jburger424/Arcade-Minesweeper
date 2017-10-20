window.addEventListener("load",eventWindowLoaded, false);

function eventWindowLoaded() {
    canvasApp();	
}

function canvasSupport() {
    return Modernizr.canvas;	
}

function canvasApp() {
    if (!canvasSupport()) {
        return;
    }

    var theCanvas = document.getElementById("myCanvas");
    var height = theCanvas.height; //get the heigth of the canvas
    var width = theCanvas.width;  //get the width of the canvas
    var context = theCanvas.getContext("2d");  //get the context
    var then = Date.now();
    var bgImage = new Image();
    var stars = new Array;

    bgImage.onload = function() {
        context.translate(width/2,height/2);
        main();
    }


    /*
    var window = {
        xMin : -width/2,
        xMax : width/2,
        yMin : -width/2,
        yMax :width/2,
        inWinow : function(x,y){
            if(x > this.xMin && x < this.xMax && y > this.yMin && y < this.yMax) return true;
            else return false;
        }

    */
    var rocket = {
        xLoc: 0,
        yLoc: 0,
        xPoint:0,
        yPoint: 0,
        score : 0,
        damage : 0,
        speed : 300,
        turningSpeed : 500,
        minAngle: -.3,
        maxAngle:.3,
        angle : 0,
        targetAngle : 0,
        rotSpeed : 3,
        rotChange: 0,
        width: 110,
        height: 150,
        xMin:-width/2,
        xMax: width/2,


        setScore : function(newScore){
            this.score = newScore;
        }
    }

    function Star(){
        var dLoc = width;
        var validLoc = false;
        while(!validLoc){
            this.xLoc = dLoc - Math.random()*2*dLoc;
            this.yLoc = rocket.yLoc + dLoc - Math.random()*2*dLoc;
            validLoc = true;
        }

        this.radius = 20;
        this.hasCollided = false;
        //console.log(rocket.xLoc+" "+rocket.yLoc);
        this.draw = function(){
            drawStar(this.xLoc,this.yLoc,this.radius,5,.5);
        }
    }

    //var stars = new Array;

    var drawStars = function(){
        if (typeof stars !== 'undefined'){
            //console.log("working");
            for(var i=0;i< stars.length ;i++){
                if(stars[i].hasCollided){
                    context.fillStyle = "grey";
                }
                else{
                    context.fillStyle = "yellow";
                }
                stars[i].draw();
            }
        }


    };

    var getDistance = function(x1,y1,x2,y2){
        var distance = Math.sqrt(Math.pow((x2-x1),2)+Math.pow((y2-y1),2));
        return distance;
    };

    var updateStars = function(){
        var numStars = 10;
        while(stars.length<numStars){
            stars[stars.length] = new Star();
        }
        for(var i=0; i<stars.length; i++){
            var tempDist = getDistance(rocket.xLoc,rocket.yLoc,stars[i].xLoc,stars[i].yLoc);
            if(i == 0){
                //console.log(tempDist);
            }
            if(tempDist > width){
                stars[i] = new Star();
            }
        }
    };

    var detectStarCollision = function(){
        for(var i = 0; i<stars.length;i++){
                if(!stars[i].hasCollided && rocket.xLoc <= stars[i].xLoc+stars[i].radius  && rocket.xLoc >= stars[i].xLoc-stars[i].radius
                && rocket.yLoc <= stars[i].yLoc+stars[i].radius && rocket.yLoc >= stars[i].yLoc-stars[i].radius){
                    console.log(stars[i].hasCollided);
                    stars[i].hasCollided = true;
                    console.log("collision at "+i);
                }

        }
    }

    function drawRocket(){
        var angle = rocket.angle;
        var scale = rocket.width/76;
        rocket.height = rocket.width/76;

        var xPoint = rocket.xLoc;
        var yPoint = rocket.yLoc;

        var rwX = [xPoint+17,xPoint+38,xPoint+17,xPoint+17]; var rwY = [yPoint+73,yPoint+66,yPoint+29,yPoint+73]; //rightwind
        var lwX = [xPoint-17,xPoint-38,xPoint-17,xPoint-17]; var lwY = [yPoint+73,yPoint+66,yPoint+29,yPoint+73]; //leftwing

        var bX = [xPoint+0,xPoint+0,xPoint-33,xPoint-14,xPoint-14,xPoint-10,xPoint+0,xPoint+10,xPoint+14,xPoint+14,xPoint+33,xPoint+0,xPoint+0]; // body
        var bY = [yPoint+0,yPoint+0,yPoint+31,yPoint+82,yPoint+82,yPoint+89,yPoint+89,yPoint+89,yPoint+82,yPoint+82,yPoint+31,yPoint+0,yPoint+0];

        var nX = [xPoint+0,xPoint+0,xPoint-8,xPoint-14,xPoint-10,xPoint-5,xPoint+0,xPoint+5,xPoint+10,xPoint+14,xPoint+8,xPoint+0,xPoint+0]; //nose
        var nY = [yPoint+0,yPoint+0,yPoint+7,yPoint+22,yPoint+24,yPoint+25,yPoint+25,yPoint+25,yPoint+24,yPoint+22,yPoint+7,yPoint+0,yPoint+0];

        var xPoints  = [rwX,lwX,bX,nX];
        var yPoints = [rwY,lwY,bY,nY]


        for(var j = 0; j < xPoints.length; j++){
            for(var i = 0; i < xPoints[j].length; i++){
                xPoints[j][i] -= rocket.xLoc;
                yPoints[j][i] -= rocket.yLoc+rocket.height;
                //console.log("which height:"+this.height/2);

                xPoints[j][i] *= scale;
                yPoints[j][i] *= scale;

                var tempXVal = xPoints[j][i]*Math.cos(angle) - yPoints[j][i]*Math.sin(angle);
                var tempYVal =  xPoints[j][i]*Math.sin(angle) +  yPoints[j][i]*Math.cos(angle);

                xPoints[j][i] = tempXVal;
                yPoints[j][i] = tempYVal;

                xPoints[j][i] += rocket.xLoc;
                yPoints[j][i] += rocket.yLoc + rocket.height;
                //xVals[i] = tempXVal + xLoc;
                //yVals[i] = tempYVal+yLoc;//+(yLoc+rHeight);
            }
        }


        //right wing point
        rocket.xPoint = bX[0];
        rocket.yPoint = bY[0];




        //right wing
        context.fillStyle="rgb(0,0,255)";
        context.beginPath();
        context.moveTo(rwX[0],rwY[0]);
        context.lineTo(rwX[1],rwY[1]);
        context.lineTo(rwX[2],rwY[2]);
        context.lineTo(rwX[3],rwY[3]);
        context.fill();

        //left wing
        context.fillStyle="rgb(0,0,255)";
        context.beginPath();
        context.moveTo(lwX[0],lwY[0]);
        context.lineTo(lwX[1],lwY[1]);
        context.lineTo(lwX[2],lwY[2]);
        context.lineTo(lwX[3],lwY[3]);
        context.fill();

        //body


        context.fillStyle="rgb(156,0,0)";
        context.beginPath();
        context.moveTo(bX[0],bY[0]);//point
        context.bezierCurveTo(bX[1],bY[1],bX[2],bY[2],bX[3],bY[3]);//point to left
        context.bezierCurveTo(bX[4],bY[4],bX[5],bY[5],bX[6],bY[6]);//left to center
        context.bezierCurveTo(bX[7],bY[7],bX[8],bY[8],bX[9],bY[9]);//center to right
        context.bezierCurveTo(bX[10],bY[10],bX[11],bY[11],bX[12],bY[12]);//right to point
        context.fill();

        //nose

        context.fillStyle="rgb(0,0,255)";
        context.beginPath();
        context.moveTo(nX[0],nY[0]);
        context.bezierCurveTo(nX[1],nY[1],nX[2],nY[2],nX[3],nY[3]); //point to left
        context.bezierCurveTo(nX[4],nY[4],nX[5],nY[5],nX[6],nY[6]);//left to center
        context.bezierCurveTo(nX[7],nY[7],nX[8],nY[8],nX[9],nY[9]);//center to right
        context.bezierCurveTo(nX[10],nY[10],nX[11],nY[11],nX[12],nY[12]);//right to point
        context.fill();




    }

    var world = {
        //pixels per second
        startTime: Date.now(),
        speed: 50,
        startX:width/2,
        startY:height/2,
        originX: 0,
        originY: 0,
        xDist: 0,
        yDist: 0,
        rotationSpeed: 20,
        angle: 0,
        distance: 0,
        calcOrigins : function(){
            world.originX = -world.distance*Math.sin(world.angle*Math.PI/180);
            world.originY = -world.distance*Math.cos(world.angle*Math.PI/180);
        }
    };

    var keysDown = {};

    addEventListener("keydown", function (e) {
	   keysDown[e.keyCode] = true;
    }, false);

    addEventListener("keyup", function (e) {
	   delete keysDown[e.keyCode];
    }, false);
    
    var update = function(modifier) {
        detectStarCollision();
        if (37 in keysDown) { // Player holding left
            if(rocket.xLoc > rocket.xMin){
                rocket.xLoc -= rocket.turningSpeed* modifier;
                rocket.targetAngle = rocket.minAngle;
            }
            else{
                rocket.xLoc = rocket.xMin;
                rocket.targetAngle = 0;
            }

        }
        if (39 in keysDown) { // Player holding right
            if(rocket.xLoc < rocket.xMax){
                rocket.xLoc += rocket.turningSpeed* modifier;
                rocket.targetAngle = rocket.maxAngle;
            }
            else{
                rocket.xLoc = rocket.xMax;
                rocket.targetAngle = 0;
            }



        }
        if(!(37 in keysDown) && !(39 in keysDown)){
            rocket.targetAngle = 0;
        }

        if(rocket.targetAngle < rocket.angle){
            if(rocket.angle > rocket.minAngle) rocket.angle -= rocket.rotSpeed * modifier;
            else rocket.angle = rocket.targetAngle;
        }
        if(rocket.targetAngle > rocket.angle){
            if(rocket.angle < rocket.maxAngle) rocket.angle += rocket.rotSpeed * modifier;
            else rocket.angle = rocket.targetAngle;
        }


    };


    
    var render = function (modifier) {

        //var dX = (rocket.speed*modifier)*Math.sin(rocket.angle);
        var dY = (rocket.speed*modifier);
        context.clearRect(0-width/2,rocket.yLoc-height/2,width,height);
        //rocket.xLoc += dX;
        rocket.yLoc -= dY;
        window.yMax -= dY;
        window.yMin -= dY;


        updateStars();
        drawStars();
        context.restore();
        context.translate(0,dY);
        //context.save();
        //context.translate(-rocket.pointX,-rocket.pointY);




        //context.translate(rocket.pointX,rocket.pointY);
        drawRocket(110);


        //context.restore(); // restores the coordinate system back to (0,0)

        context.fillStyle = "green";
        context.fillRect(rocket.xLoc,rocket.yLoc,15,15);
        context.fillStyle = "blue";
        context.fillRect(rocket.xPoint,rocket.yPoint,10,10);
        //context.rotate(rocket.angle);
        //context.restore();











    };

    var drawStar = function(x, y, r, p, m){
        context.save();
        context.beginPath();
        context.translate(x, y);
        context.moveTo(0,0-r);
        for (var i = 0; i < p; i++)
        {
            context.rotate(Math.PI / p);
            context.lineTo(0, 0 - (r*m));
            context.rotate(Math.PI / p);
            context.lineTo(0, 0 - r);
        }
        context.fill();
        context.restore();
    }

   

    // the game loop

    function main(){
        requestAnimationFrame(main);


        var now = Date.now();
        var delta = now - then;

	   update(delta / 1000);

	   render(delta / 1000);
	   then = now;
	// Request to do this again ASAP


    }
    
    var w = window;
    var requestAnimationFrame = w.requestAnimationFrame || w.webkitRequestAnimationFrame || w.msRequestAnimationFrame ||    w.mozRequestAnimationFrame;
    //start the game loop
    //gameLoop();

    //event listenters
    bgImage.src = "images/background.jpg";
    

    
} //canvasApp()
