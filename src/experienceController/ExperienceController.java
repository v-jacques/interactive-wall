
/*
*
*	This controller is what swaps out the different Experiences.
*		
*	DATE INITIALLY CREATED:
*		03/02/15 - (Brandon Caruso)
*		
*/

import javafx.scene.layout.StackPane;
import java.util.HashMap;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.beans.property.DoubleProperty;


public class ExperienceController extends StackPane {
	
	// This will contain all the diffrent Experiences to be moved between.
	public HashMap<String,Experience> experiences = new HashMap<>();
	
	 public ExperienceController() {
        super();
    }
	
	public void addExperience(String name, Experience experience){
		experiences.put(name,experience);
	}
	
	public void loadExperience(String name, Experience e){
		e.setParent(this);
		addExperience(name, e);
	}
	
	public void setExperience(String name){
		
		if(experiences.get(name) != null){
			
			final DoubleProperty opacity = opacityProperty(); 
			
			if(!getChildren().isEmpty()){
				
				// There is an Experience going lets fade out OLD and then fade in NEW

				Timeline fade = new Timeline( 
				   new KeyFrame(Duration.ZERO, 
								new KeyValue(opacity,1.0)), 
				   new KeyFrame(new Duration(1000), 

					   new EventHandler<ActionEvent>() { 

						 @Override 
						 public void handle(ActionEvent t) { 
						   //Remove displayed Experience 
						   getChildren().remove(0); 
						   //Add the new Experience and Fade In 
						  	getChildren().add(0, experiences.get(name).getNode()); 
						   Timeline fadeIn = new Timeline( 
							   new KeyFrame(Duration.ZERO, 
									  new KeyValue(opacity, 0.0)), 
							   new KeyFrame(new Duration(800), 
									  new KeyValue(opacity, 1.0))); 
						   fadeIn.play(); 
						 } 
					   }, new KeyValue(opacity, 0.0))); 
				 fade.play(); 
				experiences.get(name).startExperience();
			
			}else{
			
				// No Experience currently Viewable
				setOpacity(0.0); 

				//Add the new Experience and Fade In 
				this.getChildren().add(0, experiences.get(name).getNode()); 
			   	Timeline fadeIn = new Timeline( 
				   new KeyFrame(Duration.ZERO, 
						  new KeyValue(opacity, 0.0)), 
				   new KeyFrame(new Duration(800), 
						  new KeyValue(opacity, 1.0))); 
			   	fadeIn.play(); 
				experiences.get(name).startExperience();
			}
			

		}else{
			System.out.println("Experience could not be loaded."
								+ "No Experience by this name.");
		}
		
	}
	
	public void unloadExperience(String name) { 
     if(experiences.remove(name) == null) { 
       System.out.println("Screen didn't exist. Unload Unsuccessful."); 
     }
   } 
	
}
