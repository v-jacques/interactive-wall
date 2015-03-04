package main;

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
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import experience.mainmenu.MainMenuExperience;
import experience.sleepmode.ConfirmationExperience;
import experience.sleepmode.SleepModeExperience;

public class InteractiveWall extends Application {

	public static final String SLEEP_MODE = "sleep";
	public static final Experience SLEEP_MODE_Ex = new SleepModeExperience();
	public static final String CONFRIMATION = "confirm";
	public static final Experience CONFRIMATION_Ex = new ConfirmationExperience();
	public static final String MAIN_MENU = "main";
	public static final Experience MAIN_MENU_Ex = new MainMenuExperience();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		ExperienceController mainContainer = new ExperienceController();

		mainContainer.loadExperience(SLEEP_MODE, SLEEP_MODE_Ex);
		mainContainer.loadExperience(CONFRIMATION, CONFRIMATION_Ex);
		mainContainer.loadExperience(MAIN_MENU, MAIN_MENU_Ex);

		mainContainer.setExperience(SLEEP_MODE);

		Scene main = new Scene(mainContainer, 1600, 1000);
		main.setFill(Color.BLACK);
		primaryStage.setScene(main);
		primaryStage.setTitle("Interactive Wall");
		primaryStage.show();

	}
}