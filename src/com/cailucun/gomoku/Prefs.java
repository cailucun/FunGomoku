package com.cailucun.gomoku;



import android.content.Context;

import android.os.Bundle;

import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import com.cailucun.gomoku.R;


public class Prefs extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.set);

	}
	public static String getPicture (Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getString("piclist", "Wood");
		
	}
	public static String getMusic (Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getString("musiclist", "None");
		
	}
	public static String getOrder (Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getString("orderlist", "Player first");
		
	}
}
