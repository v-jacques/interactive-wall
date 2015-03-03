package sleepmode;

import java.io.File;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class EmbeddedMediaPlayer extends Application {

	private File file = new File("testVideo.mp4");
	private final String MEDIA_URL = file.toURI().toString();

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("My Media Player");

		BorderPane borderPane = new BorderPane();

		// create media player
		Media media = new Media(MEDIA_URL);
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setAutoPlay(true);

		// create mediaView and add media player to the viewer
		MediaView mediaView = new MediaView(mediaPlayer);

		DoubleProperty width = mediaView.fitWidthProperty();
		DoubleProperty height = mediaView.fitHeightProperty();

		width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
		height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

		borderPane.setCenter(mediaView);
		mediaView.setPreserveRatio(true);
		Scene scene = new Scene(borderPane, 1280, 720);
		primaryStage.setFullScreen(true);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}