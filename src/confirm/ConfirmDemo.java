package confirm;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.Timer;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Hand;

class ConfirmDemo {
	public static Hand h;
	public static PanelConfirm p;
	public static Timer t;

	public static int frameWidth = 300;
	public static int frameHeight = 300;

	public static void main(String[] args) {
		Controller controller = new Controller();
		SampleListener listener = new SampleListener();

		System.out.println("Press Enter to quit...");
		t = new Timer(3000, new TimerActionListener());

		controller.addListener(listener);

		JFrame frame = new JFrame();
		frame.setPreferredSize(new Dimension(frameWidth, frameHeight));
		p = new PanelConfirm();
		p.setPreferredSize(new Dimension(frameWidth, frameHeight));
		p.setBackground(Color.orange);
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