package mainmenu;

public class Util {
	// checks if c is between a and b
	public static boolean isBetween(int a, int b, int c) {
		return b > a ? c > a && c < b : c > b && c < a;
	}

	public static int palmXToPanelX() {
		return Math.round(MainMenuExperience.h.palmPosition().getX()
				+ (MainMenuExperience.frameWidth / 2));
	}

	public static int palmYToPanelY() {
		return (-1 * (Math.round(MainMenuExperience.h.palmPosition().getY())))
				+ MainMenuExperience.frameHeight;
	}
}
