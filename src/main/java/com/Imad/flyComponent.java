package com.Imad;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static com.almasb.fxgl.core.math.FXGLMath.*;

public class flyComponent extends Component {
  // current location in the swarm
  double aX;
  double aY;
  // previous location in the swarm
  double pX;
  double pY;
  // spawning area
  double aRadius;

  // location of center, starting point of the swarm
  static double aCurrentCenterX = FXGL.getAppWidth()/2.;
  static double aCurrentCenterY = FXGL.getAppHeight()/2.;

  // distance from center
  double aDistance;
  // angle from center
  double aAngle;

  // max speed of fly, <=1
  double aMaxSpeed;
  // Fly Object, visual and movement of each fly is handled in this class
  Fly myFly;

  // used for cos functions
  double currentPhase;


  public flyComponent(double pRaduis){
    aRadius=pRaduis;
  }

  public class Fly extends Pane {
    // used in onUpdate of Fly
    double currentPhase;
    // next 2 used in Move
    double xTranslate;
    double yTranslate;
    // actual fly Nodes and they correspoding properties
    Circle fly;
    double flyRadius;
    Circle flyGlowLevelOne;
    double flyGlowLevelOneRadiusScalar = 10.;
    double flyGlowLevelOneRadius;
    Circle flyGlowLevelTwo;
    double flyGlowLevelTwoRadiusScalar = 20.;
    double flyGlowLevelTwoRadius;
    Circle flyGlowLevelThree;
    double flyGlowLevelThreeRadiusScalar = 30.;
    double flyGlowLevelThreeRadius;

    /**
     *  Constructs Fly at x,y with radius radius and color color
     * @param x
     * @param y
     * @param radius
     * @param color
     */
    public Fly(double x, double y, double radius,Color color){
      // actual fly
      flyRadius = radius;
      fly = new Circle(x,y, flyRadius, color);
      fly.setLayoutX(aCurrentCenterX);
      fly.setLayoutY(aCurrentCenterY);
      // firstLevelofGlow
      flyGlowLevelOneRadius = flyRadius*flyGlowLevelOneRadiusScalar;
      flyGlowLevelOne = new Circle(x,y,flyGlowLevelOneRadius,color);
      flyGlowLevelOne.setLayoutX(aCurrentCenterX);
      flyGlowLevelOne.setLayoutY(aCurrentCenterY);
      flyGlowLevelOne.setOpacity(0.1);
      // secondLevelofGlow
      flyGlowLevelTwoRadius = flyRadius*flyGlowLevelTwoRadiusScalar;
      flyGlowLevelTwo = new Circle(x,y,flyGlowLevelTwoRadius,color);
      flyGlowLevelTwo.setLayoutX(aCurrentCenterX);
      flyGlowLevelTwo.setLayoutY(aCurrentCenterY);
      flyGlowLevelTwo.setOpacity(0.05);
      // thirdLevelofGlow
      flyGlowLevelThreeRadius = flyRadius*flyGlowLevelThreeRadiusScalar;
      flyGlowLevelThree = new Circle(x,y,flyGlowLevelThreeRadius,color);
      flyGlowLevelThree.setLayoutX(aCurrentCenterX);
      flyGlowLevelThree.setLayoutY(aCurrentCenterY);
      flyGlowLevelThree.setOpacity(0.01);
      // addAll to Pane
      getChildren().addAll(fly, flyGlowLevelOne,flyGlowLevelTwo,flyGlowLevelThree);
    }



    /**
     *  Changes the radius of the glow effects around the Fly
     * @param pPhase
     * @pre pPhase is in radiants
     */
    public void onUpdate(double pPhase){
      currentPhase = cos(pPhase);

      flyGlowLevelOne.setRadius(currentPhase*(flyGlowLevelOneRadius/4.0)+flyGlowLevelOneRadius/2.);
      flyGlowLevelTwo.setRadius(currentPhase*(flyGlowLevelTwoRadius/4.0)+flyGlowLevelTwoRadius/2.);
      flyGlowLevelThree.setRadius(currentPhase*(flyGlowLevelThreeRadius/4.0)+flyGlowLevelThreeRadius/2.);
    }

    /**
     *  Checks if fly is centered
     * @return true if fly is centered, false Otherwise
     */
    public boolean isCentered() {
      return (fly.getCenterX()) == (aCurrentCenterX +aX) && (fly.getCenterY())== (aCurrentCenterY +aY) ;
    }


    /**
     *  Moves fly towards aCurrentCenter at speed aMaxSpeed
     * @param aMaxSpeed
     * @pre aMaxSpeed <=1
     */
    public void move(double aMaxSpeed) {
      xTranslate = getTranslateX() ;
      yTranslate = getTranslateY();
     setTranslateX(  xTranslate + ((aCurrentCenterX - FXGL.getAppWidth()/2.)-xTranslate)*aMaxSpeed);
      setTranslateY(  yTranslate + ((aCurrentCenterY-FXGL.getAppHeight()/2.)-yTranslate)*aMaxSpeed);
      //     These work but run slower much worse than Translate
      //      xLayout = fly.getLayoutX();
      //      yLayout = fly.getLayoutY();
      //      fly.setLayoutX(xLayout+((aCenterX-xLayout)*aMaxSpeed));  Too slow, Layout not made for this or something
      //      fly.setLayoutY(yLayout+((aCenterY-yLayout)*aMaxSpeed));
    }

    /**
     * Moves Fly by x and y
     * @param x a double
     * @param y a double
     */
    public void moveBy(double x, double y) {
      setTranslateX(getTranslateX()+x);
      setTranslateY(getTranslateY()+y);
    }
  }

  @Override
  public void onAdded() {

    aDistance = random(0,aRadius);
    aAngle = random(0,PI2);
    aX = cos(aAngle)*aDistance;
    aY = sin(aAngle)*aDistance;

    aMaxSpeed = random(0.01,0.05);
//    aMaxSpeed = 0.01;
    currentPhase = random(0,PI/2);

    myFly = new Fly(aX,aY,1,Color.GREENYELLOW);
    entity.getViewComponent().addChild(myFly);
  }


  @Override
  public void onUpdate(double tpf) {

    currentPhase += 0.01;
    pX = aX;
    pY = aY;
    aX += random(-1,1);
    aY += random(-1,1);
    myFly.moveBy((aX-pX),(aY-pY));

    myFly.onUpdate(currentPhase);

    if (!myFly.isCentered()){
      myFly.move(aMaxSpeed);
    }
  }

}
