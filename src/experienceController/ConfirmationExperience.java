package experienceController;

/*
 *
 *	This Experience asks the user to confirm that they want to Interact.
 *		
 *	DATE INITIALLY CREATED:
 *		03/02/15 - (Brandon Caruso)
 *		
 */

import java.io.File;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class ConfirmationExperience extends StackPane implements Experience {

	ExperienceController myController;
	private File imageFile = new File("../../background.jpg");
	private final String IMAGE_URL = imageFile.toURI().toString();

	StackPane pane;

	public ConfirmationExperience() {
		pane = new StackPane();
		// Add Background Image
		Image img = new Image(IMAGE_URL);
		ImageView imgView = new ImageView(img);

		imgView.setPreserveRatio(true);
		imgView.fitWidthProperty().bind(pane.widthProperty());
		imgView.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				goToSleepMode();
			}
		});

		pane.getChildren().add(imgView);
	}

	public void setParent(ExperienceController controller) {
		myController = controller;
	}

	public void startExperience() {
	}

	public void stopExperience() {
	}

	public Node getNode() {
		return pane;
	}

	private void goToSleepMode() {
		stopExperience();
		myController.setExperience(InteractiveWall.SLEEP_MODE);
	}

}