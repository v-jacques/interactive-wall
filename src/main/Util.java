package main;

import javafx.scene.layout.Pane;

import com.leapmotion.leap.Hand;

import experience.mainmenu.MainMenuExperience;

public class Util {
	// checks if c is between a and b
	public static boolean isBetween(int a, int b, int c) {
		return b > a ? c > a && c < b : c > b && c < a;
	}

	public static double palmXToPanelX(Hand hand, Pane pane) {
		return Math.round(hand.palmPosition().getX() + (pane.getWidth() / 2));
	}

	public static double palmYToPanelY(Hand hand, Pane pane) {
		return (-1 * (Math.round(hand.palmPosition().getY())))
				+ pane.getHeight();
	}
}
