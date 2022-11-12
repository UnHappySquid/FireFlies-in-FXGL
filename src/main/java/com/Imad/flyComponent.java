package com.Imad;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static com.almasb.fxgl.core.math.FXGLMath.*;

public class flyComponent extends Component {
  // actual location in the swarm
  double aX;
  double aY;
  // spawning area
  double aRadius;
  // location of center
  static double  aCenterX = FXGL.getAppWidth()/2.;
  static double  aCenterY= FXGL.getAppHeight()/2.;
  // distance from center
  double aDistance;
  // angle from center
  double aAngle;

  double aMaxSpeed;
  Fly myFly;
  // used for cos functions
  double currentPhase;


  public flyComponent(double pRaduis){
    aRadius=pRaduis;
  }

  public class Fly extends Pane {
    double aRadius;
    Circle fly;
    Glow flyGlowEffect;
    DropShadow myEffect;

    public Fly(double x, double y, double radius,Color color){
      fly = new Circle(x,y, radius, color);
      fly.setLayoutX(aCenterX);
      fly.setLayoutY(aCenterY);
      aRadius = radius;
      getChildren().addAll(fly);


      myEffect = new DropShadow();
      flyGlowEffect = new Glow();



      myEffect.setBlurType(BlurType.GAUSSIAN);
      myEffect.setColor(color);
      myEffect.setRadius(radius*100);
      myEffect.setSpread(0.5);
      flyGlowEffect.setInput(myEffect);

      this.setEffect(myEffect);

    }

    public void onUpdate(double pPhase){

      myEffect.setRadius(abs(127*cos(pPhase)));
      myEffect.setSpread(abs(cos(pPhase)*0.95));
    }

    public boolean isCentered() {
      return (fly.getLayoutX()) == (aCenterX) && (fly.getLayoutY())== (aCenterY) ;
    }

    public void move(double aMaxSpeed) {
    double xLayout = fly.getLayoutX();
    double yLayout = fly.getLayoutY();
    fly.setLayoutX(xLayout+((aCenterX-xLayout)*aMaxSpeed));
    fly.setLayoutY(yLayout+((aCenterY-yLayout)*aMaxSpeed));
    }
  }


  @Override
  public void onAdded() {

    aDistance = random(0,aRadius);
    aAngle = random(0,PI2);
    aX = cos(aAngle)*aDistance;
    aY = sin(aAngle)*aDistance;

    aMaxSpeed = random(0.1,0.75);
    currentPhase = random(0,PI/2);

    myFly = new Fly(aX,aY,1,Color.WHITE);
    entity.getViewComponent().addChild(myFly);
  }


  @Override
  public void onUpdate(double tpf) {
    currentPhase += 0.01;
    myFly.onUpdate(currentPhase);

    if (!myFly.isCentered()){
      myFly.move(aMaxSpeed);
    }
  }

}
