package experience.block;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.*;
import org.jbox2d.common.Vec2;


public class WorldModelBootstrap {
	public static void main(String[] args){
		Vec2 gravity = new Vec2(0.0f,-10.0f);
		boolean doSleep = true;
	
		World world = new World(gravity,doSleep);
	
		BodyDef bd = new BodyDef();
		bd.position.set(50, 50);  
		bd.type = BodyType.DYNAMIC;
	
		PolygonShape ps = new PolygonShape();
		ps.setAsBox(2, 2);
	
		FixtureDef fd = new FixtureDef();
		fd.shape = ps;
		fd.density = 0.5f;
		fd.friction = 0.3f;        
		fd.restitution = 0.1f;
	
		Body body =  world.createBody(bd);
		body.createFixture(fd);
	
		float timeStep = 1.0f / 60.f;
		int velocityIterations = 6;
		int positionIterations = 2;
 
		for (int i = 0; i < 60; ++i) {
			world.step(timeStep, velocityIterations, positionIterations);
				 Vec2 position = body.getPosition();
				float angle = body.getAngle();
				System.out.printf("%4.2f %4.2f %4.2f\n", position.x, position.y, angle);
		}
	}
}