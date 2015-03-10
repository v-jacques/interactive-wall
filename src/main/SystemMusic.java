package main;

import java.io.File;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class SystemMusic {

	private static File background = new File("src/media/backgroundLoop.mp3");
	private static final String BACKGROUND_MUSIC = background.toURI()
			.toString();
	private static Media backgroundMusic = new Media(BACKGROUND_MUSIC);
	private static MediaPlayer backgroundMusicPlayer = new MediaPlayer(
			backgroundMusic);

	private static Timeline fadePlay = new Timeline(new KeyFrame(Duration.ZERO,
			ae -> backgroundMusicPlayer.play(), new KeyValue(
					backgroundMusicPlayer.volumeProperty(), 0.0)),
			new KeyFrame(new Duration(800), new KeyValue(backgroundMusicPlayer
					.volumeProperty(), 0.25)));
	private static Timeline fadePause = new Timeline(new KeyFrame(
			Duration.ZERO, new KeyValue(backgroundMusicPlayer.volumeProperty(),
					0.25)), new KeyFrame(new Duration(800),
			ae -> backgroundMusicPlayer.pause(), new KeyValue(
					backgroundMusicPlayer.volumeProperty(), 0.0)));
	private static Timeline fadeStop = new Timeline(new KeyFrame(Duration.ZERO,
			new KeyValue(backgroundMusicPlayer.volumeProperty(), 0.25)),
			new KeyFrame(new Duration(800), ae -> backgroundMusicPlayer.stop(),
					new KeyValue(backgroundMusicPlayer.volumeProperty(), 0.0)));

	public static void playBackgroundMusic() {
		backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		fadePlay.play();
	}

	public static void pauseBackgroundMusic() {

		fadePause.play();
	}

	public static void stopBackgroundMusic() {
		fadeStop.play();
	}
}