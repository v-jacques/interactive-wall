import com.leapmotion.leap.*;

import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;
import java.awt.event.*;
import java.awt.RenderingHints;


class ConfrimDemo {
	public static Hand h;
	public static MyPanel p;
	public static Timer t;
	
	public static int frameWidth = 300;
	public static int frameHeight = 300;
	
	public static void main(String[] args){
		Controller controller = new Controller();
		SampleListener listener = new SampleListener();

		System.out.println("Press Enter to quit...");
		t = new Timer( 3000 ,new MyTimerActionListener());
		
		controller.addListener(listener);
		
		JFrame frame = new JFrame();
		frame.setPreferredSize(new Dimension(frameWidth,frameHeight));
		p = new MyPanel();
		p.setPreferredSize(new Dimension(frameWidth,frameHeight));
		p.setBackground(Color.orange);
		frame.add(p);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		try{
		 	System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		controller.removeListener(listener);
	}
}

class SampleListener extends Listener {
	public void onConnect(Controller controller){
		System.out.println("Connected");
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
	}
	
	public void onFrame(Controller controller){
		//System.out.println("Frame available");
		Frame frame = controller.frame();
		
		HandList hands = frame.hands();
		ConfrimDemo.h = hands.get(0);
		ConfrimDemo.p.repaint();
		if(Math.round(ConfrimDemo.h.palmPosition().getX())-15 + (ConfrimDemo.frameWidth/2) >= 0 &&
			Math.round(ConfrimDemo.h.palmPosition().getX())-15 + (ConfrimDemo.frameWidth/2) <= 100&&
			(-1*(Math.round(ConfrimDemo.h.palmPosition().getY()))) -15 + ConfrimDemo.frameHeight >= 0&&
			(-1*(Math.round(ConfrimDemo.h.palmPosition().getY()))) -15 + ConfrimDemo.frameHeight <= 100){
			ConfrimDemo.t.start();
		}else{
			ConfrimDemo.t.stop();
		}
	}
}
	
class MyPanel extends JPanel{


		@Override
		public void paint(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(
    		RenderingHints.KEY_ANTIALIASING,
    		RenderingHints.VALUE_ANTIALIAS_OFF);
    		
    		g2d.drawRect(0,0,100,100);
			BufferedImage img = null;
			try {
				img = ImageIO.read(new File("Mushroom.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			g2d.drawImage(img,
                  	Math.round(ConfrimDemo.h.palmPosition().getX())-15 + (ConfrimDemo.frameWidth/2), (-1*(Math.round(ConfrimDemo.h.palmPosition().getY()))) -15 + ConfrimDemo.frameHeight
                   ,40,40,null);
			
			//g2d.setColor(Color.RED);
			//g2d.fillOval(Math.round(ConfrimDemo.h.palmPosition().getX())-15 + (ConfrimDemo.frameWidth/2), (-1*(Math.round(ConfrimDemo.h.palmPosition().getY()))) -15 + ConfrimDemo.frameHeight, 30, 30);
	}
}

class MyTimerActionListener implements ActionListener {
  public void actionPerformed(ActionEvent e) {

    System.out.println("Confirmed");

  }
}
	