package mainmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimerActionListener implements ActionListener {
	String experience;

	public TimerActionListener(String string) {
		experience = string;
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println(experience + " confirmed.");
	}
}
