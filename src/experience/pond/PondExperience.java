package experience.pond;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import main.Experience;
import main.ExperienceController;
import main.InteractiveWall;
import main.Util;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Listener;

public class PondExperience extends Listener implements Experience {
	Controller controller;
	ExperienceController myController;
	StackPane pane;
	Pane canvas;
	Timeline sleepTimer;
	Hand right;
	Hand left;
	ImageView rightHand;
	ImageView leftHand;
	AnimationTimer drawHands;
	ArrayList<Circle> circles;
	AnimationTimer drawCircles;
	Timeline circleAnimation;

	int circleNumber = 0;

	double rightHandPosX = -50.0;
	double rightHandPosY = -50.0;
	double realRightHandPosX = -50.0;
	double realRightHandPosY = -50.0;

	double leftHandPosX = -50.0;
	double leftHandPosY = -50.0;
	double realLeftHandPosX = -50.0;
	double realLeftHandPosY = -50.0;

	public PondExperience() {
		pane = new StackPane();
		canvas = new Pane();

		Image backImg = new Image("media/background1600_1000.jpg", 1600, 1000,
				true, true);
		ImageView backView = new ImageView(backImg);
		backView.setPreserveRatio(true);
		pane.getChildren().add(backView);

		sleepTimer = new Timeline(new KeyFrame(Duration.millis(15000),
				ae -> goToMainMenu()));

		Image palmRightNormal = new Image("media/palmRight.png", 100, 100,
				true, true);
		rightHand = new ImageView(palmRightNormal);

		Image palmLeftNormal = new Image("media/palmLeft.png", 100, 100, true,
				true);
		leftHand = new ImageView(palmLeftNormal);

		drawHands = new AnimationTimer() {
			@Override
			public void handle(long now) {
				rightHand.setTranslateX(rightHandPosX);
				rightHand.setTranslateY(rightHandPosY);
				leftHand.setTranslateX(leftHandPosX);
				leftHand.setTranslateY(leftHandPosY);
			}
		};

		/* PLACE HOLDER STUFF */

		// Text t = new Text("POND EXPERIENCE");
		//
		// t.setFont(Font.font("Avenir Next", 30));
		// t.setFill(Color.WHITE);
		//
		// BorderPane p = new BorderPane();
		// p.setCenter(t);
		// pane.getChildren().add(p);

		/* PLACE HOLDER STUFF DONE */

		canvas.getChildren().addAll(rightHand, leftHand);

		rightHand.relocate(rightHandPosX, rightHandPosY);
		leftHand.relocate(leftHandPosX, leftHandPosY);

		circles = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			Circle circl = new Circle(-50, -50, 100, null);
			circl.setStroke(Color.rgb(200, 200, 255));
			circles.add(circl);
		}

		drawCircles = new AnimationTimer() {
			@Override
			public void handle(long arg0) {
				circles.get(circleNumber).setLayoutX(rightHandPosX);
				circles.get(circleNumber).setLayoutY(rightHandPosY);

				circleAnimation = new Timeline(
						new KeyFrame(Duration.ZERO, new KeyValue(circles.get(
								circleNumber).radiusProperty(), 0)),
						new KeyFrame(Duration.seconds(1), new KeyValue(circles
								.get(circleNumber).opacityProperty(), 1)),
						new KeyFrame(Duration.seconds(3), new KeyValue(circles
								.get(circleNumber).radiusProperty(), 500)),
						new KeyFrame(Duration.seconds(3), new KeyValue(circles
								.get(circleNumber).opacityProperty(), 0)));

				circleAnimation.play();
			}
		};

		canvas.getChildren().addAll(circles);

		pane.getChildren().add(canvas);

	}

	@Override
	public void setParent(ExperienceController controller) {
		myController = controller;
	}

	@Override
	public void startExperience() {
		drawHands.start();
		drawCircles.start();
		sleepTimer.play();
		controller = new Controller(this);
	}

	@Override
	public void stopExperience() {
		right = null;
		left = null;

		rightHandPosX = -50.0;
		rightHandPosY = -50.0;

		leftHandPosX = -50.0;
		leftHandPosY = -50.0;
		drawHands.stop();
		drawCircles.stop();
		sleepTimer.stop();
	}

	@Override
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

	public void onFrame(Controller controller) {
		Frame frame = controller.frame();

		HandList hands = frame.hands();

		rightHandPosX = -50.0;
		rightHandPosY = -50.0;
		realRightHandPosX = -50.0;
		realRightHandPosY = -50.0;

		leftHandPosX = -50.0;
		leftHandPosY = -50.0;
		realLeftHandPosX = -50.0;
		realLeftHandPosY = -50.0;

		sleepTimer.play();

		if (circleNumber >= 49)
			circleNumber = 0;
		else
			circleNumber++;

		for (int i = 0; i < hands.count(); i++) {
			sleepTimer.stop();

			if (hands.get(i).isRight()) {
				right = hands.get(i);
				rightHandPosX = Util.palmXToPanelX(right);
				rightHandPosY = Util.palmYToPanelY(right);
				realRightHandPosX = right.palmPosition().getX();
				realRightHandPosY = right.palmPosition().getY();
			} else if (hands.get(i).isLeft()) {
				left = hands.get(i);
				leftHandPosX = Util.palmXToPanelX(left);
				leftHandPosY = Util.palmYToPanelY(left);
				realLeftHandPosX = left.palmPosition().getX();
				realLeftHandPosY = left.palmPosition().getY();
			}
		}
	}
}
