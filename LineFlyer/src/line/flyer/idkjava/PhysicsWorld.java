package line.flyer.idkjava;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import android.util.Log;

public class PhysicsWorld
{
	public int targetFPS = 40;
	public float timeStep = (1.0f / targetFPS);
	public int iterations = 5;

	private Body[] bodies;
	private int count = 0;

	private AABB worldAABB;
	private World world;
	private BodyDef groundBodyDef;
	private PolygonDef groundShapeDef;

	//NOTE: Everything is to scale of 1/10 becuase the physics engine wants it that way.  
	public void create()
	{
		bodies = new Body[10000];
		// Step 1: Create Physics World Boundaries
		worldAABB = new AABB();
		worldAABB.lowerBound.set(new Vec2((float) -100.0, (float) -100.0));
		worldAABB.upperBound.set(new Vec2((float) 100.0, (float) 100.0));

		// Step 2: Create Physics World with Gravity
		Vec2 gravity = new Vec2((float) 0.0, (float) -10.0);
		boolean doSleep = true;
		world = new World(worldAABB, gravity, doSleep);

		/*
		 * // Step 3: Create Ground Box groundBodyDef = new BodyDef();
		 * groundBodyDef.position.set(new Vec2((float) 0.0, (float) -10.0));
		 * Body groundBody = world.createBody(groundBodyDef); groundShapeDef =
		 * new PolygonDef(); groundShapeDef.setAsBox((float) 400.0, (float)
		 * 110.0); groundBody.createShape(groundShapeDef);
		 */
	}

	public void addBall()
	{
		// Create Dynamic Body
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set((float) 6.0 + count, (float) 24.0);
		bodies[count] = world.createBody(bodyDef);

		// Create Shape with Properties
		CircleDef circle = new CircleDef();
		circle.radius = (float) 1.8;
		circle.density = (float) 1.0;

		// Assign shape to Body
		bodies[count].createShape(circle);
		bodies[count].setMassFromShapes();

		// Increase Counter
		count += 1;
	}

	public void addSled(float xpos, float ypos)
	{
		xpos *= 0.1; //Replaced (float) 1.0 / 10.0 because this is more efficient
		ypos *= 0.1;
		// Create Dynamic Body
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(xpos, ypos);
		bodies[count] = world.createBody(bodyDef);

		float ymost = (float) (0.1 * MainView.pHeight); //Replaced 1.0 / 10.0 with 0.1 again
		float xmost = (float) (0.1 * MainView.pWidth);
		// Create Shape with Properties
		PolygonDef sled = new PolygonDef();

		sled.setAsBox(xmost / 2, ymost / 2, new Vec2(xpos, ypos), 0);

		sled.density = 1.0f;

		sled.restitution = 1.0f;

		// Assign shape to Body
		bodies[count].createShape(sled);
		bodies[count].setMassFromShapes();
		bodies[count].setAngularVelocity(3.0f);

		// Increase Counter
		count += 1;
	}

	public void createLine(Vec2 startpos, float length, float angle)
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(startpos.x * 0.1f, startpos.y * 0.1f);
		bodies[count] = world.createBody(bodyDef);

		float xlength = 0.1f * length;
		float ylength = 0.1f;

		PolygonDef line = new PolygonDef();

		line.setAsBox(xlength/2, ylength/2, new Vec2((float) Math.cos(angle) * length, (float) Math.sin(angle) * length), angle);

		bodies[count].createShape(line);

		count += 1;
	}

	public void update()
	{
		// Update Physics World
		world.step(timeStep, iterations);

		MainView.pCenMassx = 10 * (bodies[0].getWorldCenter()).x;
		MainView.pCenMassy = 10 * (bodies[0].getWorldCenter()).y;
		MainView.paor = (bodies[0].getAngle());

		Log.v("DemoActivity", "paor:" + MainView.paor);
	}
}