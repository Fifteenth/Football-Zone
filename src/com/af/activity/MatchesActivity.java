package com.af.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.af.adapter.MatchesAdapter;
import com.af.dialog.MatchDialog;
import com.android.club.R;
import com.cf.base.variable.ConstantVariable;
import com.cf.support.MatchesSupport;
import com.cf.to.MatchTO;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class MatchesActivity extends Activity{
	

	private List <MatchTO> listMatchTO = new ArrayList<MatchTO>();
	
	public List<MatchTO> getListMatchTO() {
		return listMatchTO;
	}

	private static int listIndex = -1;
	private ListView listViewMatch;
	private int layoutInt = R.layout.listview_matches;
	private MatchDialog matchDialog;
	private MatchesAdapter adapter;
	
	public static int dialogType = 0;
	
	public static final int SHOW_DATAPICK = 0; 
	public static final int DATE_DIALOG_ID = 1;  
	public static final int SHOW_TIMEPICK = 2;
	public static final int TIME_DIALOG_ID = 3;
    
    private int mYear;  
    private int mMonth;
    private int mDay; 
    private int mHour;
    private int mMinute;

    
    private float listviewTouchStartX = -1;
    private float listviewTouchStartY = -1;
    private boolean isTouchMove = false;
    private boolean isTouchMoveFlag = false;
    private boolean isAvoidResponseOnTouch = false;
    
    
    public void setAvoidResponseOnTouch(boolean isAvoidResponseOnTouch) {
		this.isAvoidResponseOnTouch = isAvoidResponseOnTouch;
	}

	private View currentItemView;
    
    private RelativeLayout layoutRight;
    private RelativeLayout lastLayoutRight;
    
    
    
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_matches);
		
		matchDialog = new MatchDialog(this);
		matchDialog.setMatchDialog(matchDialog);

		listViewMatch = (ListView) this.findViewById(R.id.listView_my);
		listMatchTO = MatchesSupport.ReadMatches();
		
		if(listMatchTO == null){
			listMatchTO = new ArrayList();
		}
		// 实例化自定义适配器
		adapter = new MatchesAdapter(this,listMatchTO,layoutInt);
		if(listMatchTO.size() > 0){
			listViewMatch.setAdapter(adapter);
		}
		
		listViewMatch.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				if(isTouchMoveFlag == true){
					// Set Init State
					isTouchMoveFlag = false;
					// 
					Button button = adapter.getButton();
					if(button != null){
						adapter.getButton().setBackgroundResource(R.drawable.button_delete);
					}
					return;
				}
				
				/*
				 * API
				 * 
				 * Toast.makeText(TeamActivity.this, "选中的是:" + position,
				 *		Toast.LENGTH_SHORT).show();
				 */
				
				listViewMatch.setItemChecked(position, true);

				// Activity
				MatchesMatchDetailActivity matchesMatchDetailActivity = new MatchesMatchDetailActivity();
				// Selection MatchTO
				matchesMatchDetailActivity.matchTO  = listMatchTO.get(position);
				
				Intent matchDetailActivity = new Intent(MatchesActivity.this,MatchesMatchDetailActivity.class);
				startActivity(matchDetailActivity);
			}
		});
		
		
		listViewMatch.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int listIndex, long arg3) {
				
				// Wish To Flicker
				listViewMatch.setSelector(android.R.color.transparent);
				listViewMatch.setSelection(-1);
				
//				Drawable drawable =  listViewMatch.getSelector();
//				listViewMatch.setSelector(drawable);
				
				// Intercept
//				if(isAvoidResponseOnLongClick){
//					// Set Init State
//					isAvoidResponseOnLongClick = false;
//					return true;
//				}

				// Intercept
//				if(isAvoidResponseOnLongClick){
//					// Set Init State
//					isAvoidResponseOnLongClick = false;
//					return true;
//				}
				
				// Set Init State
				isTouchMoveFlag = false;
				
				//// 
				MatchesActivity.listIndex = listIndex;
				matchDialog.show();
				MatchTO matchTO = listMatchTO.get(listIndex);
				// Dialog Set
				matchDialog.setEditTextRound(matchTO.getRound().trim());
				matchDialog.setEditTextScore(matchTO.getScore());
				matchDialog.setEditTextCompetitor(matchTO.getCompetitor());
				matchDialog.setEditTextDate(matchTO.getCompetitionDate());
				matchDialog.setEditTextTime(matchTO.getCompetitionTime());
				// Dialog Type
				dialogType = ConstantVariable.DIALOG_UPDATE;
				return true;
			}
		});
		
		
		listViewMatch.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				boolean returnFlag = false;
				float currentViewX = event.getX();
				float currentViewY = event.getY();
				
				
				// Default Selector
				if(!isTouchMove){
					listViewMatch.setSelector(R.color.blue);
				}
				
				if(listviewTouchStartX == -1){
					// Init
					listviewTouchStartX = currentViewX;
					listviewTouchStartY = currentViewY;
				}
				
