package main;

import javafx.scene.layout.Pane;

import com.leapmotion.leap.Hand;

public class Util {
	// checks if c is between a and b
	public static boolean isBetween(int a, int b, int c) {
		return b > a ? c > a && c < b : c > b && c < a;
	}

	public static double palmXToPanelX(Hand hand, Pane pane) {
		double origStart = -150;
		double origEnd = 150;
		double newStart = 0;
		double newEnd = 1600;
		double scale = (newEnd - newStart) / (origEnd - origStart);
		return (newStart + ((hand.palmPosition().getX() - origStart) * scale));
	}

	public static double palmYToPanelY(Hand hand, Pane pane) {
		double origStart = 400;
		double origEnd = 30;
		double newStart = 0;
		double newEnd = 1000;
		double scale = (newEnd - newStart) / (origEnd - origStart);
		return (newStart + ((hand.palmPosition().getY() - origStart) * scale));
	}
}
