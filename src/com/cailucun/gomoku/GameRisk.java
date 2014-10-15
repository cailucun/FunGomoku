package com.cailucun.gomoku;




import android.app.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import com.cailucun.gomoku.R;


public class GameRisk extends Activity{
	private static final String TAG = "Gomoku";
	private Risk risk;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		
		Display display = getWindowManager().getDefaultDisplay();
		Risk.init(this, display.getWidth(), display.getHeight());
		risk = Risk.getInstance();
		String picture,music;
		picture=Prefs.getPicture(getApplicationContext());
		music=Prefs.getMusic(getApplicationContext());
		risk.set(picture);
		if(music.equalsIgnoreCase("Music2"))
			risk.play(this,R.raw.qhc);
		else if(music.equalsIgnoreCase("Music3"))
			risk.play(this,R.raw.dzsh);
		setContentView(risk);
		risk.requestFocus();
	}
	@Override
	protected void onDestroy(){
		risk.stop(this);
		super.onDestroy();
		Log.d(TAG, "Risk Activity:onDestroy");

	}
}
