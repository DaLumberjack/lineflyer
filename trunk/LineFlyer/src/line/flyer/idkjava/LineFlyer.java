package line.flyer.idkjava;

import android.app.Activity;
import android.os.Bundle;

public class LineFlyer extends Activity
{
	//Main view object
	//private static MainView main_view;
	public static MenuBar menu_bar;
	
	//Variable used to keep track of what mode we're in
	public static int mode;
	
	//Path variable that keeps track of all the lines created
	//public static Path path;
	//PathMeasure variable that is used to read the path
	//public static PathMeasure pathmeasure;
	
	//Lines array
	public static int[] lines;
	
	public static boolean paused = true;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.ui);
        
        //main_view = new MainView(this,null);
        menu_bar = new MenuBar(this, null);
        
        menu_bar = (MenuBar) findViewById(R.id.menu_bar);
        //main_view = (MainView) findViewById(R.id.main_view);
        
        //set up the drawable for the line rider
        MainView.pNormImg = getResources().getDrawable(R.drawable.linerider175);
        
        MainView.setup();
    }
    @Override
    protected void onResume()
    {
    	super.onResume();
    	MainView.setup();
    }
}