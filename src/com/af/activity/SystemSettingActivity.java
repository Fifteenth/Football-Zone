package com.af.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.af.adapter.SystemAccessAdapter;
import com.android.club.R;
import com.ff.widgets.WaitingPointBar;
import com.http.HttpCilent;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SystemSettingActivity extends Activity {
	
	private WaitingPointBar waitingPointBar;
	private String httpResult;
	private StateHandler stateHandler;
	private SystemSettingActivity activity;
	
	private ListView listViewSystemAccess;
	private List<JSONObject> listSystemAccess;
	private SystemAccessAdapter adapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        	
        setContentView(R.layout.activity_system_setting);
        activity = this;
        // 
//        SlideSwitchView mSlideSwitchView = (SlideSwitchView) findViewById(R.id.mSlideSwitchView);
        waitingPointBar = (WaitingPointBar) findViewById(R.id.waitingPointBar);
        listViewSystemAccess = (ListView) this.findViewById(R.id.listView_system_access);
        
        Button button_update = (Button) findViewById(R.id.system_setting_button_update);
        button_update.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			// 
    			if(stateHandler == null){
    				stateHandler = new StateHandler();
    			}
    			stateHandler.sendEmptyMessage(View.VISIBLE);
    		}
    	});
        
//        if(GlobleTacticalClass.isDisplayCompetitor().equals("true")){
//        	mSlideSwitchView.setChecked(true);
//        }else{
//        	mSlideSwitchView.setChecked(false);
//        }
//        
//        mSlideSwitchView.setOnChangeListener(new OnSwitchChangedListener() {
//			
//			@Override
//			public void onSwitchChange(SlideSwitchView switchView, boolean isChecked) {
//				// TODO Auto-generated method stub
//				List <TacticalSettingTO>list = GlobleTacticalClass.gotTacticalSetting();
//				list.get(0).setDisplayCompetitor(String.valueOf(isChecked));
//				try {
//					RWTOService.writeXMLFromListTOAndSave(list, TacticalSettingTO.classPath, 
//							FileVariable.FILE_NAME_TACTICAL_SETTING);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
	}
	
	
	class HttpHandler extends Handler{
		
		@Override
        public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			// 新线程访问HTTP
			new Thread(){
				
				 @Override
				 public void run(){
					 //你要执行的方法
					 HttpCilent httpCilent = new HttpCilent();
					 httpCilent.setUrl("http://5522s.cn/system/getLast10SystemAccess");
					 httpResult = httpCilent.HttpGet();
					 int statue = 0;
					 //
					 if(httpResult.equals(WaitingPointBar.MESSAGE_TEXT_ERROR_UNKNOWN_HOST_EXCEPTION)){
						 statue = WaitingPointBar.MESSAGE_ERROR_UNKNOWN_HOST_EXCEPTION_NUMBER;
					 }else if(httpResult.equals(WaitingPointBar.MESSAGE_TEXT_ERROR_THE_SERVER_CAN_NOT_ACCESS)){
						 statue = WaitingPointBar.MESSAGE_ERROR_THE_SERVER_CAN_NOT_ACCESS_NUMBER;
					 }else{
						 // 处理业务
						 listSystemAccess = new ArrayList<JSONObject>();
						 statue = View.INVISIBLE;
						 try {
							JSONArray jsonArray = new JSONArray(httpResult);  
							for(int i=0;i<jsonArray.length();i++){
								listSystemAccess.add((JSONObject) jsonArray.get(i));
							}
							
						} catch (JSONException e) {
							e.printStackTrace();
						}
					 }
					 // 
					 stateHandler.sendEmptyMessage(statue);
				 }
			}.start();
		}
	}
	
	

	class StateHandler extends Handler{
		int i = 0;
		
		@Override
        public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			LinearLayout linearLayoutBar = (LinearLayout)waitingPointBar.getChildAt(0);
			TextView textViewMessage = (TextView)waitingPointBar.getChildAt(1);
			
			int statue = msg.what;
			
			if(statue == View.VISIBLE){
				waitingPointBar.setVisibility(statue);
				linearLayoutBar.setVisibility(statue);
				textViewMessage.setText(WaitingPointBar.MESSAGE_TEXT_WAITING);
				new HttpHandler().sendEmptyMessage(View.VISIBLE);
			}else if(statue == View.INVISIBLE){
				linearLayoutBar.setVisibility(statue);
				textViewMessage.setText("更新成功...");
				
				adapter = new SystemAccessAdapter(
						activity,listSystemAccess);
				listViewSystemAccess.setAdapter(adapter);
			}else if(statue == View.GONE){
				waitingPointBar.setVisibility(statue);
			}else if(statue == WaitingPointBar.MESSAGE_ERROR_UNKNOWN_HOST_EXCEPTION_NUMBER
					||statue == WaitingPointBar.MESSAGE_ERROR_THE_SERVER_CAN_NOT_ACCESS_NUMBER){
				linearLayoutBar.setVisibility(View.INVISIBLE);
				textViewMessage.setText("网络出现异常...");
			}
		}
	}
  	
}