//				System.out.println("listviewTouchStartX:" + listviewTouchStartX
//						+ "----" + "currentViewX:" + currentViewX);
				
				if(listviewTouchStartX - currentViewX > 80
						&& listviewTouchStartY - currentViewY < 40){
					showRight(currentItemView);
				}
				if(currentViewX - listviewTouchStartX > 80
						&& listviewTouchStartY - currentViewY < 40){
					hiddenRight(currentItemView);
				}
				

				if(event.getAction() == MotionEvent.ACTION_DOWN){
					System.out.println("++++++++++++++++++++++ACTION_DOWN");

					/*
					 * API Comment
							 	该View会先响应ACTION_DOWN事件，并返回一个boolean值，这里有两种判断：
					            a：返回True，表示该View接受此按下动作，就是说这个点击动作的按下操作被中止，
					            然后就是响应ACTION_UP事件。点击动作的按下操作被ACTION_DOWN接受之后就结束了，所以
					            之后的OnClick/OnLongClick事件就不会响应了。
					            b：返回false，表示该View不接受此按下动作，响应完之后，按下操作继续往下发，
					            之后是响应ACTION_UP事件，这里又有一个判断：
					 */
					// Look API Comment
					isTouchMoveFlag = false;
					
					// Set Init State
					returnFlag = false;
					
					// Intercept OnTouch
					if(isAvoidResponseOnTouch){
						isAvoidResponseOnTouch = false;
						returnFlag = true;
					}
					
					

					float lastX = event.getX();
					float lastY = event.getY();
					int motionPosition = listViewMatch.pointToPosition((int) lastX, (int) lastY);
					
					if (motionPosition >= 0) {
						currentItemView = listViewMatch.getChildAt(motionPosition - 
								listViewMatch.getFirstVisiblePosition());
						
						// Last Layout 
						lastLayoutRight = layoutRight;
						// Current Layout
						layoutRight = (RelativeLayout)currentItemView.findViewById(R.id.item_right);
					}
				}
				
				if(event.getAction() == MotionEvent.ACTION_UP){
					
					
					/*
					 * API Comment
						 	如果ACTION_UP事件返回True，表示ACTION_UP接受松开操作，松开操作中止；
						 View会一直处于按下状态，之后View便会响应OnLongClick事件。
	 						如果ACTION_UP事件返回false，表示ACTION_UP不接收松开操作，松开操作继续下发；
	 					因为按下与松开操作都没有被中止，所以之后View就会响应OnClick事件。
					 */
					
					System.out.println("++++++++++++++++++++++ACTION_UP");
					
					// Reflesh
//					if(!isTouchMove){
//						listViewMatch.setSelector(android.R.color.transparent);
//					}
					
					// Set Init State
					listviewTouchStartX = -1;
					listviewTouchStartY = -1;
					
					// Return 
					if(isTouchMove){
						// Set Init State
						isTouchMove = false;
						isTouchMoveFlag = true;
						
						
						/* 
						 * 1.
						 * return false
						 * Will not Response OnLongClick
						 * Will Response OnClick
						 */
//						returnFlag = false;
						
						
						
						/*
						 * 2.
						 * return true
						 * Will Response OnLongClick
						 * Will not Response OnClick
						 */
						/*
						isAvoidResponseOnLongClick = true;
						returnFlag = true;
						*/
						
						
					}else{
						// Will Response OnLongClick
//						isAvoidResponseOnLongClick = true;
//						returnFlag = true;
					}
					
					returnFlag = false;
				}
				
				
				if(event.getAction() == MotionEvent.ACTION_MOVE){
					
					System.out.println("++++++++++++++++++++++ACTION_MOVE");
					
					isTouchMove = true;
					
					// Selector
					listViewMatch.setSelector(android.R.color.transparent);
				}
				return returnFlag;
			}
		});
		
		// Add Match
		Button buttonMatchAdd = (Button) findViewById(R.id.button_matchAdd);
		buttonMatchAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				matchDialog.show();
				matchDialog.setEditTextRound("");
				matchDialog.setEditTextScore("");
				matchDialog.setEditTextCompetitor("");
				dialogType = ConstantVariable.DIALOG_ADD;
			}
		});
		
		
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);  
        mMonth = c.get(Calendar.MONTH);  
        mDay = c.get(Calendar.DAY_OF_MONTH);
        
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        
        setDateTime(); 
        setTimeOfDay();
		
	}
	
	// Dialog Back
	public void back(MatchTO matchTO) {
		switch (dialogType) {
			case ConstantVariable.DIALOG_ADD: {
				listMatchTO.add(matchTO);
				break;
			}
			case ConstantVariable.DIALOG_UPDATE: {
				listMatchTO.set(listIndex, matchTO);
				break;
			}
			case ConstantVariable.DIALOG_DELETE: {
				listMatchTO.remove(matchTO);
				break;
			}
		}
		// Set Default
		dialogType = ConstantVariable.DIALOG_DEFAULT;
		// Write
		MatchesSupport.WriteMatches(listMatchTO);
		
		// Reflesh Data
		adapter.notifyDataSetChanged();
	}
	
	
    /**
     * 设置日期
     */
	private void setDateTime(){
       final Calendar c = Calendar.getInstance();  
       
       mYear = c.get(Calendar.YEAR);  
       mMonth = c.get(Calendar.MONTH);  
       mDay = c.get(Calendar.DAY_OF_MONTH); 
  
       updateDateDisplay(); 
	}
	
	/**
	 * 更新日期显示
	 */
	private void updateDateDisplay(){
		EditText editTextDate = matchDialog.getEditTextDate();
		if(editTextDate != null){
			editTextDate.setText(new StringBuilder().append(mYear).append("-")
					.append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")
					.append((mDay < 10) ? "0" + mDay : mDay)); 
		}
       
	}
	
    /** 
     * 日期控件的事件 
     */  
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {  
  
       
    	@Override
    	public void onDateSet(DatePicker view, int year, int monthOfYear,  
              int dayOfMonth) {  
    		
			mYear = year;  
			mMonth = monthOfYear;  
			mDay = dayOfMonth;  
			updateDateDisplay();
    	}  
    }; 
	
	/**
	 * 设置时间
	 */
	private void setTimeOfDay(){
	   final Calendar c = Calendar.getInstance(); 
       mHour = c.get(Calendar.HOUR_OF_DAY);
       mMinute = c.get(Calendar.MINUTE);
       updateTimeDisplay();
	}
	
	/**
	 * 更新时间显示
	 */
	private void updateTimeDisplay(){
		EditText editTextTime = matchDialog.getEditTextTime();
		if(editTextTime != null){
			editTextTime.setText(new StringBuilder().append(mHour).append(":")
		               .append((mMinute < 10) ? "0" + mMinute : mMinute)); 
		}
	}
    
    /**
     * 时间控件事件
     */
    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateTimeDisplay();
		}
	};
    
    @Override  
    protected Dialog onCreateDialog(int id) {  
       switch (id) {  
       case DATE_DIALOG_ID:  
           return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,mDay);
       case TIME_DIALOG_ID:
    	   return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, true);
       }
       return null;  
    }  
  
    @Override  
    protected void onPrepareDialog(int id, Dialog dialog) {  
       switch (id) {  
       case DATE_DIALOG_ID:  
           ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);  
           break;
       case TIME_DIALOG_ID:
    	   ((TimePickerDialog) dialog).updateTime(mHour, mMinute);
    	   break;
       }
    } 
	
	
	/** 
     * 处理日期和时间控件的Handler 
     */  
    public Handler dateandtimeHandler = new Handler() {
  
       @Override  
       public void handleMessage(Message msg) {  
           switch (msg.what) {  
           case SHOW_DATAPICK:  
               showDialog(DATE_DIALOG_ID);
               break; 
           case SHOW_TIMEPICK:
        	   showDialog(TIME_DIALOG_ID);
        	   break;
           }  
       }  
    }; 
    
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	super.onBackPressed();
    }
    
    
    
    
    public void showRight(View rightView) {
    	if(lastLayoutRight!=null){
    		lastLayoutRight.setVisibility(View.GONE);	
    	}
    	layoutRight.setVisibility(View.VISIBLE);
	}
	
	public void hiddenRight(View rightView) {
		if(rightView == null){
			rightView = currentItemView;
		}
		layoutRight.setVisibility(View.GONE);
	}
	
}
