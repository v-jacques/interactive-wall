package experience.block;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Animation;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.geometry.Point2D;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.InnerShadow;
import main.Experience;
import main.ExperienceController;
import main.InteractiveWall;
import main.Util;

import java.io.File;

import javafx.scene.paint.*;
import javafx.scene.shape.*;

import org.jbox2d.dynamics.*;
import org.jbox2d.common.*;

import com.leapmotion.leap.*;

public class BlockExperience extends Listener implements Experience {
	Controller controller;
	ExperienceController myController;
	StackPane pane;
	Pane canvas;
	AnchorPane buttons;
	Group blocks;
	Timeline sleepTimer;
	Timeline exit;
	Timeline addBlock;
	Hand right;
	Hand left;
	Block block;
	Node selected;
	
	Image palmRightNormal;
	Image pointNormal;
	Image pointSelected;

	ImageView rightHand;
	ImageView leftHand;
	ImageView point;
	ImageView exitImage;
	ImageView addBlockImage;
	
	AnimationTimer drawHands;
	Timeline blockDrawer;
	File background;
	Media backgroundMusic;
	MediaPlayer backgroundMusicPlayer;
	
	MediaPlayer countPing;
	MediaPlayer confirmComplete;
	
	
	private File count = new File("media/countPing.mp3");
    private final String COUNT_URL = count.toURI().toString();
        
    private File confirm = new File("media/confirmComplete.mp3");
    private final String CONFIRM_URL = confirm.toURI().toString();

	Timeline fadePlay;
	Timeline fadeStop;
	
	
	double rightHandPosX = -50.0;
	double rightHandPosY = -50.0;
	double realRightHandPosX = -50.0;
	double realRightHandPosY = -50.0;

	double leftHandPosX = -50.0;
	double leftHandPosY = -50.0;
	double realLeftHandPosX = -50.0;
	double realLeftHandPosY = -50.0;
	
	double pointPosX = -50.0;
	double pointPosY = -50.0;
	double realPointPosX = -50.0;
	double realPointPosY = -50.0;

