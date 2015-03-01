package mainmenu;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.Timer;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Hand;

class MainMenuDemo {
	public static Hand h;
	public static PanelMainMenu p;
	public static Timer t1;
	public static Timer t2;
	public static Timer t3;
	public static Timer t4;

	public static int frameWidth = 800;
	public static int frameHeight = 600;

	public static void main(String[] args) {
		Controller controller = new Controller();
		SampleListener listener = new SampleListener();
		controller.addListener(listener);
		t1 = new Timer(3000, new TimerActionListener("1st Experience"));
		t2 = new Timer(3000, new TimerActionListener("2nd Experience"));
		t3 = new Timer(3000, new TimerActionListener("3rd Experience"));
		t4 = new Timer(3000, new TimerActionListener("4th Experience"));

		JFrame frame = new JFrame();
		frame.setPreferredSize(new Dimension(frameWidth, frameHeight));
		p = new PanelMainMenu();
		p.setPreferredSize(new Dimension(frameWidth, frameHeight));
		frame.add(p);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		controller.removeListener(listener);
	}
}
