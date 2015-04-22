package experience.gallery;

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
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.SwipeGesture;
import com.leapmotion.leap.Vector;

public class GalleryExperience extends Listener implements Experience {
	Controller controller;
	ExperienceController myController;
	StackPane pane;
	Pane canvas;
        Pane quote;
	Timeline sleepTimer;
	Hand right;
	Hand left;
	ImageView rightHand;
	ImageView leftHand;
	AnimationTimer drawHands;
        AnimationTimer changeImgs;
        AnimationTimer information;

	double rightHandPosX = -50.0;
	double rightHandPosY = -50.0;
	double realRightHandPosX = -50.0;
	double realRightHandPosY = -50.0;

	double leftHandPosX = -50.0;
	double leftHandPosY = -50.0;
	double realLeftHandPosX = -50.0;
	double realLeftHandPosY = -50.0;
        
        long lastSwipe = 0;
        
        boolean change = false;
        int imageHolder = 1;
        boolean direction;
                
        String[] imgs = { 
        "media/A Calm at a Mediterranean Port.jpg",
        "media/Classical Landscape with Figures and Sculpture.jpg",
        "media/Irises.jpg",
        "media/Mountain Landscape with road to Naples.jpg",
        "media/Italian Landscape (Site d'Italie, Soleil Levant).jpg",
        "media/Landscape with Lake and Boatman.jpg",
        "media/O.T. 2013.jpg",
        "media/Renee Levi 640_341.jpg",
        "media/Starry Night.jpg",
        "media/The Wounded Foot.jpg",
        "media/View of the Bridge and Part of the Town of Cava, Kingdom of Naples.jpg",
        "media/Water Lilies.jpg",
        "media/Wooded River Landscape in the Alps.jpg",
        "media/Allison_Steel_-_Seeking_Solace.jpg",
        "media/ontario.jpg",
        "media/infinite.jpg",
        "media/clangone.jpg"   };
        
        Image leftImg = new Image(imgs[imageHolder-1], 800, 500,
                        false, false);
        ImageView leftView = new ImageView(leftImg);

        Image mainImg = new Image(imgs[imageHolder], 900, 600, 
                        true, true);
        ImageView mainView = new ImageView(mainImg);

        Image rightImg = new Image(imgs[imageHolder+1], 800, 500,
                        false, false);
        ImageView rightView = new ImageView(rightImg);

