package experience.block;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.*;
import javafx.util.Duration;

public class Block {
	
	public Node node;
	
	private float posX; //meters;
	private float posY; //meters;
	
	private double width;  //meters;
	private double height; //meters;
	
	
	public Block(float x, float y, double width ,double height) {
		this.posX = x;
		this.posY = y;
		this.width = width;
		this.height = height;
		node = create();
	}
	
	public void setPoints(float x, float y) {
		this.posX = x;
		this.posY = y;
	}
	
	private Node create() {
		
		//creating the block
		Rectangle block = new Rectangle();
		block.setWidth(WorldModel.fromMetersToPixels((float)width));
		block.setHeight(WorldModel.fromMetersToPixels((float)height));
		block.setArcWidth(WorldModel.fromMetersToPixels((float)width)*.1f);
		block.setArcHeight(WorldModel.fromMetersToPixels((float)width)*.1f);
		LinearGradient lg1;
		if(Math.random() < .5f){
		
			block.setStroke(Color.GOLD);
		
			Stop[] stops = new Stop[] { new Stop(0, Color.DIMGREY),new Stop(1, Color.LIGHTGREY)};
        	lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.REFLECT, stops);
		}else{
			block.setStroke(Color.GREY);
		
			Stop[] stops = new Stop[] { new Stop(0, Color.GOLDENROD),new Stop(1, Color.LIGHTGOLDENRODYELLOW)};
        	lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.REFLECT, stops);
		}
		
		block.setFill(lg1);
		
		block.setStrokeWidth(1.5);
		
		//setting block position for JavaFX
		block.setLayoutX(WorldModel.fromJPosXToSPosX((float)posX));
		block.setLayoutY(WorldModel.fromJPosYToSPosY((float)posY));
		
		block.setCache(true);
		
		//setting block info for JBox2
		BodyDef bd = new BodyDef();
		bd.type = BodyType.DYNAMIC;
		bd.position.set(posX, posY);
		
		PolygonShape ps = new PolygonShape();
		ps.setAsBox((float)width*.5f,(float)height*.5f);
		
		FixtureDef fd = new FixtureDef();
		fd.shape = ps;
		fd.density = 0.9f;
		fd.friction = 0.7f;
		fd.restitution = 0.1f;
		
		Body body = WorldModel.WORLD.createBody(bd);
		body.createFixture(fd);
		block.setUserData(body);
		
		return block;
	}
}