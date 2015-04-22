package main;

public class Util {
	// checks if c is between a and b
	public static boolean isBetween(int a, int b, int c) {
		return b > a ? c > a && c < b : c > b && c < a;
	}

	public static boolean isBetween(double a, double b, double c) {
		return b > a ? c > a && c < b : c > b && c < a;
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
