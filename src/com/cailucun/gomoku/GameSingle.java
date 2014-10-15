package com.cailucun.gomoku;




import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import com.cailucun.gomoku.R;

public class GameSingle extends Activity{
	private static final String TAG = "Gomoku";
	private Single single;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		
		Display display = getWindowManager().getDefaultDisplay();
		Single.init(this, display.getWidth(), display.getHeight());
		single = Single.getInstance();
		String picture,music,order;
		picture=Prefs.getPicture(getApplicationContext());
		music=Prefs.getMusic(getApplicationContext());
		order=Prefs.getOrder(getApplicationContext());
		single.set(picture,order);
		if(music.equalsIgnoreCase("Music2"))
			single.play(this,R.raw.qhc);
		else if(music.equalsIgnoreCase("Music3"))
			single.play(this,R.raw.dzsh);
		setContentView(single);
		
		single.requestFocus();
	}
	@Override
	protected void onDestroy(){
		single.stop(this);
		super.onDestroy();
		Log.d(TAG, "Single Activity:onDestroy");

	}

}