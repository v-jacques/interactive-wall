package experience.mainmenu;

import java.io.File;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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

public class MainMenuExperience extends Listener implements Experience {
	Controller controller;
	ExperienceController myController;
	StackPane pane;
	Pane canvas;
	Timeline sleepTimer;
	Timeline rightHandChange;
	String nextExperience = "";
	Hand right;
	Hand left;
	ImageView rightHand;
	ImageView leftHand;
	AnimationTimer drawHands;
	AnimationTimer animationText;
	AnimationTimer drawIcons;

	MediaPlayer countPing;
	MediaPlayer confirmComplete;
	private File count = new File("src/media/countPing.mp3");
	private final String COUNT_URL = count.toURI().toString();

	private File confirm = new File("src/media/confirmComplete.mp3");
	private final String CONFIRM_URL = confirm.toURI().toString();

	double rightHandPosX = -100.0;
	double rightHandPosY = -100.0;
	double realRightHandPosX = -100.0;
	double realRightHandPosY = -100.0;

	double leftHandPosX = -100.0;
	double leftHandPosY = -100.0;
	double realLeftHandPosX = -100.0;
	double realLeftHandPosY = -100.0;

	public MainMenuExperience() {
		pane = new StackPane();
		canvas = new Pane();

		Media countMedia = new Media(COUNT_URL);
		countPing = new MediaPlayer(countMedia);
		countPing.setVolume(.25);

		Media confirmMedia = new Media(CONFIRM_URL);
		confirmComplete = new MediaPlayer(confirmMedia);
		confirmComplete.setVolume(.25);

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

		Image palmRightNormal = new Image("media/palmRight.png", 100, 100,
				true, true);
		rightHand = new ImageView(palmRightNormal);

		Image palmLeftNormal = new Image("media/palmLeft.png", 100, 100, true,
				true);
		leftHand = new ImageView(palmLeftNormal);

		// RIGHT Hand 2 1 0 Images
		Image rightHand0 = new Image("media/Hold_0s_102_107.png", 100, 100,
				true, true);
		Image rightHand1 = new Image("media/Hold_1s_102_107.png", 100, 100,
				true, true);
		Image rightHand2 = new Image("media/Hold_2s_102_107.png", 100, 100,
				true, true);
		Image rightHandFull = new Image("media/Hold_fullHand_102_107.png", 100,
				100, true, true);

		rightHandChange = new Timeline(new KeyFrame(Duration.seconds(1),
				ae -> {
					countPing.stop();
					countPing.play();
				}, new KeyValue(rightHand.imageProperty(), rightHand2)),
				new KeyFrame(Duration.seconds(1.5), ae -> {
					countPing.stop();
					countPing.play();
				}, new KeyValue(rightHand.imageProperty(), rightHand1)),
				new KeyFrame(Duration.seconds(2), ae -> {
					countPing.stop();
					countPing.play();
				}, new KeyValue(rightHand.imageProperty(), rightHand0)),
				new KeyFrame(Duration.seconds(2.5), ae -> {
					goToNextExperience();
					confirmComplete.stop();
					confirmComplete.play();
				}, new KeyValue(rightHand.imageProperty(), rightHandFull)));

		drawHands = new AnimationTimer() {
			@Override
			public void handle(long now) {
				rightHand.setTranslateX(rightHandPosX);
				rightHand.setTranslateY(rightHandPosY);
				leftHand.setTranslateX(leftHandPosX);
				leftHand.setTranslateY(leftHandPosY);
			}
		};

		/*
		 * Text t = new Text ("screenX" + leftHandPosX); t.setX(150.0);
		 * t.setY(289.0); Text t2 = new Text ("screenY" + leftHandPosY);
		 * t2.setX(150.0); t2.setY(300.0); Text t3 = new Text ("palmX" +
		 * realLeftHandPosX); t3.setX(150.0); t3.setY(311.0); Text t4 = new Text
		 * ("palmY" + realLeftHandPosY); t4.setX(150.0); t4.setY(322.0);
		 * 
		 * canvas.getChildren().addAll(t, t2); canvas.getChildren().addAll(t3,
		 * t4);
		 * 
		 * animationText = new AnimationTimer() {
		 * 
		 * @Override public void handle(long now){ t.setText("screenX" +
		 * leftHandPosX); t2.setText("screenY" + leftHandPosY);
		 * t3.setText("palmX" + realLeftHandPosX); t4.setText("palmY" +
		 * realLeftHandPosY); } };
		 */

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
				boolean inABox = false;

				if (Util.isBetween(270, 270 + 486, (int) rightHandPosX)
						&& Util.isBetween(147, 147 + 289, (int) rightHandPosY)) {
					inABox = true;
					rightHandChange.play();
					nextExperience = InteractiveWall.BLOCK;
					blockView.setImage(blockkHover);
				} else {
					blockView.setImage(blockImg);
				}

				if (Util.isBetween(843, 843 + 486, (int) rightHandPosX)
						&& Util.isBetween(147, 147 + 289, (int) rightHandPosY)) {
					inABox = true;
					rightHandChange.play();
					nextExperience = InteractiveWall.FIREWORK;
					fireworkView.setImage(fireworkHover);
				} else {
					fireworkView.setImage(fireworkImg);
				}

				if (Util.isBetween(270, 270 + 486, (int) rightHandPosX)
						&& Util.isBetween(509, 509 + 289, (int) rightHandPosY)) {
					inABox = true;
					rightHandChange.play();
					nextExperience = InteractiveWall.GALLERY;
					galleryView.setImage(galleryHover);
				} else {
					galleryView.setImage(galleryImg);
				}

				if (Util.isBetween(843, 843 + 486, (int) rightHandPosX)
						&& Util.isBetween(509, 509 + 289, (int) rightHandPosY)) {
					inABox = true;
					rightHandChange.play();
					nextExperience = InteractiveWall.POND;
					pondView.setImage(pondHover);
				} else {
					pondView.setImage(pondImg);
				}

				if (!inABox) {
					nextExperience = "";
					rightHandChange.stop();
					rightHand.setImage(palmRightNormal);
				}

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
		confirmComplete.stop();
		countPing.stop();
		drawHands.start();
		sleepTimer.play();
		// animationText.start();
		drawIcons.start();
		sleepTimer.play();
		SystemMusic.playBackgroundMusic();
		controller = new Controller(this);
	}

