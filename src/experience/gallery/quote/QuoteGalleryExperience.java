package experience.gallery.quote;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
import com.leapmotion.leap.Vector;

public class QuoteGalleryExperience extends Listener implements Experience {
	Controller controller;
	ExperienceController myController;
	StackPane pane;

	Pane canvas;
	Timeline sleepTimer;
	Timeline changeGallery;

	Hand right;
	Hand left;

	ImageView rightHand;
	ImageView leftHand;
	ImageView artWork;

	AnimationTimer drawHands;
	AnimationTimer changeQuotes;

	double rightHandPosX = -50.0;
	double rightHandPosY = -50.0;
	double realRightHandPosX = -50.0;
	double realRightHandPosY = -50.0;

	double leftHandPosX = -50.0;
	double leftHandPosY = -50.0;
	double realLeftHandPosX = -50.0;
	double realLeftHandPosY = -50.0;

	int quoteHolder = 1;
	boolean direction;

	Image artButton = new Image("media/galleryButton-selected 270_63 px.png",
			270, 63, true, true);

	Image notArt = new Image("media/galleryButton 270_63 px.png", 270, 63,
			true, true);

	ExperienceQuote[] quoteList;

	Text topQ;
	Text mainQ;
	Text bottomQ;

	public QuoteGalleryExperience() {
		quoteList = new ExperienceQuote[] {
				new ExperienceQuote("a", "author a"),
				new ExperienceQuote("b", "author b"),
				new ExperienceQuote("c", "author c"),
				new ExperienceQuote("d", "author d"),
				new ExperienceQuote("e", "author e"),
				new ExperienceQuote("f", "author f") };

		pane = new StackPane();
		canvas = new Pane();

		artWork = new ImageView(notArt);
		artWork.setLayoutX(650);
		artWork.setLayoutY(890);

		Image backImg = new Image("media/background1600_1000.jpg", 1600, 1000,
				true, true);
		ImageView backView = new ImageView(backImg);
		backView.setPreserveRatio(true);
		pane.getChildren().add(backView);

		topQ = new Text(quoteList[quoteHolder - 1].getQuote() + " - "
				+ quoteList[quoteHolder - 1].getAuthor());

		mainQ = new Text(quoteList[quoteHolder].getQuote() + " - "
				+ quoteList[quoteHolder].getAuthor());

		bottomQ = new Text(quoteList[quoteHolder + 1].getQuote() + " - "
				+ quoteList[quoteHolder + 1].getAuthor());

		bottomQ.setLayoutX(700);
		bottomQ.setLayoutY(250);
		bottomQ.setFont(Font.font(null, FontWeight.NORMAL, 40));
		canvas.getChildren().add(bottomQ);

		mainQ.setLayoutX(700);
		mainQ.setLayoutY(500);
		mainQ.setFont(Font.font(null, FontWeight.NORMAL, 40));
		canvas.getChildren().add(mainQ);

		topQ.setLayoutX(700);
		topQ.setLayoutY(750);
		topQ.setFont(Font.font(null, FontWeight.NORMAL, 40));
		canvas.getChildren().add(topQ);

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

				if (Util.isBetween(650, 650 + 270, (int) rightHandPosX)
						&& Util.isBetween(890, 890 + 63, (int) rightHandPosY)) {
					changeGallery.play();
				}
			}
		};

		changeQuotes = new AnimationTimer() {
			@Override
			public void handle(long now) {
				changeQuotes();
			}
		};

		Image rightHandFull = new Image("media/Hold_fullHand_102_107.png", 100,
				100, true, true);

		changeGallery = new Timeline(new KeyFrame(Duration.seconds(.5), ae -> {
			rightHand.setImage(rightHandFull);
		}), new KeyFrame(Duration.seconds(1), ae -> {
			switchGallery();
		}));

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

	public void switchGallery() {
		// canvas.setVisible(true);
		// quote.setVisible(false);
		// artWork.setImage(artButton);
		// quoteWall.setImage(notQuote);
	}

	public void changeQuotes() {
		if (direction) {
			if (quoteHolder == (quoteList.length - 2)) {
				Text newTop = new Text(quoteList[quoteHolder].getQuote()
						+ " - " + quoteList[quoteHolder].getAuthor());
				Text newMain = new Text(quoteList[quoteHolder + 1].getQuote()
						+ " - " + quoteList[quoteHolder + 1].getAuthor());
				Text newBottom = new Text(quoteList[0].getQuote() + " - "
						+ quoteList[0].getAuthor());

				topQ.setText(newTop.getText());
				mainQ.setText(newMain.getText());
				bottomQ.setText(newBottom.getText());

				quoteHolder++;
			} else if (quoteHolder == (quoteList.length - 1)) {
				Text newTop = new Text(quoteList[quoteHolder].getQuote()
						+ " - " + quoteList[quoteHolder].getAuthor());
				Text newMain = new Text(quoteList[0].getQuote() + " - "
						+ quoteList[0].getAuthor());
				Text newBottom = new Text(quoteList[1].getQuote() + " - "
						+ quoteList[1].getAuthor());

				topQ.setText(newTop.getText());
				mainQ.setText(newMain.getText());
				bottomQ.setText(newBottom.getText());

				quoteHolder = 0;
			} else {
				Text newTop = new Text(quoteList[quoteHolder].getQuote()
						+ " - " + quoteList[quoteHolder].getAuthor());
				Text newMain = new Text(quoteList[quoteHolder + 1].getQuote()
						+ " - " + quoteList[quoteHolder + 1].getAuthor());
				Text newBottom = new Text(quoteList[quoteHolder + 2].getQuote()
						+ " - " + quoteList[quoteHolder + 2].getAuthor());

				topQ.setText(newTop.getText());
				mainQ.setText(newMain.getText());
				bottomQ.setText(newBottom.getText());

				quoteHolder++;
			}
		} else {
			if (quoteHolder == 1) {
				Text newTop = new Text(
						quoteList[quoteList.length - 1].getQuote() + " - "
								+ quoteList[quoteList.length - 1].getAuthor());
				Text newMain = new Text(quoteList[quoteHolder - 1].getQuote()
						+ " - " + quoteList[quoteHolder - 1].getAuthor());
				Text newBottom = new Text(quoteList[quoteHolder].getQuote()
						+ " - " + quoteList[quoteHolder].getAuthor());

				topQ.setText(newTop.getText());
				mainQ.setText(newMain.getText());
				bottomQ.setText(newBottom.getText());

				quoteHolder--;
			} else if (quoteHolder == 0) {
				Text newTop = new Text(
						quoteList[quoteList.length - 2].getQuote() + " - "
								+ quoteList[quoteList.length - 2].getAuthor());
				Text newMain = new Text(
						quoteList[quoteList.length - 1].getQuote() + " - "
								+ quoteList[quoteList.length - 1].getAuthor());
				Text newBottom = new Text(quoteList[quoteHolder].getQuote()
						+ " - " + quoteList[quoteHolder].getAuthor());

				topQ.setText(newTop.getText());
				mainQ.setText(newMain.getText());
				bottomQ.setText(newBottom.getText());

				quoteHolder = quoteList.length - 1;
			} else {
				Text newTop = new Text(quoteList[quoteHolder - 2].getQuote()
						+ " - " + quoteList[quoteHolder - 2].getAuthor());
				Text newMain = new Text(quoteList[quoteHolder - 1].getQuote()
						+ " - " + quoteList[quoteHolder - 1].getAuthor());
				Text newBottom = new Text(quoteList[quoteHolder].getQuote()
						+ " - " + quoteList[quoteHolder].getAuthor());

				topQ.setText(newTop.getText());
				mainQ.setText(newMain.getText());
				bottomQ.setText(newBottom.getText());

				quoteHolder--;
			}

		}
		changeQuotes.stop();
	}

	public void onConnect(Controller controller) {
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

		if (swipe.isValid() && swipeStart.getY() > 130 && swipeDir.getY() < 0) {
			direction = true;
			switch (swipe.state()) {
			case STATE_STOP:
				changeQuotes.start();
				break;
			default:
				break;
			}
			// System.out.println(swipe.state());
		} else if (swipe.isValid() && swipeStart.getY() < -130
				&& swipeDir.getY() > 0) {
			direction = false;
			switch (swipe.state()) {
			case STATE_STOP:
				changeQuotes.start();
				break;
			default:
				break;
			}
			// System.out.println(swipe.state());
		}

	}
}
