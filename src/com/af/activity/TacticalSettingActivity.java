package com.af.activity;

import java.util.List;

import com.android.club.R;
import com.cf.base.variable.FileVariable;
import com.cf.fix.GlobleTacticalClass;
import com.cf.service.RWTOService;
import com.cf.to.TacticalSettingTO;
import com.ff.switchButton.SlideSwitchView;
import com.ff.switchButton.SlideSwitchView.OnSwitchChangedListener;

import android.app.Activity;
import android.os.Bundle;

public class TacticalSettingActivity extends Activity {
	
	private SlideSwitchView mSlideSwitchView;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        	
        setContentView(R.layout.activity_setting_tactical);
        
        mSlideSwitchView = (SlideSwitchView) findViewById(R.id.mSlideSwitchView);
        
        if(GlobleTacticalClass.isDisplayCompetitor().equals("true")){
        	mSlideSwitchView.setChecked(true);
        }else{
        	mSlideSwitchView.setChecked(false);
        }
        mSlideSwitchView.setOnChangeListener(new OnSwitchChangedListener() {
			
			@Override
			public void onSwitchChange(SlideSwitchView switchView, boolean isChecked) {
				// TODO Auto-generated method stub
				List <TacticalSettingTO>list = GlobleTacticalClass.gotTacticalSetting();
				list.get(0).setDisplayCompetitor(String.valueOf(isChecked));
				try {
					RWTOService.writeXMLFromListTOAndSave(list, TacticalSettingTO.classPath, 
							FileVariable.FILE_NAME_TACTICAL_SETTING);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
  	
}