package com.af.activity;

import com.android.club.R;
import com.cf.base.variable.ConstantVariable;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ManagementAboutActivity extends Activity {
	
	
	public static String about = ConstantVariable.SYSBOL_DOUBLE_QUOTES;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_management_about);
		
		
		TextView tv_1 = (TextView) findViewById(R.id.title);
		tv_1.setText(about);
	
	}
	
	
}