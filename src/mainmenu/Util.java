package mainmenu;

public class Util {
	// checks if c is between a and b
	public static boolean isBetween(int a, int b, int c) {
		return b > a ? c > a && c < b : c > b && c < a;
	}

	public static int palmXToPanelX() {
		return Math.round(MainMenuDemo.h.palmPosition().getX() - 15
				+ (MainMenuDemo.frameWidth / 2));
	}

	public static int pamlYToPanelY() {
		return (-1 * (Math.round(MainMenuDemo.h.palmPosition().getY()))) - 15
				+ MainMenuDemo.frameHeight;
	}
}
