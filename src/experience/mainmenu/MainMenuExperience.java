package experience.mainmenu;

import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
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
        AnimationTimer animationText;

	double rightHandPosX = -50.0;
	double rightHandPosY = -50.0;
        double realRightHandPosX = -50.0;
        double realRightHandPosY = -50.0;

	double leftHandPosX = -50.0;
	double leftHandPosY = -50.0;
        double realLeftHandPosX = -50.0;
        double realLeftHandPosY = -50.0;

	public MainMenuExperience() {
		pane = new StackPane();
		canvas = new Pane();

		Image backImg = new Image("media/background1600_1000.jpg", 1600, 1000,
				false, false);
		ImageView backView = new ImageView(backImg);
		backView.setPreserveRatio(true);
		pane.getChildren().add(backView);

		Image blockImg = new Image("media/BlockMenu486_289.png", 486, 289,
				false, false);
		ImageView blockView = new ImageView(blockImg);
		blockView.setLayoutX(270);
		blockView.setLayoutY(147);
		canvas.getChildren().add(blockView);

		Image fireworkImg = new Image("media/FireworkMenu486_289.png", 486,
				289, false, false);
		ImageView fireworkView = new ImageView(fireworkImg);
		fireworkView.setLayoutX(843);
		fireworkView.setLayoutY(147);
		canvas.getChildren().add(fireworkView);

		Image galleryImg = new Image("media/GalleryMenu486_289.png", 486, 289,
				false, false);
		ImageView galleryView = new ImageView(galleryImg);
		galleryView.setLayoutX(270);
		galleryView.setLayoutY(509);
		canvas.getChildren().add(galleryView);

		Image pondImg = new Image("media/PondMenu486_289.png", 486, 289, false,
				false);
		ImageView pondView = new ImageView(pondImg);
		pondView.setLayoutX(843);
		pondView.setLayoutY(509);
		canvas.getChildren().add(pondView);

		// sleepTimer = new Timeline(new KeyFrame(Duration.millis(15000),
		// ae -> goToSleepMode()));
                                
		rightHand = new ImageView(new Image("media/palmRight.png", 100, 100,
				true, true));
		leftHand = new ImageView(new Image("media/palmLeft.png", 100, 100, true,
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
                
                Text t = new Text ("screenX" + leftHandPosX);
                t.setX(150.0);
                t.setY(289.0);
                Text t2 = new Text ("screenY" + leftHandPosY);
                t2.setX(150.0);
                t2.setY(300.0);
                Text t3 = new Text ("palmX" + realLeftHandPosX);
                t3.setX(150.0);
                t3.setY(311.0);
                Text t4 = new Text ("palmY" + realLeftHandPosY);
                t4.setX(150.0);
                t4.setY(322.0);
                
                canvas.getChildren().addAll(t, t2);
                canvas.getChildren().addAll(t3, t4);
                
                animationText = new AnimationTimer() {
                    @Override
                    public void handle(long now){
                        t.setText("screenX" + leftHandPosX);
                        t2.setText("screenY" + leftHandPosY);
                        t3.setText("palmX" + realLeftHandPosX);
                        t4.setText("palmY" + realLeftHandPosY);
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
		// sleepTimer.play();
                animationText.start();
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
                realRightHandPosX = -50.0;
                realRightHandPosY = -50.0;
               

		leftHandPosX = -50.0;
		leftHandPosY = -50.0;
                realLeftHandPosX = -50.0;
                realLeftHandPosY = -50.0;        


		for (int i = 0; i < hands.count(); i++) {
			if (hands.get(i).isRight()) {
				right = hands.get(i);
				rightHandPosX = Util.palmXToPanelX(right, pane);
				rightHandPosY = Util.palmYToPanelY(right, pane);
                                realRightHandPosX = right.palmPosition().getX();
                                realRightHandPosY = right.palmPosition().getY();
                                        
			} else if (hands.get(i).isLeft()) {
				left = hands.get(i);
				leftHandPosX = Util.palmXToPanelX(left, pane);
				leftHandPosY = Util.palmYToPanelY(left, pane);
                                realLeftHandPosX = left.palmPosition().getX();
                                realLeftHandPosY = left.palmPosition().getY();
			}
		}                
	}
}
