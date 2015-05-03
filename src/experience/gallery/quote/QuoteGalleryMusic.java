package experience.gallery.quote;

import java.io.File;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

public class QuoteGalleryMusic {

	private static File background = new File(
			"src/media/John_Lewis_Grant_-_10_-_Bach_Prelude___Fugue_10.mp3");
	private static final String BACKGROUND_MUSIC = background.toURI()
			.toString();
	private static Media backgroundMusic = new Media(BACKGROUND_MUSIC);
	private static MediaPlayer backgroundMusicPlayer = new MediaPlayer(
			backgroundMusic);

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
		backgroundMusicPlayer.setVolume(0.25);
		backgroundMusicPlayer.setStartTime(Duration.seconds(5));
		backgroundMusicPlayer.play();
	}

	public static void pauseBackgroundMusic() {
		fadePause.play();
	}

	public static void stopBackgroundMusic() {
		fadeStop.play();
	}
	
	public static Status getMusicStatus() {
		return backgroundMusicPlayer.getStatus();
	}

}