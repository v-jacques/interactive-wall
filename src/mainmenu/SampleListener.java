package mainmenu;

import com.leapmotion.leap.*;

class SampleListener extends Listener {

	public void onConnect(Controller controller) {
		System.out.println("Connected");
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
	}

	public void onFrame(Controller controller) {
		Frame frame = controller.frame();

		HandList hands = frame.hands();
		MainMenuExperience.h = hands.get(0);
		MainMenuExperience.p.repaint();

		if (Util.isBetween(200, 400, Util.palmXToPanelX())
				&& Util.isBetween(100, 300, Util.palmYToPanelY())) {
			MainMenuExperience.t1.start();
		} else {
			MainMenuExperience.t1.stop();
		}

		if (Util.isBetween(400, 600, Util.palmXToPanelX())
				&& Util.isBetween(100, 300, Util.palmYToPanelY())) {
			MainMenuExperience.t2.start();
		} else {
			MainMenuExperience.t2.stop();
		}

		if (Util.isBetween(200, 400, Util.palmXToPanelX())
				&& Util.isBetween(300, 500, Util.palmYToPanelY())) {
			MainMenuExperience.t3.start();
		} else {
			MainMenuExperience.t3.stop();
		}

		if (Util.isBetween(400, 600, Util.palmXToPanelX())
				&& Util.isBetween(300, 500, Util.palmYToPanelY())) {
			MainMenuExperience.t4.start();
		} else {
			MainMenuExperience.t4.stop();
		}
	}

}