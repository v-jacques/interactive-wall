package mainmenu;

import com.leapmotion.leap.*;

class SampleListener extends Listener {

	public void onConnect(Controller controller) {
		System.out.println("Connected");
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
	}

	public void onFrame(Controller controller) {
		// System.out.println("Frame available");
		Frame frame = controller.frame();

		HandList hands = frame.hands();
		MainMenuDemo.h = hands.get(0);
		MainMenuDemo.p.repaint();
		if (Math.round(MainMenuDemo.h.palmPosition().getX()) - 15
				+ (MainMenuDemo.frameWidth / 2) >= 0
				&& Math.round(MainMenuDemo.h.palmPosition().getX()) - 15
						+ (MainMenuDemo.frameWidth / 2) <= 100
				&& (-1 * (Math.round(MainMenuDemo.h.palmPosition().getY())))
						- 15 + MainMenuDemo.frameHeight >= 0
				&& (-1 * (Math.round(MainMenuDemo.h.palmPosition().getY())))
						- 15 + MainMenuDemo.frameHeight <= 100) {
			MainMenuDemo.t.start();
		} else {
			MainMenuDemo.t.stop();
		}
	}

}