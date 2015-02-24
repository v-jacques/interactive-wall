import com.leapmotion.leap.*;

class SampleListener extends Listener {

	public void onConnect(Controller controller) {
		System.out.println("Connected");
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
	}

	public void onFrame(Controller controller) {
		Frame frame = controller.frame();

		System.out.println("Frame id: " + frame.id() + ", timestamp: "
				+ frame.timestamp() + ", hands: " + frame.hands().count()
				+ ", fingers: " + frame.fingers().count() + ", tools: "
				+ frame.tools().count() + ", gestures "
				+ frame.gestures().count());
	}
}