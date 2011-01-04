package line.flyer.idkjava;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.jbox2d.common.Vec2;

public class MainView extends View
{
	// player characteristics
	public static final float pHeight = 32.00f;
	public static final float pWidth = 32.00f;
	public static final float pMass = 1.00f;
	public static final float mInertia = pMass * (pHeight * pHeight + pWidth * pWidth) / 12.0f; // I = m(w^2 + h^2)/12

	// force of gravity
	//private static final float fgravity = 0.1f;

	// Terminal velocity
	//private static final int termv = 50;

	private static final int maxlines = 10000;
	
	//Paint describes settings used when drawing on Canvas
	private static Paint paint;
	
	//Values for calculations
	private static float offx = 0;
	private static float offy = 0;
	private static float diffx = 0;
	private static float diffy = 0;

	//Arrays for lines
	private static float[] startx;
	private static float[] starty;
	private static float[] endx;
	private static float[] endy;
	private static float[] normal;

	private static boolean[] colliding;

	// variables for old location
	//private static float pOldx; // Old x
	//private static float pOldy; // Old y
	//private static float pOldr; // Old r

	//private static float pxv; // player linear x velocity
	//private static float pyv; // player linear y velocity
	public static float paor; // player angle of rotation, in radians
	//private static float prv; // player rotational velocity
	public static float pCenMassx; // center of mass x coord
	public static float pCenMassy; // center of mass y coord

	public static Drawable pNormImg; // normal player image

	// start value for dragging
	public static float sx;
	public static float sy;

	public static int waiter = 0;

	// last value for line making
	public static int lx;
	public static int ly;

	//global ix and iy
	public static float ixg;
	public static float iyg;

	// marker for what line we're on, prolly will have to change this later on
	// to be like sand game, but im lazy right now
	public static int cline;

	PhysicsWorld mWorld;
	private Handler mHandler;

	public MainView(Context context, AttributeSet attrs)
	{
		super(context);
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(3);
		mWorld = new PhysicsWorld();
		mWorld.create();

		mWorld.addSled((float) 20.0, (float) 50.0);
		// Start Regular Update
		mHandler = new Handler();
		mHandler.post(update);
	}

	private Runnable update = new Runnable()
	{
		public void run()
		{
			mWorld.update();
			mHandler.postDelayed(update, (long) (mWorld.timeStep * 1000));
		}
	};

	protected void onPause()
	{
		mHandler.removeCallbacks(update);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{

		// draw lines
		canvas.translate(offx, offy);
		canvas.drawColor(Color.GRAY);
		canvas.drawLines(normal, paint);

		paint.setColor(Color.RED);
		canvas.drawPoint(ixg, iyg, paint);
		paint.setColor(Color.BLACK);

		// save the current canvas to restore it after we're done drawing the
		// line guy
		canvas.save();

		// rotate the canvas to the angle at which the player is currently
		// rotated
		canvas.rotate((float) Math.toDegrees(paor), pCenMassx, pCenMassy);

		// set region to draw the player
		pNormImg.setBounds((int) (pCenMassx - (pWidth / 2)), (int) (pCenMassy - (pHeight / 2)), (int) (pCenMassx + (pWidth / 2)), (int) (pCenMassy + (pHeight / 2)));

		// draw the player
		pNormImg.draw(canvas);

		// un-rotate the canvas
		canvas.restore();

		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		float x, y;
		int action;
		x = event.getX() - offx;
		y = event.getY() - offy;
		action = event.getAction();

		switch (LineFlyer.mode)
		{
		case 1:
			if (action == MotionEvent.ACTION_DOWN)
			{
				lx = (int) x;
				ly = (int) y;
			}
			else if (action == MotionEvent.ACTION_MOVE)
			{
				if ((x - lx) * (x - lx) + (y - ly) * (y - ly) >= 400)
				{
					makeline(lx, ly, (int) x, (int) y, 0);

					lx = (int) x;
					ly = (int) y;
				}

			}
			break;
		case 2:
			if (action == MotionEvent.ACTION_DOWN)
			{
				sx = x + offx;
				sy = y + offy;
			}
			else if (action == MotionEvent.ACTION_MOVE)
			{
				diffx = x + offx - sx;
				sx = x + offx;
				// Need to do this after to not screw up the sx variable
				offx += diffx;

				diffy = y + offy - sy;
				sy = y + offy;
				// Need to do this after to not screw up the sy variable
				offy += diffy;
			}

		}

		return true;
	}

	public static void setup()
	{
		cline = 0;
		startx = new float[maxlines];
		starty = new float[maxlines];
		endx = new float[maxlines];
		endy = new float[maxlines];
		normal = new float[maxlines * 4];
		colliding = new boolean[maxlines];
		for (int i = 0; i < maxlines; i++)
		{
			startx[i] = 10000;
			starty[i] = 10000;
			endx[i] = 10000;
			endy[i] = 10000;
			colliding[i] = false;
		}
		for (int j = 0; j < maxlines * 4; j++)
		{
			normal[j] = 10000;
		}
		//pxv = 0;
		//pyv = 0;
		paor = 0;
		//prv = 0;
		pCenMassx = 20;
		pCenMassy = 20;
	}

	public void makeline(int stx, int sty, int enx, int eny, int type)
	{
		switch (type)
		{
		}

		// draw a line, later need to use switch for different types of lines
		startx[cline] = stx;
		starty[cline] = sty;
		endx[cline] = enx;
		endy[cline] = eny;
		normal[(cline * 4)] = stx;
		normal[(cline * 4) + 1] = sty;
		normal[(cline * 4) + 2] = enx;
		normal[(cline * 4) + 3] = eny;

		mWorld.createLine(new Vec2(stx, sty), (float) Math.sqrt((enx - stx) * (enx - stx) + (eny - sty) * (eny - sty)), (float) Math.atan((eny - sty) / (enx - stx)));
		cline++;

	}

}