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
		MainMenuDemo.h = hands.get(0);
		MainMenuDemo.p.repaint();

		if (Util.isBetween(200, 400, Util.palmXToPanelX())
				&& Util.isBetween(100, 300, Util.palmYToPanelY())) {
			MainMenuDemo.t1.start();
		} else {
			MainMenuDemo.t1.stop();
		}

		if (Util.isBetween(400, 600, Util.palmXToPanelX())
				&& Util.isBetween(100, 300, Util.palmYToPanelY())) {
			MainMenuDemo.t2.start();
		} else {
			MainMenuDemo.t2.stop();
		}

		if (Util.isBetween(200, 400, Util.palmXToPanelX())
				&& Util.isBetween(300, 500, Util.palmYToPanelY())) {
			MainMenuDemo.t3.start();
		} else {
			MainMenuDemo.t3.stop();
		}

		if (Util.isBetween(400, 600, Util.palmXToPanelX())
				&& Util.isBetween(300, 500, Util.palmYToPanelY())) {
			MainMenuDemo.t4.start();
		} else {
			MainMenuDemo.t4.stop();
		}
	}

}