	public GalleryExperience() {
		pane = new StackPane();
		canvas = new Pane();
                quote = new Pane();
                quote.setVisible(false);

		Image backImg = new Image("media/background1600_1000.jpg", 1600, 1000,
				true, true);
		ImageView backView = new ImageView(backImg);
		backView.setPreserveRatio(true);
		pane.getChildren().add(backView);
                
                leftView.setLayoutX(-600);
                leftView.setLayoutY(250);
                canvas.getChildren().add(leftView);
                
                mainView.setLayoutX((1600-mainImg.getWidth())/2);
                mainView.setLayoutY((1000-mainImg.getHeight())/2);
                canvas.getChildren().add(mainView);
                
                rightView.setLayoutX(1400);
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
                
                changeImgs = new AnimationTimer() {
                        @Override
			public void handle(long now){                            
                            changeImg();
			}
                };
                
                information = new AnimationTimer() {
                        @Override
                        public void handle(long now){ 
                                if (Util.isBetween((int)(1600-mainImg.getWidth())/2, (int)(1600-mainImg.getWidth())/2 
                                                + (int)mainImg.getWidth(), (int) rightHandPosX)
						&& Util.isBetween((int)(1000-mainImg.getHeight())/2, 
                                                (int)(1000-mainImg.getHeight())/2 + (int)mainImg.getHeight(), (int) rightHandPosY)
                                                ) {
					//mainView.setImage(mainHover);
                                } else {
					mainView.setImage(mainImg);
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
        
        public void changeImg(){
                if (direction){
                    if(imageHolder==(imgs.length - 2)){
                        Image newLeft = new Image(imgs[imageHolder], 800, 500,
                                false, false);
                        Image newMain = new Image(imgs[imageHolder+1], 900, 600,
                                true, true);
                        Image newRight = new Image(imgs[0], 800, 500,
                                false, false);
                        leftView.setImage(newLeft);
                        mainView.setImage(newMain);
                        mainView.setLayoutX((1600-newMain.getWidth())/2);
                        mainView.setLayoutY((1000-newMain.getHeight())/2);
                        rightView.setImage(newRight);
                        imageHolder++;
                    }
                    else if(imageHolder==(imgs.length - 1)){
                        Image newLeft = new Image(imgs[imageHolder], 800, 500,
                                false, false);
                        Image newMain = new Image(imgs[0], 900, 600,
                                true, true);
                        Image newRight = new Image(imgs[1], 800, 500,
                                false, false);
                        leftView.setImage(newLeft);
                        mainView.setImage(newMain);
                        mainView.setLayoutX((1600-newMain.getWidth())/2);
                        mainView.setLayoutY((1000-newMain.getHeight())/2);
                        rightView.setImage(newRight);
                        imageHolder = 0;
                    }
                    else{
                        Image newLeft = new Image(imgs[imageHolder], 800, 500,
                                        false, false);
                        Image newMain = new Image(imgs[imageHolder+1], 900, 600,
                                        true, true);
                        Image newRight = new Image(imgs[imageHolder+2], 800, 500,
                                        false, false);
                        leftView.setImage(newLeft);
                        mainView.setImage(newMain);
                        mainView.setLayoutX((1600-newMain.getWidth())/2);
                        mainView.setLayoutY((1000-newMain.getHeight())/2);
                        rightView.setImage(newRight);
                        imageHolder++;
                    }
                }
                else{
                    if(imageHolder==1){
                        Image newLeft = new Image(imgs[imgs.length-1], 800, 500,
                                false, false);
                        Image newMain = new Image(imgs[imageHolder-1], 900, 600,
                                true, true);
                        Image newRight = new Image(imgs[imageHolder], 800, 500,
                                false, false);
                        leftView.setImage(newLeft);
                        mainView.setImage(newMain);
                        mainView.setLayoutX((1600-newMain.getWidth())/2);
                        mainView.setLayoutY((1000-newMain.getHeight())/2);
                        rightView.setImage(newRight);
                        imageHolder--;
                    }
                    else if(imageHolder==0){
                        Image newLeft = new Image(imgs[imgs.length-2], 800, 500,
                                false, false);
                        Image newMain = new Image(imgs[imgs.length-1], 900, 600,
                                true, true);
                        Image newRight = new Image(imgs[imageHolder], 800, 500,
                                false, false);
                        leftView.setImage(newLeft);
                        mainView.setImage(newMain);
                        mainView.setLayoutX((1600-newMain.getWidth())/2);
                        mainView.setLayoutY((1000-newMain.getHeight())/2);
                        rightView.setImage(newRight);
                        imageHolder = imgs.length-1;
                    }
                    else{
                        Image newLeft = new Image(imgs[imageHolder-2], 800, 500,
                                        false, false);
                        Image newMain = new Image(imgs[imageHolder-1], 900, 600,
                                        true, true);
                        Image newRight = new Image(imgs[imageHolder], 800, 500,
                                        false, false);
                        leftView.setImage(newLeft);
                        mainView.setImage(newMain);
                        mainView.setLayoutX((1600-newMain.getWidth())/2);
                        mainView.setLayoutY((1000-newMain.getHeight())/2);
                        rightView.setImage(newRight); 
                        imageHolder--;
                    }
                    
                }
                System.out.print(imageHolder + " ");
                changeImgs.stop();
        }
        
        public void onConnect(Controller controller){
                controller.enableGesture(Gesture.Type.TYPE_SWIPE);
                controller.config().setFloat("Gesture.Swipe.MinLength", 200.0f);
                controller.config().setFloat("Gesture.Swipe.MinVelocity", 1000f);
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
               
                GestureList gestures = frame.gestures();
                SwipeGesture swipe = new SwipeGesture(gestures.get(0));
                
                Vector swipeStart = swipe.startPosition();
                Vector swipeDir = swipe.direction();
                
                if(canvas.isVisible()){
                        if (swipe.isValid() && swipeStart.getX() > 130 && swipeDir.getX() < 0) {
                            direction = true;
                            switch (swipe.state()){
                                case STATE_STOP:
                                    changeImgs.start();
                            }
                            System.out.println(swipe.state());
                        }

                        else if(swipe.isValid() && swipeStart.getX() < -130 && swipeDir.getX() > 0){
                            direction = false;
                            switch (swipe.state()){
                                case STATE_STOP:
                                    changeImgs.start();
                            }
                            System.out.println(swipe.state());
                        }
                }
                if(quote.isVisible()){
                        if (swipe.isValid() && swipeStart.getY() > 130 && swipeDir.getY() < 0) {
                            direction = true;
                            switch (swipe.state()){
                                case STATE_STOP:
                                    changeImgs.start();
                            }
                            System.out.println(swipe.state());
                        }

                        else if(swipe.isValid() && swipeStart.getY() < -130 && swipeDir.getY() > 0){
                            direction = false;
                            switch (swipe.state()){
                                case STATE_STOP:
                                    changeImgs.start();
                            }
                            System.out.println(swipe.state());
                        }
                }

	}
}
