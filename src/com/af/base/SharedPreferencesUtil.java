package com.af.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharedPreferencesUtil {

	public Object getData(Context context,String key){
		SharedPreferences sharedPreferences = PreferenceManager.
				getDefaultSharedPreferences(context);
		return sharedPreferences.contains(key);
	}
	
	public void putData(Context context,String key,String value){
		SharedPreferences sharedPreferences = PreferenceManager.
				getDefaultSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.putString(key,value);
		editor.commit();
	}
}
