package experienceController;

/*
 *
 *	This interface just makes sure that all of the Experience can set their Parent.
 *		
 *	DATE INITIALLY CREATED:
 *		03/02/15 - (Brandon Caruso)
 *		
 */

import javafx.scene.Node;

public interface Experience {

	public void setParent(ExperienceController controller);

	public void startExperience();

	public void stopExperience();

	public Node getNode();

}