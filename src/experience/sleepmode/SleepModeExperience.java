package experience.sleepmode;

/*
 *
 *	This Experience Play a Video Loop until a motion is detected by the sensor.
 *		
 *	DATE INITIALLY CREATED:
 *		03/02/15 - (Brandon Caruso)
 *		
 */

import java.io.File;

import main.Experience;
import main.ExperienceController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Listener;

public class SleepModeExperience extends Listener implements Experience {

	Controller controller;
	ExperienceController myController;
	MediaPlayer mediaPlayer;
	private File videofile = new File("src/media/testVideo.mp4");
	private final String MEDIA_URL = videofile.toURI().toString();

	BorderPane pane;

	public SleepModeExperience() {
		pane = new BorderPane();
		// create media player
		Media media = new Media(MEDIA_URL);
		mediaPlayer = new MediaPlayer(media);

		// create mediaView and add media player to the viewer
		MediaView mediaView = new MediaView(mediaPlayer);

		DoubleProperty width = mediaView.fitWidthProperty();
		DoubleProperty height = mediaView.fitHeightProperty();

		width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
		height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
		pane.setCenter(mediaView);
		mediaView.setPreserveRatio(true);
	}

	public void startExperience() {
		mediaPlayer.play();
		controller = new Controller(this);
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
		controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
		controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
	}

	public void stopExperience() {
		mediaPlayer.stop();
	}

	public Node getNode() {
		return pane;
	}

	private void goToConfirm() {
		controller.removeListener(this);
		// myController.setExperience(controller.CONFRIMATION);
	}

	public void onFrame(Controller controller) {
		Frame frame = controller.frame();

		if (frame.gestures().count() > 0) {
			goToConfirm();
		}
	}

	@Override
	public void setParent(ExperienceController controller) {
		myController = controller;
	}

}