package experience.firework;

import javafx.application.Application;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.scene.effect.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import java.util.*;
import javafx.animation.*;
import javafx.scene.canvas.*;
import javafx.scene.effect.Reflection;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import java.io.File;
import javafx.event.EventHandler;


public class Particle {
        private static final double GRAVITY = 0.06;
        // properties for animation
        // and colouring
        double alpha;
        final double easing;
        double fade;
        double posX;
        double posY;
        double velX;
        double velY;
        final double targetX;
        final double targetY;
        final Paint color;
        final int size;
        final boolean usePhysics;
        final boolean shouldExplodeChildren;
        final boolean hasTail;
        double lastPosX;
        double lastPosY;
		String type;
       
        public Particle(double posX, double posY, double velX, double velY, double targetX, double targetY,
                Paint color, int size, boolean usePhysics, boolean shouldExplodeChildren, boolean hasTail, String type) {
            this.posX = posX;
            this.posY = posY;
            this.velX = velX;
            this.velY = velY;
            this.targetX = targetX;
            this.targetY = targetY;
            this.color = color;
            this.size = size;
            this.usePhysics = usePhysics;
            this.shouldExplodeChildren = shouldExplodeChildren;
            this.hasTail = hasTail;
            this.alpha    = 1;
            this.easing   = Math.random() * 0.02;
            this.fade     = .001 + Math.random() * 0.1;
						this.type = type;
        }

        public boolean update() {
            lastPosX = posX;
            lastPosY = posY;
            if(this.usePhysics) { // on way down
				if (this.type=="willow") {
					velY += GRAVITY + .18;
				} else if (this.type=="ring") {
					double g = GRAVITY * .01;
					velY += g; 
				} else {
					velY += GRAVITY;
				}
				if (this.type=="ring") {
					posY += velY*.001;
					posX += velX*.001;
					this.alpha -= this.fade*3;
				} else if (this.type=="willow") {
                	posY += velY*.7;
					posX += velX;
					this.alpha -= this.fade;
				} else {
                	posY += velY;
					posX += velX;
					this.alpha -= this.fade;
				}
            } else { // on way up
				double x_dist = (targetX - posX);
				posX += x_dist * (0.03 + easing);
                double distance = (targetY - posY);
                // ease the position
                posY += distance * (0.03 + easing);
                // cap to 1
                alpha = Math.min(distance * distance * 0.00005, 1);
            }
            //posX += velX;
            return alpha < 0.005;
        }

        public void draw(GraphicsContext context) {
            final double x = Math.round(posX);
            final double y = Math.round(posY);
            final double xVel = (x - lastPosX) * -5;
            final double yVel = (y - lastPosY) * -5;
            // set the opacity for all drawing of this particle
            context.setGlobalAlpha(Math.random() * this.alpha);
            // draw particle
            context.setFill(color);
            context.fillOval(x-size, y-size, size+size, size+size);
            // draw the arrow triangle from where we were to where we are now
            if (hasTail) {
                context.setFill(Color.rgb(255,255,255,0.3));
                context.fillPolygon(new double[]{posX + 1.5,posX + xVel,posX - 1.5},
                        new double[]{posY,posY + yVel,posY}, 3);
            }
        }
    }