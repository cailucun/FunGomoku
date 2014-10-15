package com.cailucun.gomoku;




import android.app.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import com.cailucun.gomoku.R;


public class GameTwo extends Activity{
	private static final String TAG = "Gomoku";
	private Two two;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		
		Display display = getWindowManager().getDefaultDisplay();
		Two.init(this, display.getWidth(), display.getHeight());
		two = Two.getInstance();
		String picture,music;
		picture=Prefs.getPicture(getApplicationContext());
		music=Prefs.getMusic(getApplicationContext());
		two.set(picture);
		
		if(music.equalsIgnoreCase("Music2"))
			two.play(this,R.raw.qhc);
		else if(music.equalsIgnoreCase("Music3"))
			two.play(this,R.raw.dzsh);
			
		setContentView(two);
		two.requestFocus();
	}
	@Override
	protected void onDestroy(){
		two.stop(this);
		super.onDestroy();	
		Log.d(TAG, "Two Activity:onDestroy");

	}

}
