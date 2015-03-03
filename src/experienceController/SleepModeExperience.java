
/*
*
*	This Experience Play a Video Loop until a motion is detected by the sensor.
*		
*	DATE INITIALLY CREATED:
*		03/02/15 - (Brandon Caruso)
*		
*/

import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import java.io.File;
import javafx.event.*;
import javafx.scene.input.MouseEvent;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;

public class SleepModeExperience implements Experience{
	
	ExperienceController myController;
	MediaPlayer mediaPlayer;
	private File videofile = new File("../../testVideo.mp4");
	private final String MEDIA_URL = videofile.toURI().toString();
	
	BorderPane pane;
	
	public SleepModeExperience() {	
		pane = new BorderPane();
 		// create media player
		Media media = new Media(MEDIA_URL);
		mediaPlayer = new MediaPlayer(media);		
 		
 		// create mediaView and add media player to the viewer
		MediaView mediaView = new MediaView(mediaPlayer);
		
		DoubleProperty width = mediaView.fitWidthProperty();
		DoubleProperty height = mediaView.fitHeightProperty();
		
		width.bind(Bindings.selectDouble(mediaView.sceneProperty(),"width"));
		height.bind(Bindings.selectDouble(mediaView.sceneProperty(),"height"));
		mediaView.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				goToConfirm();
			}
		});
		pane.setCenter(mediaView);
		mediaView.setPreserveRatio(true);
	}
	
	public void setParent(ExperienceController controller){
		myController = controller;
	}
	
	public void startExperience(){
		System.out.println("Here");
		mediaPlayer.play();
	}

	public void stopExperience(){
		mediaPlayer.stop();
	}
	
	public Node getNode(){
		return pane;
	}
	
	private void goToConfirm(){ 
	 	stopExperience();
     	myController.setExperience(InteractiveWall.CONFRIMATION); 
    }
	
	
}