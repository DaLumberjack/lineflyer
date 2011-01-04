package line.flyer.idkjava;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MenuBar extends LinearLayout
{
	//Used when exit is called because we need the specific instance of the activity to end
	private LineFlyer activity;
	
	private Context context;
	
	static ImageButton grab;
	static ImageButton play_pause_button;
	private ImageButton save_button;
	private ImageButton load_button;
	private ImageButton linebutton;
	private ImageButton exit_button;
	
	//Used for eraser
	static boolean eraser_on = false;
	private int temp_element = 0;
	
	//Used for play/pause
	static boolean play = true;

	//Constructor
	public MenuBar(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
	}
	
	//Used to get specific instance of activity
	public void setActivity(LineFlyer act)
	{
		activity = act;
	}
	


	//Called when it's finished inflating the XML layout
	@Override
	protected void onFinishInflate()
	{
		//Set up all the variables for the objects
		grab = (ImageButton) findViewById(R.id.grab);
		play_pause_button = (ImageButton) findViewById(R.id.play_pause_button);
		save_button = (ImageButton) findViewById(R.id.save_button);
		load_button = (ImageButton) findViewById(R.id.load_button);
		linebutton = (ImageButton) findViewById(R.id.linebutton);
		exit_button = (ImageButton) findViewById(R.id.exit_button);
		
		grab.setOnClickListener
		(
			new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
			    	LineFlyer.mode = 2;
				}
			}
		);
		
		//Set up the OnClickListener for the play/pause button
		play_pause_button.setOnClickListener
		(
			new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					LineFlyer.paused = !LineFlyer.paused;
					if (LineFlyer.paused == true){
						play_pause_button.setImageResource(R.drawable.play);
					}
					else{
						play_pause_button.setImageResource(R.drawable.pause);
					}
				}
				
			}
		);
		play_pause_button.setImageResource(R.drawable.play);
		
		//Set up the OnClickListener for the save button
		save_button.setOnClickListener
		(
			new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
			    	 
				}
			}
		);
		
		//Set up the OnClickListener for the load button
		load_button.setOnClickListener
		(
			new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
			    	
				}
			}
		);
		
		//Set up the OnClickListener for the load demo button
		linebutton.setOnClickListener
		(
			new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
			    	LineFlyer.mode = 1;
				}
			}
		);
		
		//Set up the OnClickListener for the exit button
		exit_button.setOnClickListener
		(
			new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					System.exit(0);
				}
			}
		);
	}
}