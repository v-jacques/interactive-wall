package confirm;
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
		ConfirmDemo.h = hands.get(0);
		ConfirmDemo.p.repaint();
		if (Math.round(ConfirmDemo.h.palmPosition().getX()) - 15
				+ (ConfirmDemo.frameWidth / 2) >= 0
				&& Math.round(ConfirmDemo.h.palmPosition().getX()) - 15
						+ (ConfirmDemo.frameWidth / 2) <= 100
				&& (-1 * (Math.round(ConfirmDemo.h.palmPosition().getY())))
						- 15 + ConfirmDemo.frameHeight >= 0
				&& (-1 * (Math.round(ConfirmDemo.h.palmPosition().getY())))
						- 15 + ConfirmDemo.frameHeight <= 100) {
			ConfirmDemo.t.start();
		} else {
			ConfirmDemo.t.stop();
		}
	}
}