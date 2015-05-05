package experience.pond;

import java.io.File;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class PondMusic {

	private static File background = new File(
			"media/Lee_Rosevere_-_03_-_Cosmic_Tingles.mp3");
	private static final String BACKGROUND_MUSIC = background.toURI()
			.toString();
	private static Media backgroundMusic = new Media(BACKGROUND_MUSIC);
	private static MediaPlayer backgroundMusicPlayer = new MediaPlayer(
			backgroundMusic);

	// private static File backgroundWater = new
	// File("src/media/StreamNoise.mp3");
	// private static final String WATER_BACKGROUND_MUSIC = backgroundWater
	// .toURI().toString();
	// private static Media backgroundWaterMusic = new Media(
	// WATER_BACKGROUND_MUSIC);
	// private static MediaPlayer backgroundWaterMusicPlayer = new MediaPlayer(
	// backgroundWaterMusic);

	// private static Timeline fadePlay = new Timeline(new
	// KeyFrame(Duration.ZERO,
	// ae -> backgroundMusicPlayer.play(), new KeyValue(
	// backgroundMusicPlayer.volumeProperty(), 0.0)),
	// new KeyFrame(new Duration(800), new KeyValue(backgroundMusicPlayer
	// .volumeProperty(), 0.25)));

	private static Timeline fadePause = new Timeline(new KeyFrame(
			Duration.ZERO, new KeyValue(backgroundMusicPlayer.volumeProperty(),
					0.25)), new KeyFrame(new Duration(800),
			ae -> backgroundMusicPlayer.pause(), new KeyValue(
					backgroundMusicPlayer.volumeProperty(), 0.0)));
	private static Timeline fadeStop = new Timeline(new KeyFrame(Duration.ZERO,
			new KeyValue(backgroundMusicPlayer.volumeProperty(), 0.25)),
			new KeyFrame(new Duration(800), ae -> backgroundMusicPlayer.stop(),
					new KeyValue(backgroundMusicPlayer.volumeProperty(), 0.0)));

	// private static Timeline fadeWaterPause = new Timeline(new KeyFrame(
	// Duration.ZERO, new KeyValue(
	// backgroundWaterMusicPlayer.volumeProperty(), 0.25)),
	// new KeyFrame(new Duration(800), ae -> backgroundWaterMusicPlayer
	// .pause(), new KeyValue(backgroundWaterMusicPlayer
	// .volumeProperty(), 0.0)));
	// private static Timeline fadeWaterStop = new Timeline(new KeyFrame(
	// Duration.ZERO, new KeyValue(
	// backgroundWaterMusicPlayer.volumeProperty(), 0.25)),
	// new KeyFrame(new Duration(800), ae -> backgroundWaterMusicPlayer
	// .stop(), new KeyValue(backgroundWaterMusicPlayer
	// .volumeProperty(), 0.0)));

	public static void playBackgroundMusic() {
		backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		backgroundMusicPlayer.setVolume(0.25);
		backgroundMusicPlayer.setStartTime(Duration.seconds(5));
		backgroundMusicPlayer.play();

		// backgroundWaterMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		// backgroundWaterMusicPlayer.setVolume(0.25);
		// backgroundWaterMusicPlayer.setStartTime(Duration.seconds(5));
		// backgroundWaterMusicPlayer.play();

		// AudioStream backgroundMusic;
		// AudioData musicData;
		// AudioPlayer musicPlayer = AudioPlayer.player;
		// ContinuousAudioDataStream loop = null;
		// try {
		// backgroundMusic = new AudioStream(new
		// FileInputStream("src/media/StreamNoise.wav"));
		// musicData = backgroundMusic.getData();
		// loop = new ContinuousAudioDataStream(musicData);
		// musicPlayer.start(loop);
		// } catch (IOException error) {
		// System.out.println(error);
		// }
	}

	public static void pauseBackgroundMusic() {
		fadePause.play();
		// fadeWaterPause.play();
	}

	public static void stopBackgroundMusic() {
		fadeStop.play();
		// fadeWaterStop.play();
	}
}