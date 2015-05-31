package com.af.activity;

import java.util.HashMap;

import com.android.club.R;
import com.cf.base.variable.ConstantVariable;
import com.ff.gallery.GalleryView;
import com.ff.gallery.ImageAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class NavigationActivity extends Activity {

	private TextView tvTitle; 	
	private GalleryView gallery; 	
	private ImageAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_navigation);

		initRes();
		initSounds();
	}
	
	private void initRes(){
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		gallery = (GalleryView) findViewById(R.id.mygallery);

		adapter = new ImageAdapter(this); 	
		adapter.createReflectedImages();	// 创建倒影效果
		gallery.setAdapter(adapter);
		
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {	// 设置选择事件监听
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				tvTitle.setText(ConstantVariable.titles[position]);
				
				playSounds(1,0);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		gallery.setOnItemClickListener(new OnItemClickListener() {			// 设置点击事件监听
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Show Message
//				Toast.makeText(NavigationActivit.this, "img " + (position+1) 
//						+ " selected", Toast.LENGTH_SHORT).show();
			
				switch(position){
				case 0:
					Intent teamIntroductionActivity = new Intent(NavigationActivity.this, IntroductionActivity.class);
					startActivity(teamIntroductionActivity);
					break;
				case 1:
					Intent matchsActivity = new Intent(NavigationActivity.this, MatchesActivity.class);
					startActivity(matchsActivity);
					break;
				case 2:
					Intent team = new Intent(NavigationActivity.this, TeamActivity.class);
					startActivity(team);
					break;
				case 3:
					Intent startActivity = new Intent(NavigationActivity.this, TacticalActivity.class);
					FormationActivity.startActivity = startActivity;
					startActivity(startActivity);
					break;
				case 4:
					new  AlertDialog.Builder(NavigationActivity.this)    
				    .setTitle("提示：" )
				    .setMessage("转会市场已关闭!" )
				    .setPositiveButton("确定" ,null).show(); 
					break;
					
//					Intent timePickerDemo = new Intent(NavigationActivity.this,TimeActivity.class);
//					startActivity(timePickerDemo);
//					break;
					
				case 5:
					Intent financeActivity = new Intent(NavigationActivity.this,FinanceActivity.class);
					startActivity(financeActivity);
					break;
					
				case 6:
					Intent managementActivity = new Intent(NavigationActivity.this,ManagementActivity.class);
					startActivity(managementActivity);
					break;
					
				case 7:
					Intent systemSettingActivity = new Intent(NavigationActivity.this,SystemSettingActivity.class);
					startActivity(systemSettingActivity);
					break;
					
				default:
					new  AlertDialog.Builder(NavigationActivity.this)    
				    .setTitle("提示：" )
				    .setMessage("此功能敬请期待!" )
				    .setPositiveButton("确定" ,null).show(); 
					break;
				}
			}
		});
	}
	
	
	private SoundPool sp;
	private HashMap<Integer,Integer> spMap;
	
	
	public void playSounds(int sound, int number){
        AudioManager am = (AudioManager)this.getSystemService(this.AUDIO_SERVICE);
        float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float audioCurrentVolumn = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float volumnRatio = audioCurrentVolumn/audioMaxVolumn;
        
        sp.play(spMap.get(sound), volumnRatio, volumnRatio, 1, number, 1);
    }


	public void initSounds(){
		sp = new SoundPool(1,AudioManager.STREAM_MUSIC,0);
		
		spMap = new HashMap<Integer,Integer>();
        spMap.put(1,sp.load(this, R.raw.slide_ringtones,1));
	}
	
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int currentVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);

		switch (keyCode) {
			case KeyEvent.KEYCODE_VOLUME_UP:// 音量增大
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
						currentVolume + 1, 1);
				break;
			case KeyEvent.KEYCODE_VOLUME_DOWN:// 音量减小
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
						currentVolume - 1, 1);
				break;
	
			case KeyEvent.KEYCODE_BACK:// 返回键
				onBackPressed();
			default:
				break;
		}

		return true;
		// return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	
}