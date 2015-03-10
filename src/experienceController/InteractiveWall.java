
/*
*
*	This is the main class for this JavaFX application. All this does is set the primary
*	stage and scene. The Scene Controller does the rest swapping out the different
*	Panes.
*		
*	DATE INITIALLY CREATED:
*		03/02/15 - (Brandon Caruso)
*		
*/

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.paint.Color;


public class InteractiveWall extends Application{
 
     public static final String SLEEP_MODE = "sleep" ; 
     public static final Experience SLEEP_MODE_Ex = new SleepModeExperience() ; 
     public static final String CONFRIMATION = "confirm" ; 
     public static final Experience CONFRIMATION_Ex = new ConfirmationExperience();
      
      
     @Override 
     public void start(Stage primaryStage) { 

       ExperienceController mainContainer = new ExperienceController(); 
      	
       mainContainer.loadExperience(SLEEP_MODE, SLEEP_MODE_Ex);
	   mainContainer.loadExperience(CONFRIMATION, CONFRIMATION_Ex);

	   mainContainer.setExperience(SLEEP_MODE);

	   Group root = new Group(); 
	   root.getChildren().addAll(mainContainer); 
	   Scene main = new Scene(root, 1280, 720);
	   main.setFill(Color.BLACK);

	   primaryStage.setScene(main); 
	   primaryStage.show(); 
	   
     } 
}