package mainmenu;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.Timer;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Hand;

public class MainMenuExperience extends Application {
	public static Hand h;
	public static PanelMainMenu p;
	public static Timer t1;
	public static Timer t2;
	public static Timer t3;
	public static Timer t4;

	public static int frameWidth = 800;
	public static int frameHeight = 600;

	public static void main(String[] args) {
		Controller controller = new Controller();
		SampleListener listener = new SampleListener();
		controller.addListener(listener);
		t1 = new Timer(3000, new TimerActionListener("1st Experience"));
		t2 = new Timer(3000, new TimerActionListener("2nd Experience"));
		t3 = new Timer(3000, new TimerActionListener("3rd Experience"));
		t4 = new Timer(3000, new TimerActionListener("4th Experience"));

		launch(args);
		
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		controller.removeListener(listener);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("MainMenuExperience");

		Pane pane = new Pane();
		
		Image blockImg = new Image("media/BlockMenu486_289.png", 486, 289, false, false);
		ImageView blockView = new ImageView(blockImg);
		blockView.setLayoutX(270);
		blockView.setLayoutY(147);
		pane.getChildren().add(blockView);
		
		Image fireworkImg = new Image("media/FireworkMenu486_289.png", 486, 289, false, false);
		ImageView fireworkView = new ImageView(fireworkImg);
		fireworkView.setLayoutX(843);
		fireworkView.setLayoutY(147);
		pane.getChildren().add(fireworkView);
		
		Image galleryImg = new Image("media/GalleryMenu486_289.png", 486, 289, false, false);
		ImageView galleryView = new ImageView(galleryImg);
		galleryView.setLayoutX(270);
		galleryView.setLayoutY(509);
		pane.getChildren().add(galleryView);
		
		Image pondImg = new Image("media/PondMenu486_289.png", 486, 289, false, false);
		ImageView pondView = new ImageView(pondImg);
		pondView.setLayoutX(843);
		pondView.setLayoutY(509);
		pane.getChildren().add(pondView);
		
		Scene scene = new Scene(pane, 1600, 1000);
		scene.getStylesheets().add(MainMenuExperience.class.getResource("../css/mainmenu.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
