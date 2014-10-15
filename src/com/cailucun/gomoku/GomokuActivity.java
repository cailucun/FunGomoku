package com.cailucun.gomoku;



import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import com.cailucun.gomoku.R;


public class GomokuActivity extends Activity implements OnClickListener {
	//private static final String TAG = "Sudoku";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Set up click listeners for all the buttons
		View button_single = findViewById(R.id.button_single);
		button_single.setOnClickListener(this);
		View button_two = findViewById(R.id.button_two);
		button_two.setOnClickListener(this);
		View button_risk = findViewById(R.id.button_risk);
		button_risk.setOnClickListener(this);
		View button_setting = findViewById(R.id.button_setting);
		button_setting.setOnClickListener(this);
		View button_exit = findViewById(R.id.button_exit);
		button_exit.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_two:
			Intent i = new Intent(this, GameTwo.class);
			startActivity(i);
			break;
		case R.id.button_single:
			Intent j = new Intent(this, GameSingle.class);
			startActivity(j);
			break;	
		case R.id.button_risk:
			Intent k = new Intent(this, GameRisk.class);
			startActivity(k);
			break;	
		case R.id.button_setting:
			Intent h = new Intent(this, Prefs.class);
			startActivity(h);
			break;
		case R.id.button_exit:
			finish();
			break;
		}
	}
	}
		