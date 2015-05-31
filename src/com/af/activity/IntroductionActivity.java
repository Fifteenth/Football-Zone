package com.af.activity;

import com.android.club.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

public class IntroductionActivity extends Activity{
	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.activity_introduction);
	    
	    
	    
	    
	    // 
	    ImageView imageviewAvatar = (ImageView) findViewById(R.id.imageview_introduction_logo);
	    Bitmap bitmap = BitmapFactory.decodeFile("mnt/sdcard/Football-Club/image/test.png");
	    
	    if(bitmap != null){
	    	imageviewAvatar.setImageBitmap(bitmap);
	    }
	    
	}
}



