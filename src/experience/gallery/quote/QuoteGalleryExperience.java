package experience.gallery.quote;

import java.io.File;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
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
	Timeline rightHandChange;

	Hand right;
	Hand left;

	ImageView rightHand;
	ImageView leftHand;
	ImageView quoteView;
	ImageView artView;

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

	MediaPlayer confirmComplete;
	private File confirm = new File("src/media/confirmComplete.mp3");
	private final String CONFIRM_URL = confirm.toURI().toString();

	Image artButtonHovered = new Image(
			"media/galleryButton-selected 270_63 px.png", 270, 63, true, true);
	Image artButton = new Image("media/galleryButton 270_63 px.png", 270, 63,
			true, true);
	Image quoteButton = new Image("media/quoteButton-selected 270_63 px.png",
			270, 63, true, true);

	ExperienceQuote[] quoteList;

	Text topQ;
	Text mainQ;
	Text bottomQ;

	public QuoteGalleryExperience() {
		quoteList = new ExperienceQuote[] {
				new ExperienceQuote(
						"Life isn't about finding yourself. Life is about creating yourself.",
						"George Bernard Shaw"),
				new ExperienceQuote(
						"In the end, it's not the years in your life that count. It's the life in your years.",
						"Abraham Lincoln"),
				new ExperienceQuote(
						"You have succeeded in life when all you really want is only what you really need.",
						"Vernon Howard"),
				new ExperienceQuote(
						"Open your eyes, look within. Are you satisfied with the life you're living?",
						"Bob Marley"),
				new ExperienceQuote(
						"It is often said that before you die your life passes before your eyes. It is in fact true. It's called living.",
						"Terry Pratchett"),
				new ExperienceQuote(
						"Success is not final, failure is not fatal: it is the courage to continue that counts.",
						"Winston Churchill"),
				new ExperienceQuote(
						"The starting point of all achievement is desire.",
						"Napoleon Hill"),
				new ExperienceQuote(
						"A successful man is one who can lay a firm foundation with the bricks others have thrown at him.",
						"David Brinkley"),
				new ExperienceQuote(
						"Immature love says: 'I love you because I need you.' Mature love says 'I need you because I love you.",
						"Erich Fromm"),
				new ExperienceQuote(
						"The most powerful weapon on earth is the human soul on fire.",
						"Ferdinand Foch"),
				new ExperienceQuote(
						"Love doesn't make the world go 'round. Love is what makes the ride worthwhile.",
						"Franklin P. Jones") };

		pane = new StackPane();
		canvas = new Pane();
		canvas.setBackground(new Background(new BackgroundFill(Color.WHITE,
				CornerRadii.EMPTY, Insets.EMPTY)));

		artView = new ImageView(artButton);
		artView.setLayoutX(515);
		artView.setLayoutY(859);
		canvas.getChildren().add(artView);

		quoteView = new ImageView(quoteButton);
		quoteView.setLayoutX(810);
		quoteView.setLayoutY(859);
		canvas.getChildren().add(quoteView);

		// Image backImg = new Image("media/background1600_1000.jpg", 1600,
		// 1000,
		// true, true);
		// ImageView backView = new ImageView(backImg);
		// backView.setPreserveRatio(true);
		// pane.getChildren().add(backView);

		Image exitImg = new Image("media/Exit180_180.png", 150, 150, true, true);
		Image exitHoveredImg = new Image("media/ExitHovered180_180.png", 150,
				150, true, true);
		ImageView exitView = new ImageView(exitImg);
		exitView.setOpacity(0.3);
		exitView.setPreserveRatio(true);
		exitView.setLayoutX(1335);
		exitView.setLayoutY(710);
		canvas.getChildren().add(exitView);

		topQ = new Text(quoteList[quoteHolder - 1].getQuote() + "\n- "
				+ quoteList[quoteHolder - 1].getAuthor());

		mainQ = new Text(quoteList[quoteHolder].getQuote() + "\n- "
				+ quoteList[quoteHolder].getAuthor());

		bottomQ = new Text(quoteList[quoteHolder + 1].getQuote() + "\n- "
				+ quoteList[quoteHolder + 1].getAuthor());

		bottomQ.setLayoutX(200);
		bottomQ.setLayoutY(750);

		bottomQ.autosize();
		// bottomQ.setMaxSize(800, 300);
		// bottomQ.setMinSize(800, 300);

		// bottomQ.setStyle("-fx-background-color: #fff, #fff; -fx-background-color: #fff; -fx-background-color: #fff;");
		// bottomQ.setBorder(Border.EMPTY);
		// bottomQ.setBackground(Background.EMPTY);
		// bottomQ.setFocusTraversable(false);
		bottomQ.setFont(Font.font(null, FontWeight.NORMAL, 40));
		bottomQ.setEffect(new GaussianBlur());
		canvas.getChildren().add(bottomQ);

		mainQ.setLayoutX(200);
		mainQ.setLayoutY(400);
		// mainQ.setMaxSize(800, 300);
		// mainQ.setMinSize(800, 300);
		// mainQ.setBorder(Border.EMPTY);
		mainQ.setFont(Font.font(null, FontWeight.NORMAL, 40));
		canvas.getChildren().add(mainQ);

		topQ.setLayoutX(200);
		topQ.setLayoutY(50);
		// topQ.setMaxSize(800, 300);
		// topQ.setMinSize(800, 300);
		// topQ.setBorder(Border.EMPTY);
		// topQ.setBackground(Background.EMPTY);
		// topQ.setFocusTraversable(false);
		topQ.setFont(Font.font(null, FontWeight.NORMAL, 40));
		topQ.setEffect(new GaussianBlur());
		canvas.getChildren().add(topQ);

		sleepTimer = new Timeline(new KeyFrame(Duration.millis(20000),
				ae -> goToMainMenu()));

		Image palmRightNormal = new Image("media/palmRight.png", 100, 100,
				true, true);
		rightHand = new ImageView(palmRightNormal);

		Image palmLeftNormal = new Image("media/palmLeft.png", 100, 100, true,
				true);
		leftHand = new ImageView(palmLeftNormal);

		Image rightHandFull = new Image("media/Hold_fullHand_102_107.png", 100,
				100, true, true);

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

		drawHands = new AnimationTimer() {
			@Override
			public void handle(long now) {
				rightHand.setTranslateX(rightHandPosX);
				rightHand.setTranslateY(rightHandPosY);
				leftHand.setTranslateX(leftHandPosX);
				leftHand.setTranslateY(leftHandPosY);

				if (Util.isBetween(1235, 1550, (int) rightHandPosX)
						&& Util.isBetween(610, 960, (int) rightHandPosY)) {
					if (Util.isBetween(1335, 1450, (int) rightHandPosX)
							&& Util.isBetween(710, 860, (int) rightHandPosY)) {
						exitView.setImage(exitHoveredImg);
						exitView.setOpacity(1.0);
						rightHandChange.play();
					}
				} else {
					exitView.setImage(exitImg);
					exitView.setOpacity(0.3);
					rightHandChange.stop();
					rightHand.setImage(palmRightNormal);
				}

				if (Util.isBetween(515, 515 + 270, (int) rightHandPosX)
						&& Util.isBetween(859, 859 + 63, (int) rightHandPosY)) {
					changeGallery.play();
				} else {
					changeGallery.stop();
					rightHand.setImage(palmRightNormal);
				}
			}
		};

		changeQuotes = new AnimationTimer() {
			@Override
			public void handle(long now) {
				changeQuotes();
			}
		};

		changeGallery = new Timeline(new KeyFrame(Duration.seconds(.5), ae -> {
			rightHand.setImage(rightHandFull);
			confirmComplete.play();
		}), new KeyFrame(Duration.seconds(1), ae -> {
			switchGallery();
		}), new KeyFrame(Duration.seconds(2), ae -> {
			rightHand.setImage(palmRightNormal);
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
		rightHandChange.stop();
		changeGallery.stop();
		changeQuotes.stop();
	}

	@Override
	public Node getNode() {
		return pane;
	}

	private void goToMainMenu() {
		controller.removeListener(this);
		myController.setExperience(InteractiveWall.MAIN_MENU);
	}

	private void goToArtGallery() {
		controller.removeListener(this);
		myController.setExperience(InteractiveWall.GALLERY);
	}

	public void switchGallery() {
		// stopExperience();
		goToArtGallery();
	}

	public void changeQuotes() {
		if (direction) {
			if (quoteHolder == (quoteList.length - 2)) {
				Text newTop = new Text(quoteList[quoteHolder].getQuote()
						+ "\n- " + quoteList[quoteHolder].getAuthor());
				Text newMain = new Text(quoteList[quoteHolder + 1].getQuote()
						+ "n- " + quoteList[quoteHolder + 1].getAuthor());
				Text newBottom = new Text(quoteList[0].getQuote() + "\n- "
						+ quoteList[0].getAuthor());

				topQ.setText(newTop.getText());
				mainQ.setText(newMain.getText());
				bottomQ.setText(newBottom.getText());

				quoteHolder++;
			} else if (quoteHolder == (quoteList.length - 1)) {
				Text newTop = new Text(quoteList[quoteHolder].getQuote()
						+ "\n- " + quoteList[quoteHolder].getAuthor());
				Text newMain = new Text(quoteList[0].getQuote() + "\n- "
						+ quoteList[0].getAuthor());
				Text newBottom = new Text(quoteList[1].getQuote() + "\n- "
						+ quoteList[1].getAuthor());

				topQ.setText(newTop.getText());
				mainQ.setText(newMain.getText());
				bottomQ.setText(newBottom.getText());

				quoteHolder = 0;
			} else {
				Text newTop = new Text(quoteList[quoteHolder].getQuote()
						+ "\n- " + quoteList[quoteHolder].getAuthor());
				Text newMain = new Text(quoteList[quoteHolder + 1].getQuote()
						+ "\n- " + quoteList[quoteHolder + 1].getAuthor());
				Text newBottom = new Text(quoteList[quoteHolder + 2].getQuote()
						+ "\n- " + quoteList[quoteHolder + 2].getAuthor());

				topQ.setText(newTop.getText());
				mainQ.setText(newMain.getText());
				bottomQ.setText(newBottom.getText());

				quoteHolder++;
			}
		} else {
			if (quoteHolder == 1) {
				Text newTop = new Text(
						quoteList[quoteList.length - 1].getQuote() + "\n- "
								+ quoteList[quoteList.length - 1].getAuthor());
				Text newMain = new Text(quoteList[quoteHolder - 1].getQuote()
						+ "\n- " + quoteList[quoteHolder - 1].getAuthor());
				Text newBottom = new Text(quoteList[quoteHolder].getQuote()
						+ "\n- " + quoteList[quoteHolder].getAuthor());

				topQ.setText(newTop.getText());
				mainQ.setText(newMain.getText());
				bottomQ.setText(newBottom.getText());

				quoteHolder--;
			} else if (quoteHolder == 0) {
				Text newTop = new Text(
						quoteList[quoteList.length - 2].getQuote() + "\n- "
								+ quoteList[quoteList.length - 2].getAuthor());
				Text newMain = new Text(
						quoteList[quoteList.length - 1].getQuote() + "\n- "
								+ quoteList[quoteList.length - 1].getAuthor());
				Text newBottom = new Text(quoteList[quoteHolder].getQuote()
						+ "\n- " + quoteList[quoteHolder].getAuthor());

				topQ.setText(newTop.getText());
				mainQ.setText(newMain.getText());
				bottomQ.setText(newBottom.getText());

				quoteHolder = quoteList.length - 1;
			} else {
				Text newTop = new Text(quoteList[quoteHolder - 2].getQuote()
						+ "\n- " + quoteList[quoteHolder - 2].getAuthor());
				Text newMain = new Text(quoteList[quoteHolder - 1].getQuote()
						+ "\n- " + quoteList[quoteHolder - 1].getAuthor());
				Text newBottom = new Text(quoteList[quoteHolder].getQuote()
						+ "\n- " + quoteList[quoteHolder].getAuthor());

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
		}

	}
}
