package experience.pond;

import java.io.File;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import main.Experience;
import main.ExperienceController;
import main.InteractiveWall;
import main.Util;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
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

	double rightHandPosX = -50.0;
	double rightHandPosY = -50.0;
	double realRightHandPosX = -50.0;
	double realRightHandPosY = -50.0;

	double leftHandPosX = -50.0;
	double leftHandPosY = -50.0;
	double realLeftHandPosX = -50.0;
	double realLeftHandPosY = -50.0;

	private int original[], water[];
	private short waterMap[];
	private int width, height, halfWidth, halfHeight, size;
	private int oldInd, newInd, mapInd;

	private AnimationTimer timer;
	private long lastTimerCall;

	private final BooleanProperty drop = new SimpleBooleanProperty(false);
	protected final ObservableList<Point3D> points = FXCollections
			.observableArrayList();

	Timeline rightHandChange;
	Timeline leftHandChange;
	MediaPlayer countPing;
	MediaPlayer confirmComplete;
	private File count = new File("src/media/countPing.mp3");
	private final String COUNT_URL = count.toURI().toString();
	private File confirm = new File("src/media/confirmComplete.mp3");
	private final String CONFIRM_URL = confirm.toURI().toString();

	public PondExperience() {
		pane = new StackPane();
		canvas = new Pane();

		Image backImg = new Image("media/TaiChi1600_1000.jpg", 1600, 1000,
				true, true);
		ImageView backView = new ImageView(backImg);
		backView.setPreserveRatio(true);
		backView.setLayoutY(300); // DO NOT LIKE THIS
		pane.getChildren().add(backView);

		Image exitImg = new Image("media/Exit180_180.png", 150, 150, true, true);
		Image exitHoveredImg = new Image("media/ExitHovered180_180.png", 150,
				150, true, true);
		ImageView exitView = new ImageView(exitImg);
		exitView.setOpacity(0.3);
		exitView.setPreserveRatio(true);
		exitView.setLayoutX(1335);
		exitView.setLayoutY(710);
		canvas.getChildren().add(exitView);

		width = (int) backView.getImage().getWidth();
		height = (int) backView.getImage().getHeight();
		halfWidth = width >> 1;
		halfHeight = height >> 1;
		size = width * (height + 2) * 2;
		waterMap = new short[size];
		water = new int[width * height];
		original = new int[width * height];
		oldInd = width;
		newInd = width * (height + 3);
		PixelReader pixelReader = backView.getImage().getPixelReader();
		pixelReader.getPixels(0, 0, width, height,
				WritablePixelFormat.getIntArgbInstance(), original, 0, width);

		lastTimerCall = System.nanoTime();
		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (now > lastTimerCall + 30_000_000l) {
					backView.setImage(applyEffect());
					lastTimerCall = now;
				}
			}
		};

		sleepTimer = new Timeline(new KeyFrame(Duration.millis(20000),
				ae -> goToMainMenu()));

		Image palmRightNormal = new Image("media/palmRight.png", 100, 100,
				true, true);
		rightHand = new ImageView(palmRightNormal);
		rightHand.setVisible(false);

		Image rightHandFull = new Image("media/Hold_fullHand_102_107.png", 100,
				100, true, true);

		Image palmLeftNormal = new Image("media/palmLeft.png", 100, 100, true,
				true);
		leftHand = new ImageView(palmLeftNormal);
		leftHand.setVisible(false);

		Image leftHandFull = new Image("media/HoldLeft_fullHand_102_107.png",
				100, 100, true, true);

		Media countMedia = new Media(COUNT_URL);
		countPing = new MediaPlayer(countMedia);
		countPing.setVolume(.25);

		Media confirmMedia = new Media(CONFIRM_URL);
		confirmComplete = new MediaPlayer(confirmMedia);
		confirmComplete.setVolume(.25);

		rightHandChange = new Timeline(new KeyFrame(Duration.seconds(.5),
				ae -> {
					rightHand.setImage(rightHandFull);
					confirmComplete.play();
				}), new KeyFrame(Duration.seconds(1), ae -> {
			goToMainMenu();
		}), new KeyFrame(Duration.seconds(2), ae -> {
			rightHand.setImage(palmRightNormal);
		}));

		leftHandChange = new Timeline(new KeyFrame(Duration.seconds(.5),
				ae -> {
					leftHand.setImage(leftHandFull);
					confirmComplete.play();
				}), new KeyFrame(Duration.seconds(1), ae -> {
			goToMainMenu();
		}), new KeyFrame(Duration.seconds(2), ae -> {
			leftHand.setImage(palmLeftNormal);
		}));

		drawHands = new AnimationTimer() {
			@Override
			public void handle(long now) {
				rightHand.setTranslateX(rightHandPosX);
				rightHand.setTranslateY(rightHandPosY);
				leftHand.setTranslateX(leftHandPosX);
				leftHand.setTranslateY(leftHandPosY);

				if (Util.isBetween(1235, 1550, (int) rightHandPosX)
						&& Util.isBetween(610, 960, (int) rightHandPosY)) {
					rightHand.setVisible(true);
					if (Util.isBetween(1335, 1450, (int) rightHandPosX)
							&& Util.isBetween(710, 860, (int) rightHandPosY)) {
						exitView.setImage(exitHoveredImg);
						exitView.setOpacity(1.0);
						rightHandChange.play();
					}
				} else if (Util.isBetween(1235, 1550, (int) leftHandPosX)
						&& Util.isBetween(610, 960, (int) leftHandPosY)) {
					leftHand.setVisible(true);
					if (Util.isBetween(1335, 1450, (int) leftHandPosX)
							&& Util.isBetween(710, 860, (int) leftHandPosY)) {
						exitView.setImage(exitHoveredImg);
						exitView.setOpacity(1.0);
						leftHandChange.play();
					}
				} else {
					exitView.setImage(exitImg);
					exitView.setOpacity(0.3);

					rightHand.setVisible(false);
					rightHandChange.stop();
					rightHand.setImage(palmRightNormal);

					leftHand.setVisible(false);
					leftHandChange.stop();
					leftHand.setImage(palmLeftNormal);
				}
			}
		};

		canvas.getChildren().addAll(rightHand, leftHand);

		rightHand.relocate(rightHandPosX, rightHandPosY);
		leftHand.relocate(leftHandPosX, leftHandPosY);

		pane.getChildren().add(canvas);

		isDrop().addListener(
				(ov, b, b1) -> {
					if (b1) {
						Platform.runLater(() -> {
							for (int i = 0; i < points.size(); i++) {
								Point3D t1 = points.get(i);
								Point2D d = canvas.sceneToLocal(t1.getX(),
										t1.getY());
								double dx = d.getX(), dy = d.getY();

								if (dx >= 0d && dx <= canvas.getWidth()
										&& dy >= 0d && dy <= canvas.getHeight()) {
									waterDrop((int) dx, (int) dy, 10);
								}
							}
						});
					}
				});
	}

	@Override
	public void setParent(ExperienceController controller) {
		myController = controller;
	}

	@Override
	public void startExperience() {
		drawHands.start();
		sleepTimer.play();
		timer.start();
		PondMusic.playBackgroundMusic();
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
		sleepTimer.stop();
		timer.stop();
		PondMusic.pauseBackgroundMusic();
	}

	@Override
	public Node getNode() {
		return pane;
	}

	private void goToMainMenu() {
		stopExperience();
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

		drop.set(false);
		points.clear();
		for (Finger finger : frame.fingers()) {
			// getZ is being used to only capture pointing fingers
			if (finger.isValid() && finger.tipPosition().getZ() < 25) {
				// System.out.println(finger.tipPosition().getX());
				Point3D p = new Point3D(Util.leapXtoPanelX(finger
						.stabilizedTipPosition().getX()),
						Util.leapYToPanelY(finger.stabilizedTipPosition()
								.getY()), finger.stabilizedTipPosition().getZ());
				points.add(p);
			}
		}
		drop.set(!points.isEmpty());
	}

	public void waterDrop(int dx, int dy, int rad) {
		for (int j = dy - rad; j < dy + rad; j++) {
			for (int k = dx - rad; k < dx + rad; k++) {
				if (j >= 0 && j < height && k >= 0 && k < width) {
					waterMap[oldInd + (j * width) + k] += 128;
				}
			}
		}
	}

	private Image applyEffect() {
		int a, b, i = oldInd;
		oldInd = newInd;
		newInd = i;
		i = 0;
		mapInd = oldInd;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				short data = (short) ((waterMap[mapInd - width]
						+ waterMap[mapInd + width] + waterMap[mapInd - 1] + waterMap[mapInd + 1]) >> 1);
				data -= waterMap[newInd + i];
				// ripple effect size
				data -= data >> 5;
				waterMap[newInd + i] = data;
				data = (short) (1024 - data);
				a = ((x - halfWidth) * data / 1024) + halfWidth;
				if (a >= width) {
					a = width - 1;
				}
				if (a < 0) {
					a = 0;
				}
				b = ((y - halfHeight) * data / 1024) + halfHeight;
				if (b >= height) {
					b = height - 1;
				}
				if (b < 0) {
					b = 0;
				}
				water[i++] = original[a + (b * width)];
				mapInd++;
			}
		}
		WritableImage raster = new WritableImage(width, height);
		PixelWriter pixelWriter = raster.getPixelWriter();
		pixelWriter.setPixels(0, 0, width, height,
				PixelFormat.getIntArgbInstance(), water, 0, width);
		return raster;
	}

	public ObservableValue<Boolean> isDrop() {
		return drop;
	}
}
