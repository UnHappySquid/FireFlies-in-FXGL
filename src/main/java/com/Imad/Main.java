package com.Imad;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.SpawnData;
import java.awt.Dimension;
import java.awt.Toolkit;
import javafx.scene.paint.Color;

import static com.almasb.fxgl.dsl.FXGL.*;
public class Main extends GameApplication {

// to get app width and height
  Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  double width = screenSize.getWidth();
  double height = screenSize.getHeight();

  private int fliesAmount=300;

  @Override
  protected void initSettings(GameSettings gameSettings) {
    gameSettings.setTitle("Flies");
    gameSettings.setVersion("");
    gameSettings.setWidth((int)width);
    gameSettings.setHeight((int)height);
    gameSettings.setFullScreenAllowed(true);
    gameSettings.setFullScreenFromStart(true);
  }

  @Override
  protected void initGame() {
    getGameScene().setBackgroundColor(Color.BLACK.brighter());
    getGameWorld().addEntityFactory(new FliesFactory());

    for (int i=0; i<fliesAmount; i++){
      spawn("fly", new SpawnData().put("radius", 200.));
    }

    spawn("FG");
  }

  public static void main(String[] args) {
    launch(args);
  }


}