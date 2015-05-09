package main;

import javafx.scene.layout.Pane;

import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Finger;

public class Util {
	// checks if c is between a and b
	public static boolean isBetween(int a, int b, int c) {
		return b > a ? c > a && c < b : c > b && c < a;
	}
	
	public static boolean isBetween(double a, double b, double c) {
		return b > a ? c > a && c < b : c > b && c < a;
	}

	public static double palmXToPanelX(Hand hand) {
		double origStart = -150;
		double origEnd = 150;
		double newStart = 0;
		double newEnd = 1600;
		double scale = (newEnd - newStart) / (origEnd - origStart);
		return (newStart + ((hand.palmPosition().getX() - origStart) * scale));
	}

	public static double palmYToPanelY(Hand hand) {
		double origStart = 400;
		double origEnd = 30;
		double newStart = 0;
		double newEnd = 1000;
		double scale = (newEnd - newStart) / (origEnd - origStart);
		return (newStart + ((hand.palmPosition().getY() - origStart) * scale));
	}
	
	public static double pointXToPanelX(Finger finger) {
		double origStart = -150;
		double origEnd = 150;
		double newStart = 0;
		double newEnd = 1600;
		double scale = (newEnd - newStart) / (origEnd - origStart);
		return (newStart + ((finger.stabilizedTipPosition().getX() - origStart) * scale));
	}

	public static double pointYToPanelY(Finger finger) {
		double origStart = 400;
		double origEnd = 30;
		double newStart = 0;
		double newEnd = 1000;
		double scale = (newEnd - newStart) / (origEnd - origStart);
		return (newStart + ((finger.stabilizedTipPosition().getY() - origStart) * scale));
	}
    
    public static double leapXtoPanelX(double x) {
		double origStart = -140;
		double origEnd = 140;
		double newStart = 0;
		double newEnd = 1600;
		double scale = (newEnd - newStart) / (origEnd - origStart);
		return (newStart + ((x - origStart) * scale));
	}

	public static double leapYToPanelY(double y) {
		double origStart = 390;
		double origEnd = 40;
		double newStart = 0;
		double newEnd = 1000;
		double scale = (newEnd - newStart) / (origEnd - origStart);
		return (newStart + ((y - origStart) * scale));
	}
}
