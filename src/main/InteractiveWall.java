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
import experience.pond.PondExperience;
import experience.firework.FireworkExperience;
import experience.block.BlockExperience;
import experience.gallery.GalleryExperience;
import experience.sleepmode.ConfirmationExperience;
import experience.sleepmode.SleepModeExperience;
import static javafx.application.Application.launch;
import javafx.scene.Cursor;

public class InteractiveWall extends Application {

	public static final String SLEEP_MODE = "sleep";
	public static final Experience SLEEP_MODE_Ex = new SleepModeExperience();
	public static final String CONFIRMATION = "confirm";
	public static final Experience CONFRIMATION_Ex = new ConfirmationExperience();
	public static final String MAIN_MENU = "main";
	public static final Experience MAIN_MENU_Ex = new MainMenuExperience();
	public static final String POND = "pond";
	public static final Experience POND_Ex = new PondExperience();
	public static final String FIREWORK = "firework";
	public static final Experience FIREWORK_Ex = new FireworkExperience();
	public static final String BLOCK = "block";
	public static final Experience BLOCK_Ex = new BlockExperience();
	public static final String GALLERY = "gallery";
	public static final Experience GALLERY_Ex = new GalleryExperience();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		ExperienceController mainContainer = new ExperienceController();

		mainContainer.loadExperience(SLEEP_MODE, SLEEP_MODE_Ex);
		mainContainer.loadExperience(CONFIRMATION, CONFRIMATION_Ex);
		mainContainer.loadExperience(MAIN_MENU, MAIN_MENU_Ex);
		mainContainer.loadExperience(POND, POND_Ex);
		mainContainer.loadExperience(FIREWORK, FIREWORK_Ex);
		mainContainer.loadExperience(BLOCK, BLOCK_Ex);
		mainContainer.loadExperience(GALLERY, GALLERY_Ex);

		//mainContainer.setExperience(SLEEP_MODE);
		mainContainer.setExperience(GALLERY);

		Scene main = new Scene(mainContainer, 1600, 1000);
		main.setFill(Color.BLACK);
		main.setCursor(Cursor.NONE);
		primaryStage.setScene(main);
		primaryStage.setTitle("Interactive Wall");
		// primaryStage.setFullScreen(true);
		primaryStage.show();

	}
}