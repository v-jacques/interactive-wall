package experience.gallery;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.Experience;
import main.ExperienceController;
import main.InteractiveWall;
import main.Util;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.SwipeGesture;

public class GalleryExperience extends Listener implements Experience {
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
        
        String img1 = "media/A Calm at a Mediterranean Port.jpg";
        String img2 = "media/Classical Landscape with Figures and Sculpture.jpg";
        String img3 = "media/Irises.jpg";
        String img4 = "media/Mountain Landscape with road to Naples.jpg";
        String img5 = "media/Italian Landscape (Site d'Italie, Soleil Levant).jpg";
        String img6 = "media/Landscape with Lake and Boatman.jpg";
        String img7 = "media/O.T. 2013.jpg";
        String img8 = "media/Renee Levi 640_341.jpg";
        String img9 = "media/Starry Night.jpg";
        String img10 = "media/The Wounded Foot.jpg";
        String img11 = "media/View of the Bridge and Part of the Town of Cava, Kingdom of Naples.jpg";
        String img12 = "media/Water Lilies.jpg";
        String img13 = "media/Wooded River Landscape in the Alps.jpg";
        String holder = "";

	public GalleryExperience() {
		pane = new StackPane();
		canvas = new Pane();             

		Image backImg = new Image("media/background1600_1000.jpg", 1600, 1000,
				true, true);
		ImageView backView = new ImageView(backImg);
		backView.setPreserveRatio(true);
		pane.getChildren().add(backView);
                
                Image mainImg = new Image(img2, 900, 600,
                                true, true);
                ImageView mainView = new ImageView(mainImg);
                mainView.setLayoutX(400);
                mainView.setLayoutY(200);
                canvas.getChildren().add(mainView);
                
                Image leftImg = new Image(img1, 800, 500,
                                true, true);
                ImageView leftView = new ImageView(leftImg);
                leftView.setLayoutX(1400);
                leftView.setLayoutY(250);
                canvas.getChildren().add(leftView);
                
                Image rightImg = new Image(img3, 800, 500,
                                true, true);
                ImageView rightView = new ImageView(rightImg);
                rightView.setLayoutX(-450);
                rightView.setLayoutY(250);
                canvas.getChildren().add(rightView);

		sleepTimer = new Timeline(new KeyFrame(Duration.millis(5000),
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
	}

	@Override
	public void setParent(ExperienceController controller) {
		myController = controller;
	}

	@Override
	public void startExperience() {
		drawHands.start();
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
		sleepTimer.stop();
	}

	@Override
	public Node getNode() {
		return pane;
	}

	private void goToMainMenu() {
		controller.removeListener(this);
		myController.setExperience(InteractiveWall.MAIN_MENU);
	}
        
        public void onConnect(Controller controller){
                controller.enableGesture(Gesture.Type.TYPE_SWIPE);
                controller.config().setFloat("Gesture.Swipe.MinLength", 200.0f);
                controller.config().save();
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
                
                GestureList gestures = frame.gestures();
                SwipeGesture swipe = new SwipeGesture(gestures.get(0));
                if (swipe.isValid()){
                        holder = img1;
                        img1 = img2;
                        img2 = img3;
                        img3 = img4;
                        img4 = img5;
                        img5 = img6;
                        img6 = img7;
                        img7 = img8;
                        img8 = img9;
                        img9 = img10;
                        img10 = img11;
                        img11 = img12;
                        img12 = img13;
                        img13 = holder;
                        
                }
                
                
	}
}
