package experience.pond;

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
import com.leapmotion.leap.Screen;
import com.leapmotion.leap.Vector;

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

	public PondExperience() {
		pane = new StackPane();
		canvas = new Pane();

		Image backImg = new Image("media/pond1600-1000px-unfinished.jpg", 1600,
				1000, true, true);
		ImageView backView = new ImageView(backImg);

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

		backView.setPreserveRatio(true);
		pane.getChildren().add(backView);

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
								double dx = d.getX(), dy = d.getY(), dz = Math
										.abs(t1.getZ());
								int rad = (dz < 50) ? 5 : ((dz < 150) ? 4 : 3);
								if (dx >= 0d && dx <= canvas.getWidth()
										&& dy >= 0d && dy <= canvas.getHeight()) {
									waterDrop((int) dx, (int) dy, rad);
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
	}

	@Override
	public Node getNode() {
		return pane;
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

		Screen screen = controller.locatedScreens().get(0);
		drop.set(false);
		points.clear();
		for (Finger finger : frame.fingers()) {
			if (finger.isValid()) {
				Vector inter = screen.intersect(finger.tipPosition(),
						finger.direction(), true);
				Point3D p = new Point3D(screen.widthPixels()
						* Math.min(1d, Math.max(0d, inter.getX())),
						screen.heightPixels()
								* Math.min(1d,
										Math.max(0d, (1d - inter.getY()))),
						finger.tipPosition().getZ());
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
				data -= data >> 4;
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
