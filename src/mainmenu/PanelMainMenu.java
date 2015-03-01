package mainmenu;

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

		g2d.clearRect(0, 0, MainMenuDemo.frameWidth, MainMenuDemo.frameHeight);

		g2d.drawRect(200, 100, 200, 200);
		g2d.drawRect(400, 300, 200, 200);
		g2d.drawRect(400, 100, 200, 200);
		g2d.drawRect(200, 300, 200, 200);

		g2d.drawString("x: " + MainMenuDemo.h.palmPosition().getX(), 20, 20);
		g2d.drawString("y: " + MainMenuDemo.h.palmPosition().getY(), 20, 35);

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("Mushroom.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		g2d.drawImage(img, Util.palmXToPanelX(), Util.pamlYToPanelY(), 40, 40,
				null);
	}
}
