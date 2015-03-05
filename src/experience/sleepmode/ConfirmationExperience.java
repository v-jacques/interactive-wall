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
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.Experience;
import main.ExperienceController;
import main.InteractiveWall;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Listener;

public class ConfirmationExperience extends Listener implements Experience {

	ExperienceController myController;
	private File imageFile = new File("src/media/welcome.png");
	private final String IMAGE_URL = imageFile.toURI().toString();
	private File leftHandImage = new File("src/media/palmLeft.png");
	private final String LEFT_HAND_IMAGE_URL = leftHandImage.toURI().toString();
	private File rightHandImage = new File("src/media/palmRight.png");
	private final String RIGHT_HAND_IMAGE_URL = rightHandImage.toURI()
			.toString();

	Controller controller;
	StackPane pane;
	Pane canvas;
	Timeline timeline;
	Text t;
	ImageView rightHand;
	ImageView leftHand;
	AnimationTimer drawHands;

	double rightHandPosX = -50.0;
	double rightHandPosY = -50.0;

	double leftHandPosX = -50.0;
	double leftHandPosY = -50.0;

	Hand right;
	Hand left;

	public ConfirmationExperience() {

		rightHand = new ImageView(new Image(RIGHT_HAND_IMAGE_URL, 50, 50, true,
				true));
		leftHand = new ImageView(new Image(LEFT_HAND_IMAGE_URL, 50, 50, true,
				true));

		pane = new StackPane();
		canvas = new Pane();

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
		drawHands.start();
		timeline.play();
		controller = new Controller(this);
	}

	public void stopExperience() {
		right = null;
		left = null;

		rightHandPosX = -50.0;
		rightHandPosY = -50.0;

		leftHandPosX = -50.0;
		leftHandPosY = -50.0;

		drawHands.stop();
		timeline.stop();
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
			goToMainMenu();
		}
	}

	public void onFrame(Controller controller) {
		Frame frame = controller.frame();

		HandList hands = frame.hands();

		for (int i = 0; i < hands.count(); i++) {
			if (hands.get(i).isRight()) {
				right = hands.get(i);
				rightHandPosX = Math.round(right.palmPosition().getX()
						+ (pane.getWidth() / 2));
				rightHandPosY = (-1 * (Math.round(right.palmPosition().getY())))
						+ pane.getHeight();
			} else if (hands.get(i).isLeft()) {
				left = hands.get(i);
				leftHandPosX = Math.round(left.palmPosition().getX()
						+ (pane.getWidth() / 2));
				leftHandPosY = (-1 * (Math.round(left.palmPosition().getY())))
						+ pane.getHeight();
			}
		}
		shouldConfirm();
	}

}