package confirm;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PanelConfirm extends JPanel {
	private static final long serialVersionUID = 1L;

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);

		g2d.drawRect(0, 0, 100, 100);
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("Mushroom.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		g2d.drawImage(img, Math.round(ConfirmDemo.h.palmPosition().getX())
				- 15 + (ConfirmDemo.frameWidth / 2),
				(-1 * (Math.round(ConfirmDemo.h.palmPosition().getY()))) - 15
						+ ConfirmDemo.frameHeight, 40, 40, null);

		// g2d.setColor(Color.RED);
		// g2d.fillOval(Math.round(ConfrimDemo.h.palmPosition().getX())-15 +
		// (ConfrimDemo.frameWidth/2),
		// (-1*(Math.round(ConfrimDemo.h.palmPosition().getY()))) -15 +
		// ConfrimDemo.frameHeight, 30, 30);
	}
}
