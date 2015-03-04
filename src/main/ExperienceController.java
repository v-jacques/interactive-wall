package main;

/*
 *
 *	This controller is what swaps out the different Experiences.
 *		
 *	DATE INITIALLY CREATED:
 *		03/02/15 - (Brandon Caruso)
 *		
 */

import java.util.HashMap;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class ExperienceController extends StackPane {

	// This will contain all the diffrent Experiences to be moved between.
	public HashMap<String, Experience> experiences = new HashMap<>();
	Experience current;

	public ExperienceController() {
		super();
	}

	public void addExperience(String name, Experience experience) {
		experiences.put(name, experience);
	}

	public void loadExperience(String name, Experience e) {
		e.setParent(this);
		addExperience(name, e);
	}

	public void setExperience(String name) {

		if (experiences.get(name) != null) {

			final DoubleProperty opacity = opacityProperty();

			if (!getChildren().isEmpty()) {

				// There is an Experience going lets fade out OLD and then fade
				// in NEW

				Timeline fade = new Timeline(new KeyFrame(Duration.ZERO,
						new KeyValue(opacity, 1.0)), new KeyFrame(new Duration(
						1000), ae -> {
					current.stopExperience();
					// Remove displayed Experience
						getChildren().remove(0);
						fadeInNewExperience(name, opacity);

					}, new KeyValue(opacity, 0.0)));
				fade.play();

			} else {

				// No Experience currently Viewable
				setOpacity(0.0);

				// Add the new Experience and Fade In
				fadeInNewExperience(name, opacity);

			}

		} else {
			System.out.println("Experience could not be loaded."
					+ "No Experience by this name.");
		}

	}

	public void unloadExperience(String name) {
		if (experiences.remove(name) == null) {
			System.out.println("Screen didn't exist. Unload Unsuccessful.");
		}
	}

	private void doExperienceSwap(String name) {
		experiences.get(name).startExperience();
		current = experiences.get(name);
	}

	private void fadeInNewExperience(String name, DoubleProperty opacity) {
		getChildren().add(0, experiences.get(name).getNode());
		Timeline fadeIn = new Timeline(new KeyFrame(Duration.ZERO,
				new KeyValue(opacity, 0.0)), new KeyFrame(new Duration(800),
				ae -> doExperienceSwap(name), new KeyValue(opacity, 1.0)));
		fadeIn.play();
	}

}
