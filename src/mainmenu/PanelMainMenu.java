package mainmenu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PanelMainMenu extends JPanel {
	private static final long serialVersionUID = 1L;

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);

		g2d.clearRect(0, 0, MainMenuExperience.frameWidth, MainMenuExperience.frameHeight);
		g2d.drawString("palm x: " + MainMenuExperience.h.palmPosition().getX(), 20,
				20);
		g2d.drawString("palm y: " + MainMenuExperience.h.palmPosition().getY(), 20,
				35);
		g2d.drawString("panel x: " + Util.palmXToPanelX(), 20, 50);
		g2d.drawString("panel y: " + Util.palmYToPanelY(), 20, 65);

		if (MainMenuExperience.t1.isRunning())
			g.setColor(Color.gray);
		else
			g.setColor(Color.red);
		g2d.fillRect(200, 100, 200, 200);

		if (MainMenuExperience.t2.isRunning())
			g.setColor(Color.gray);
		else
			g.setColor(Color.green);
		g2d.fillRect(400, 100, 200, 200);

		if (MainMenuExperience.t3.isRunning())
			g.setColor(Color.gray);
		else
			g.setColor(Color.black);
		g2d.fillRect(200, 300, 200, 200);

		if (MainMenuExperience.t4.isRunning())
			g.setColor(Color.gray);
		else
			g.setColor(Color.blue);
		g2d.fillRect(400, 300, 200, 200);

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("Mushroom.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		g2d.drawImage(img, Util.palmXToPanelX() - 20,
				Util.palmYToPanelY() - 20, 40, 40, null);
	}
}
