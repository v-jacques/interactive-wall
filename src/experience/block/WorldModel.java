package experience.block;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.*;
import org.jbox2d.common.Vec2;


public class WorldModel {

	// Create the World 
	
	public static final World WORLD = new World(new Vec2(0.0f, -10.0f),true);
	
	// Screen width and height
		
	public static final int WIDTH = 1600;
    public static final int HEIGHT = 1000;
     
    //Block dimensions in pixel
    public static final int BLOCK_WIDTH_DIMENSIONS = 1;
	public static final int BLOCK_HEIGHT_DIMENSIONS = 1;	
	
	//Build a WALL
	public static void addWall(float posX, float posY, float width, float height){
		PolygonShape ps = new PolygonShape();
		ps.setAsBox(width,height);
		 
		FixtureDef fd = new FixtureDef();
		fd.shape = ps;
 
		BodyDef bd = new BodyDef();
		bd.position= new Vec2(posX ,posY);
 
		WorldModel.WORLD.createBody(bd).createFixture(fd);
	}
	
	public static final float fromMetersToPixels(float x){
		return x*100;
	}
	
	public static final float fromPixelsToMeters(float x){
		return x/100;
	}
	
	public static final float fromJPosXToSPosX(float x){
		return x * 100;
	}
	
	public static final float fromJPosYToSPosY(float y){
		return HEIGHT - (y*100);
	}
}