package com.af.activity;

import java.util.ArrayList;
import java.util.List;

import com.android.club.R;
import com.cf.base.variable.ConstantVariable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class FormationActivity extends Activity {
	
	public final static int CALL_ON_ITEM_SELECTED_TYPE_SPINNER_ACTIVED = 2;
	public final static int CALL_ON_ITEM_SELECTED_TYPE_SET_SELECTION = 1;
	public final static int CALL_ON_ITEM_SELECTED_TYPE_SET_SELECTION_AFTER = 0;
	public final static int DEFAULT_SELECTION = 0;
	
	public static Spinner spinner; 
	public static ArrayAdapter<String> adapter;  
	
	/*
	 * 1.init
	 * 2.setSelection
	 */
	public static int callOnItemSelectedType = CALL_ON_ITEM_SELECTED_TYPE_SET_SELECTION_AFTER;
	public static int lastSelection = DEFAULT_SELECTION;
	
	public static OnItemSelectedListener onItemSelectedListener;
	public static Intent formationActivity;
	public static Intent startActivity;

	@Override
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_formation);
        
        Window window = getWindow();  
        WindowManager.LayoutParams wl = window.getAttributes();  
        wl.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;  
        wl.alpha=0.6f;//设置透明度,0.0为完全透明，1.0为完全不透明  
        window.setAttributes(wl);
        
        // Formation
        List<String> list = new ArrayList<String>();     
        list.add(ConstantVariable.formation331);     
        list.add(ConstantVariable.formation322);     
        list.add(ConstantVariable.formation232);     
        list.add(ConstantVariable.formation241);     
        list.add(ConstantVariable.formation421);  
        
        spinner = (Spinner)findViewById(R.id.spinner_City);    
        
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。   
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);   
        //第三步：为适配器设置下拉列表下拉时的菜单样式。   
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);   
        //第四步：将适配器添加到下拉列表上   
        spinner.setAdapter(adapter);   
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中   
        
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
        	
            @SuppressWarnings("unchecked")
			public void onItemSelected(AdapterView arg0, View arg1, int currentSelection, long arg3) {
            
            	switch(callOnItemSelectedType){
	            	case CALL_ON_ITEM_SELECTED_TYPE_SPINNER_ACTIVED:{          		
	            		if(lastSelection!=currentSelection){
	            			callOnItemSelectedType = CALL_ON_ITEM_SELECTED_TYPE_SET_SELECTION;
	            			spinner.setSelection(lastSelection, false);
	            		}else{
	            			callOnItemSelectedType = CALL_ON_ITEM_SELECTED_TYPE_SET_SELECTION_AFTER;
	            		}
	            		break;
	        		}
	            	case CALL_ON_ITEM_SELECTED_TYPE_SET_SELECTION:{
	            		callOnItemSelectedType = CALL_ON_ITEM_SELECTED_TYPE_SET_SELECTION_AFTER;
	            		break;
	            	}
	            	case CALL_ON_ITEM_SELECTED_TYPE_SET_SELECTION_AFTER:{
	            		lastSelection = currentSelection;
	            		
	            		switch(lastSelection){
		            		case 0:{
		            			TacticalActivity.formation = ConstantVariable.formation331;
		            			break;
		            		}
		            		case 1:{
		            			TacticalActivity.formation = ConstantVariable.formation322;
		            			break;
		            		}
		            		case 2:{
		            			TacticalActivity.formation = ConstantVariable.formation232;
		            			break;
		            		}
		            		case 3:{
		            			TacticalActivity.formation = ConstantVariable.formation241;
		            			break;
		            		}
		            		case 4:{
		            			TacticalActivity.formation = ConstantVariable.formation421;
		            			break;
		            		}
	            		}
	            		startActivity.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); 
	                	startActivity(startActivity); 
	                	break;
	            	}
            	}
            }   
            @SuppressWarnings("unchecked")
			public void onNothingSelected(AdapterView arg0) {   
                // TODO Auto-generated method stub   
                //myTextView.setText("NONE");   
                arg0.setVisibility(View.VISIBLE);   
            }   
        }); 
        
        
        /*下拉菜单弹出的内容选项触屏事件处理*/  
        spinner.setOnTouchListener(new Spinner.OnTouchListener(){   
            public boolean onTouch(View v, MotionEvent event) {   
                // TODO Auto-generated method stub   
                /* 将mySpinner 隐藏，不隐藏也可以，看自己爱好*/  
                v.setVisibility(View.INVISIBLE);   
                return false;   
            }  
        });   
        /*下拉菜单弹出的内容选项焦点改变事件处理*/  
        spinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener(){   
        public void onFocusChange(View v, boolean hasFocus) {   
        	// TODO Auto-generated method stub   
            v.setVisibility(View.VISIBLE);   
        }   
        });    
	}
}