	@Override
	public void stopExperience() {
		right = null;
		left = null;

		rightHandPosX = -100.0;
		rightHandPosY = -100.0;

		leftHandPosX = -100.0;
		leftHandPosY = -100.0;
		rightHandChange.stop();
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

	private void goToNextExperience() {
		controller.removeListener(this);
		SystemMusic.pauseBackgroundMusic();
		if (!nextExperience.isEmpty()) {
			myController.setExperience(nextExperience);
		} else {
			this.stopExperience();
			this.startExperience();
		}
	}

	public void onFrame(Controller controller) {
		Frame frame = controller.frame();

		HandList hands = frame.hands();

		rightHandPosX = -100.0;
		rightHandPosY = -100.0;
		realRightHandPosX = -100.0;
		realRightHandPosY = -100.0;

		leftHandPosX = -100.0;
		leftHandPosY = -100.0;
		realLeftHandPosX = -100.0;
		realLeftHandPosY = -100.0;

		sleepTimer.play();

		for (int i = 0; i < hands.count(); i++) {
			sleepTimer.stop();

			if (hands.get(i).isRight()) {
				right = hands.get(i);
				rightHandPosX = Util.leapXtoPanelX(right
						.stabilizedPalmPosition().getX());
				rightHandPosY = Util.leapYToPanelY(right
						.stabilizedPalmPosition().getY());
				realRightHandPosX = right.palmPosition().getX();
				realRightHandPosY = right.palmPosition().getY();

			} else if (hands.get(i).isLeft()) {
				left = hands.get(i);
				leftHandPosX = Util.leapXtoPanelX(left.stabilizedPalmPosition()
						.getX());
				leftHandPosY = Util.leapYToPanelY(left.stabilizedPalmPosition()
						.getY());
				realLeftHandPosX = left.palmPosition().getX();
				realLeftHandPosY = left.palmPosition().getY();
			}
		}
	}
}