	public BlockExperience() {
		
		pane = new StackPane();
		canvas = new Pane();
		blocks = new Group();
		buttons = new AnchorPane();
		
		/* SET UP HAND AND POINT IMAGE */
		
		Image backImg = new Image("media/block_background1600_900.jpg", 1600, 1000,
				true, true);
		ImageView backView = new ImageView(backImg);
		backView.setPreserveRatio(true);
		pane.getChildren().add(backView);

		sleepTimer = new Timeline(new KeyFrame(Duration.millis(30000),
				ae -> goToMainMenu()));
			
		Media countMedia = new Media(COUNT_URL);
		countPing = new MediaPlayer(countMedia);
		countPing.setVolume(.25);
		
		Media confirmMedia = new Media(CONFIRM_URL);
		confirmComplete = new MediaPlayer(confirmMedia);
		confirmComplete.setVolume(.25);	
		
		Image exitImageNormal = new Image("media/Exit180_180.png", 150,150,
				true, true);
		exitImage = new ImageView(exitImageNormal);
		
		Image exitImageHovered = new Image("media/ExitHovered180_180.png", 150,150,
				true, true);
		
		Image addBlockNormal = new Image("media/BlockPlus60_60.png", 125,125,
				true, true);
		addBlockImage = new ImageView(addBlockNormal);
		
		Image addBlockHovered = new Image("media/BlockPlusHovered.png", 125,125,
				true, true);
		
		 palmRightNormal = new Image("media/palmRight.png", 100, 100,
				true, true);
		
		rightHand = new ImageView(palmRightNormal);
		Image rightHandFull = new Image("media/Hold_fullHand_102_107.png", 100,
                                100, true, true);
		
		pointNormal = new Image("media/tap.png", 50, 50,
				true, true);
		pointSelected = new Image("media/tapSelected.png", 50, 50,
				true, true);
		point = new ImageView(pointNormal);

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
				point.setTranslateX(pointPosX);
				point.setTranslateY(pointPosY);
				
				if(!exitImage.getBoundsInParent().contains(rightHand.getBoundsInParent()) 
					&& !addBlockImage.getBoundsInParent().contains(rightHand.getBoundsInParent())){
					
					rightHand.setImage(palmRightNormal);
					
				}
				
				if(exitImage.getBoundsInParent().contains(rightHand.getBoundsInParent())){
					exitImage.setImage(exitImageHovered);
					
					if(exit.getStatus() != Animation.Status.RUNNING){
						exit.play();
					}
						
					
				}else{
					exit.stop();
					confirmComplete.stop();
					exitImage.setImage(exitImageNormal);
				}
				
				if(blocks.getChildren().size() < 25){
					addBlockImage.setVisible(true);
					if(addBlockImage.getBoundsInParent().contains(rightHand.getBoundsInParent())){
						addBlockImage.setImage(addBlockHovered);
					
						if(addBlock.getStatus() != Animation.Status.RUNNING){
							addBlock.play();
						}
						
					
					}else{
						addBlock.stop();
						addBlockImage.setImage(addBlockNormal);
					}
				}else{
					addBlockImage.setVisible(false);
				}
				
				if(selected == null){
					point.setImage(pointNormal);
				}else{
					point.setImage(pointSelected);
				}
				
			}
		};
		
		exit = new Timeline(new KeyFrame(Duration.seconds(.5),
								 ae -> {
								 	rightHand.setImage(rightHandFull);
									confirmComplete.play();
							}),
							new KeyFrame(Duration.seconds(1),
								 ae -> {
									goToMainMenu();
							}),
							new KeyFrame(Duration.seconds(2),
								 ae -> {
									rightHand.setImage(palmRightNormal);

							}));
							
		addBlock = new Timeline(new KeyFrame(Duration.seconds(.5),
								 ae -> {
								 	rightHand.setImage(rightHandFull);
								 	addBlock();
							}),
							new KeyFrame(Duration.seconds(1),
								 ae -> {
										countPing.stop();
									countPing.play();
							}),
							new KeyFrame(Duration.seconds(2),
								 ae -> {
									rightHand.setImage(palmRightNormal);

							}));
							
		
		/* PLACE HOLDER  

		Text t = new Text("BLOCK EXPERIENCE");

		t.setFont(Font.font("Avenir Next", 30));
		t.setFill(Color.WHITE);

		BorderPane p = new BorderPane();
		p.setCenter(t);
		pane.getChildren().add(p);

		 PLACE HOLDER  DONE */
		
		background = new File("media/blockBackgroundLoop.mp3");
		String BACKGROUND_MUSIC = background.toURI().toString();
		backgroundMusic = new Media(BACKGROUND_MUSIC);
	 	backgroundMusicPlayer = new MediaPlayer(backgroundMusic);

		fadePlay = new Timeline(new KeyFrame(Duration.ZERO,
			ae -> backgroundMusicPlayer.play(), new KeyValue(
					backgroundMusicPlayer.volumeProperty(), 0.0)),
			new KeyFrame(new Duration(800), new KeyValue(backgroundMusicPlayer
					.volumeProperty(), 0.25)));
	
	 	fadeStop = new Timeline(new KeyFrame(Duration.ZERO,
			new KeyValue(backgroundMusicPlayer.volumeProperty(), 0.25)),
			new KeyFrame(new Duration(800), ae -> backgroundMusicPlayer.stop(),
					new KeyValue(backgroundMusicPlayer.volumeProperty(), 0.0)));

		
		/* CREATE THE GROUND AND WALLS 	*/	
		
		WorldModel.addWall(8,1f,16.0f,1.0f);
		
		/* THIS IS FOR TESTING -- TO SEE GROUND
		
		Rectangle ground = new Rectangle();
		ground.setWidth(WorldModel.fromMetersToPixels(16.0f));
		ground.setHeight(WorldModel.fromMetersToPixels(1.0f));
		ground.setFill(Color.ORANGE);
		ground.setStroke(Color.BLACK);
		//ground.setStrokeWidth(1.5);
		ground.setLayoutX(WorldModel.fromJPosXToSPosX(0.0f));
		ground.setLayoutY(WorldModel.fromJPosYToSPosY(1.0f));*/
		
			
		WorldModel.addWall(2.5f,5.0f,0.25f,10.0f);
		Rectangle left = new Rectangle();
		left.setWidth(WorldModel.fromMetersToPixels(0.25f));
		left.setHeight(WorldModel.fromMetersToPixels(9.0f));
		Stop[] stops = new Stop[] { new Stop(0, new Color(1,1,1,0.0)), new Stop(1, Color.CYAN)};
        LinearGradient lg1 = new LinearGradient(0, 0, 0, 1, true, CycleMethod.REFLECT, stops);
		left.setFill(lg1);
		left.setStroke(Color.LIGHTCYAN);
		left.setOpacity(0.70);
		left.setEffect(new GaussianBlur());
		//ground.setStrokeWidth(1.5);
		left.setLayoutX(WorldModel.fromJPosXToSPosX(3.00f));
		left.setLayoutY(WorldModel.fromJPosYToSPosY(10.5f));
		
		WorldModel.addWall(12.5f,5.0f,0.25f,10.0f);
		Rectangle right = new Rectangle();
		right.setWidth(WorldModel.fromMetersToPixels(0.25f));
		right.setHeight(WorldModel.fromMetersToPixels(9.0f));
		right.setFill(lg1);
		right.setStroke(Color.LIGHTCYAN);
		right.setOpacity(0.70);
		right.setEffect(new GaussianBlur());
		//ground.setStrokeWidth(1.5);
		right.setLayoutX(WorldModel.fromJPosXToSPosX(12.75f));
		right.setLayoutY(WorldModel.fromJPosYToSPosY(10.5f));
		
        
        blockDrawer = new Timeline();
        blockDrawer.setCycleCount(Timeline.INDEFINITE);
 		
 		
        Duration duration = Duration.seconds(1.0/60.0); // Set duration for frame.
             
         /**
         * Set ActionEvent and duration to the KeyFrame. 
         * The ActionEvent is trigged when KeyFrame execution is over. 
         */
        KeyFrame frame = new KeyFrame(duration, ae ->{
			//Create time step. Set Iteration count 8 for velocity and 3 for positions
			WorldModel.WORLD.step(1.0f/60.f, 8, 3); 
			//Move balls to the new position computed by JBox2D
			for(int i = 0 ; i< blocks.getChildren().size(); i++){
			Node current = blocks.getChildren().get(i);
			Body body = (Body)current.getUserData();
			float xpos = body.getPosition().x;
			float ypos = body.getPosition().y;
			
			current.setLayoutX(WorldModel.fromJPosXToSPosX((float)xpos));
			current.setLayoutY(WorldModel.fromJPosYToSPosY((float)ypos));
			current.setRotate(-(180*body.getAngle())/Math.PI);
			}

        }, null,null);
 
        blockDrawer.getKeyFrames().add(frame);
        
        
        blocks.setManaged(false);
        
		selected = null;
	
	
		canvas.getChildren().addAll(rightHand, leftHand, point,left,right);//, ground ,left,right );

		rightHand.relocate(rightHandPosX, rightHandPosY);
		leftHand.relocate(leftHandPosX, leftHandPosY);
		point.relocate(pointPosX, pointPosY);
		
		buttons.getChildren().addAll(exitImage,addBlockImage);
		buttons.setBottomAnchor(exitImage, 100.0); 
		buttons.setRightAnchor(exitImage, 115.0);
		buttons.setBottomAnchor(addBlockImage, 105.0);
		buttons.setLeftAnchor(addBlockImage, 140.0);
		
		pane.getChildren().add(blocks);
		pane.getChildren().addAll(buttons,canvas);
	}

	@Override
	public void setParent(ExperienceController controller) {
		myController = controller;
		
	}

	@Override
	public void startExperience() {
		createDefaultBlocks();
		drawHands.start();
		sleepTimer.play();
		blockDrawer.play();
		backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		fadePlay.play();
		controller = new Controller(this);
		controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
	}

	@Override
	public void stopExperience() {
		right = null;
		left = null;

		rightHandPosX = -50.0;
		rightHandPosY = -50.0;

		leftHandPosX = -50.0;
		leftHandPosY = -50.0;
		
		pointPosX = -50.0;
		pointPosY = -50.0;
		
		drawHands.stop();
		sleepTimer.stop();
		blockDrawer.stop();
		exit.stop();
		confirmComplete.stop();
		countPing.stop();
		
		// REMOVE BLOCKS
		for(int i = 0 ; i< blocks.getChildren().size(); i++){
				Node current = (Node)blocks.getChildren().get(i);
				Body body = (Body) current.getUserData();
				WorldModel.WORLD.destroyBody(body);
		}
		blocks.getChildren().clear();
		
		fadeStop.play();

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
		GestureList gestures = frame.gestures();
		FingerList extended = frame.fingers().extended();
		
		rightHandPosX = -50.0;
		rightHandPosY = -50.0;
		realRightHandPosX = -50.0;
		realRightHandPosY = -50.0;

		leftHandPosX = -50.0;
		leftHandPosY = -50.0;
		realLeftHandPosX = -50.0;
		realLeftHandPosY = -50.0;
		
		pointPosX = -50.0;
		pointPosY = -50.0;
		realPointPosX = -50.0;
		realPointPosY = -50.0;

		
		if(extended.count() == 1){	
			pointPosX = Util.pointXToPanelX(extended.get(0));
			pointPosY = Util.pointYToPanelY(extended.get(0));
			realPointPosX = extended.get(0).stabilizedTipPosition().getX();
			realPointPosY = extended.get(0).stabilizedTipPosition().getY();	
			if(Util.pointXToPanelX(extended.get(0)) > 325 && Util.pointXToPanelX(extended.get(0))< 1325
				&& Util.pointYToPanelY(extended.get(0)) > 75 && Util.pointYToPanelY(extended.get(0))< 825
			){
				
			for(int i = 0 ; i< blocks.getChildren().size(); i++){
				Node current = (Node)blocks.getChildren().get(i);
				if(current.getBoundsInParent().contains(point.getBoundsInParent())){
						 selected = current;
						 break;
				}else{
					selected = null;
				}	
			}
			
				if(selected != null){
					Body body = (Body) selected.getUserData();
					body.setType(BodyType.STATIC);
					body.setTransform(new Vec2( (float)( pointPosX - ( 25 + selected.getBoundsInLocal().getWidth()/2 ))/100 ,
											(float)(10-((pointPosY - ( 25 + selected.getBoundsInLocal().getHeight()/2 )))/100)
											)
												,body.getAngle());
					body.setType(BodyType.DYNAMIC);
				}
			}
		}else{
			
			if(selected != null){
				Body body = (Body) selected.getUserData();
				body.setType(BodyType.DYNAMIC);
				selected = null;
			}
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
		}
	}
	
	private void createDefaultBlocks(){
		//create blocks   
		for(int i = 0 ; i< 10; i++){
        	block = new Block(8, 9, .9,.9);
        	blocks.getChildren().add(block.node);
        }
	}
	
	private void addBlock(){
        block = new Block(8, 9, .9,.9);
        blocks.getChildren().add(block.node);
	}
}
