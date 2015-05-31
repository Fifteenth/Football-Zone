package com.af.db;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DB {

	
	public void dbInit(Activity activity){
		SQLiteDatabase db = activity.openOrCreateDatabase("test.db", Context.MODE_PRIVATE, null);  
	}

}
