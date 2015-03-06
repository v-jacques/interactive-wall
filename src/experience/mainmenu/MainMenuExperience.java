package experience.mainmenu;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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

public class MainMenuExperience extends Listener implements Experience {
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
	AnimationTimer drawIcons;

	double rightHandPosX = -50.0;
	double rightHandPosY = -50.0;

	double leftHandPosX = -50.0;
	double leftHandPosY = -50.0;

	public MainMenuExperience() {
		pane = new StackPane();
		canvas = new Pane();

		Image backImg = new Image("media/background1600_1000.jpg", 1600, 1000,
				true, true);
		ImageView backView = new ImageView(backImg);
		backView.setPreserveRatio(true);
		pane.getChildren().add(backView);

		Image blockImg = new Image("media/BlockMenu486_289.png", 486, 289,
				true, true);
		ImageView blockView = new ImageView(blockImg);
		blockView.setLayoutX(270);
		blockView.setLayoutY(147);
		canvas.getChildren().add(blockView);

		Image fireworkImg = new Image("media/FireworkMenu486_289.png", 486,
				289, true, true);
		ImageView fireworkView = new ImageView(fireworkImg);
		fireworkView.setLayoutX(843);
		fireworkView.setLayoutY(147);
		canvas.getChildren().add(fireworkView);

		Image galleryImg = new Image("media/GalleryMenu486_289.png", 486, 289,
				true, true);
		ImageView galleryView = new ImageView(galleryImg);
		galleryView.setLayoutX(270);
		galleryView.setLayoutY(509);
		canvas.getChildren().add(galleryView);

		Image pondImg = new Image("media/PondMenu486_289.png", 486, 289, true,
				true);
		ImageView pondView = new ImageView(pondImg);
		pondView.setLayoutX(843);
		pondView.setLayoutY(509);
		canvas.getChildren().add(pondView);

		sleepTimer = new Timeline(new KeyFrame(Duration.millis(15000),
				ae -> goToSleepMode()));

		rightHand = new ImageView(new Image("media/palmRight.png", 50, 50,
				true, true));
		leftHand = new ImageView(new Image("media/palmLeft.png", 50, 50, true,
				true));

		drawHands = new AnimationTimer() {
			@Override
			public void handle(long now) {
				rightHand.setTranslateX(rightHandPosX);
				rightHand.setTranslateY(rightHandPosY);
				leftHand.setTranslateX(leftHandPosX);
				leftHand.setTranslateY(leftHandPosY);
			}
		};

		Image blockkHover = new Image("media/BlockMenuHovered486_289.png", 486,
				289, true, true);
		Image fireworkHover = new Image("media/FireworkMenuHovered486_289.png",
				486, 289, true, true);
		Image galleryHover = new Image("media/GalleryMenuHovered486_289.png",
				486, 289, true, true);
		Image pondHover = new Image("media/PondMenuHovered486_289.png", 486,
				289, true, true);

		drawIcons = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (Util.isBetween(270, 270 + 486, (int) rightHandPosX)
						&& Util.isBetween(147, 147 + 289, (int) rightHandPosX))
					blockView.setImage(blockkHover);
				else
					blockView.setImage(blockImg);

				if (Util.isBetween(843, 843 + 486, (int) rightHandPosX)
						&& Util.isBetween(147, 147 + 289, (int) rightHandPosY))
					fireworkView.setImage(fireworkHover);
				else
					fireworkView.setImage(fireworkImg);

				if (Util.isBetween(270, 270 + 486, (int) rightHandPosX)
						&& Util.isBetween(509, 509 + 289, (int) rightHandPosY))
					galleryView.setImage(galleryHover);
				else
					galleryView.setImage(galleryImg);

				if (Util.isBetween(843, 843 + 486, (int) rightHandPosX)
						&& Util.isBetween(509, 509 + 289, (int) rightHandPosX))
					pondView.setImage(pondHover);
				else
					pondView.setImage(pondImg);
			}
		};

		canvas.getChildren().addAll(rightHand, leftHand);

		rightHand.relocate(rightHandPosX, rightHandPosY);
		leftHand.relocate(leftHandPosX, leftHandPosY);

		pane.getChildren().add(canvas);
	}

	@Override
	public void setParent(ExperienceController controller) {
		myController = controller;
	}

	@Override
	public void startExperience() {
		drawHands.start();
		drawIcons.start();
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
		drawIcons.stop();
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

	public void onFrame(Controller controller) {
		Frame frame = controller.frame();

		HandList hands = frame.hands();

		rightHandPosX = -50.0;
		rightHandPosY = -50.0;

		leftHandPosX = -50.0;
		leftHandPosY = -50.0;

		sleepTimer.play();

		for (int i = 0; i < hands.count(); i++) {
			sleepTimer.stop();

			if (hands.get(i).isRight()) {
				right = hands.get(i);
				rightHandPosX = Util.palmXToPanelX(right, pane);
				rightHandPosY = Util.palmYToPanelY(right, pane);
			} else if (hands.get(i).isLeft()) {
				left = hands.get(i);
				leftHandPosX = Util.palmXToPanelX(left, pane);
				leftHandPosY = Util.palmYToPanelY(left, pane);
			}
		}
	}
}
