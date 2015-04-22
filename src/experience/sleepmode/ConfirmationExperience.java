package experience.sleepmode;

/*
 *
 *	This Experience asks the user to confirm that they want to Interact.
 *		
 *	DATE INITIALLY CREATED:
 *		03/02/15 - (Brandon Caruso)
 *		
 */

import java.io.File;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.Experience;
import main.ExperienceController;
import main.InteractiveWall;
import main.SystemMusic;
import main.Util;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Listener;

public class ConfirmationExperience extends Listener implements Experience {

	ExperienceController myController;
	private File imageFile = new File("media/welcome.png");
	private final String IMAGE_URL = imageFile.toURI().toString();
	private File leftHandImage = new File("media/palmLeft.png");
	private final String LEFT_HAND_IMAGE_URL = leftHandImage.toURI().toString();
	private File rightHandImage = new File("media/palmRight.png");
	private final String RIGHT_HAND_IMAGE_URL = rightHandImage.toURI()
			.toString();
	private File leftHandFullImage = new File(
			"media/HoldLeft_fullHand_102_107.png");
	private final String LEFT_HAND_FULL_IMAGE_URL = leftHandFullImage.toURI()
			.toString();
	private File rightHandFullImage = new File(
			"media/Hold_fullHand_102_107.png");
	private final String RIGHT_HAND_FULL_IMAGE_URL = rightHandFullImage.toURI()
			.toString();

	Controller controller;
	StackPane pane;
	Pane canvas;
	Timeline timeline;
	Timeline goToMainMenu;
	Text t;
	Image leftHandNormal;
	Image rightHandNormal;
	ImageView rightHand;
	ImageView leftHand;
	AnimationTimer drawHands;

	double rightHandPosX = -100.0;
	double rightHandPosY = -100.0;

	double leftHandPosX = -100.0;
	double leftHandPosY = -100.0;

	Hand right;
	Hand left;

	MediaPlayer confirmSound;
	private File audiofile = new File("media/confirmComplete.mp3");
	private final String MEDIA_URL = audiofile.toURI().toString();

	public ConfirmationExperience() {
		rightHandNormal = new Image(RIGHT_HAND_IMAGE_URL, 100, 100, true, true);
		rightHand = new ImageView(rightHandNormal);
		leftHandNormal = new Image(LEFT_HAND_IMAGE_URL, 100, 100, true, true);
		leftHand = new ImageView(leftHandNormal);
		Image rightHandFull = new Image(RIGHT_HAND_FULL_IMAGE_URL, 100, 100,
				true, true);
		Image leftHandFull = new Image(LEFT_HAND_FULL_IMAGE_URL, 100, 100,
				true, true);

		pane = new StackPane();
		canvas = new Pane();

		Media media = new Media(MEDIA_URL);
		confirmSound = new MediaPlayer(media);
		confirmSound.setVolume(.25);

		// Add Background Image
		Image img = new Image(IMAGE_URL);
		ImageView imgView = new ImageView(img);

		imgView.setPreserveRatio(true);

		DoubleProperty widthImg = imgView.fitWidthProperty();
		DoubleProperty heightImg = imgView.fitHeightProperty();

		widthImg.bind(Bindings.selectDouble(imgView.sceneProperty(), "width"));
		heightImg
				.bind(Bindings.selectDouble(imgView.sceneProperty(), "height"));

		timeline = new Timeline(new KeyFrame(Duration.millis(15000),
				ae -> goToSleepMode()));

		goToMainMenu = new Timeline(new KeyFrame(Duration.seconds(1),
				new KeyValue(rightHand.imageProperty(), rightHandFull),
				new KeyValue(leftHand.imageProperty(), leftHandFull)),
				new KeyFrame(Duration.seconds(2), ae -> confirmSound.play()),
				new KeyFrame(Duration.seconds(3), ae -> goToMainMenu()));

		drawHands = new AnimationTimer() {
			@Override
			public void handle(long now) {
				rightHand.setTranslateX(rightHandPosX);
				rightHand.setTranslateY(rightHandPosY);
				leftHand.setTranslateX(leftHandPosX);
				leftHand.setTranslateY(leftHandPosY);
			}
		};

		// resetHands = new AnimationTimer() {
		// @Override
		// public void handle(long now) {
		// rightHand.setTranslateX(rightHandPosX);
		// rightHand.setTranslateY(rightHandPosY);
		// leftHand.setTranslateX(leftHandPosX);
		// leftHand.setTranslateY(leftHandPosY);
		// }
		// };

		canvas.getChildren().addAll(rightHand, leftHand);

		rightHand.relocate(rightHandPosX, rightHandPosY);
		leftHand.relocate(leftHandPosX, leftHandPosY);

		pane.getChildren().add(imgView);
		pane.getChildren().add(canvas);

	}

	public void setParent(ExperienceController controller) {
		myController = controller;
	}

	public void startExperience() {
		SystemMusic.playBackgroundMusic();

		drawHands.start();
		timeline.play();

		rightHandPosX = -100.0;
		rightHandPosY = -100.0;

		leftHandPosX = -100.0;
		leftHandPosY = -100.0;

		rightHand.relocate(rightHandPosX, rightHandPosY);
		leftHand.relocate(leftHandPosX, leftHandPosY);
		controller = new Controller(this);

	}

	public void stopExperience() {
		right = null;
		left = null;
		leftHand.setImage(leftHandNormal);
		rightHand.setImage(rightHandNormal);
		rightHandPosX = -100.0;
		rightHandPosY = -100.0;

		leftHandPosX = -100.0;
		leftHandPosY = -100.0;
		confirmSound.stop();
		drawHands.stop();
		timeline.stop();
		goToMainMenu.stop();
	}

	public Node getNode() {
		return pane;
	}

	private void goToSleepMode() {
		controller.removeListener(this);
		myController.setExperience(InteractiveWall.SLEEP_MODE);
	}

	private void goToMainMenu() {
		controller.removeListener(this);
		myController.setExperience(InteractiveWall.MAIN_MENU);
	}

	private void shouldConfirm() {
		if (right != null && left != null && right.timeVisible() > 2
				&& left.timeVisible() > 2) {
			goToMainMenu.play();
		}
	}

	public void onFrame(Controller controller) {
		Frame frame = controller.frame();

		HandList hands = frame.hands();

		for (int i = 0; i < hands.count(); i++) {
			if (hands.get(i).isRight()) {
				right = hands.get(i);
				rightHandPosX = Util.leapXtoPanelX(right
						.stabilizedPalmPosition().getX());
				rightHandPosY = Util.leapYToPanelY(right
						.stabilizedPalmPosition().getY());

			} else if (hands.get(i).isLeft()) {
				left = hands.get(i);
				leftHandPosX = Util.leapXtoPanelX(left.stabilizedPalmPosition()
						.getX());
				leftHandPosY = Util.leapYToPanelY(left.stabilizedPalmPosition()
						.getY());
			}
		}
		shouldConfirm();
	}

}