package com.af.dialog;



import com.af.activity.MatchesActivity;
import com.android.club.R;
import com.cf.base.util.ValidateUtil;
import com.cf.service.BuildTOService;
import com.cf.to.MatchTO;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
 

public class MatchDialog extends Dialog {
	
	// Class Path
	final public static String classPath = MatchDialog.class.getName();

	// EditText
	private EditText editTextRound;
	private EditText editTextScore;
	private EditText editTextCompetitor;
	public EditText editTextDate;  
	public EditText editTextTime; 
	public static Button pickDate;  
	public static Button pickTime; 
	
	
	private MatchDialog matchDialog;
	private MatchesActivity mathchesActivity;
	
	public EditText getEditTextRound() {
		return editTextRound;
	}

	public EditText getEditTextScore() {
		return editTextScore;
	}

	public EditText getEditTextCompetitor() {
		return editTextCompetitor;
	}
	
	public EditText getEditTextDate() {
		return editTextDate;
	}
	
	public EditText getEditTextTime() {
		return editTextTime;
	}
	
	
	public void setEditTextRound(String round) {
		this.editTextRound.setText(round);
	}

	public void setEditTextScore(String score) {
		this.editTextScore.setText(score);
	}

	public void setEditTextCompetitor(String competitor) {
		this.editTextCompetitor.setText(competitor);
	}
	
	public void setEditTextDate(String competitionDate) {
		this.editTextDate.setText(competitionDate);
	}

	public void setEditTextTime(String competitionTime) {
		this.editTextTime.setText(competitionTime);
	}
	
	public MatchDialog getMatchDialog() {
		return matchDialog;
	}

	public void setMatchDialog(MatchDialog matchDialog) {
		this.matchDialog = matchDialog;
	}
	
	public MatchDialog(Context context) {
		super(context);
		mathchesActivity = (MatchesActivity) context;
	}
	
	
	public MatchesActivity getMathchesActivity() {
		return mathchesActivity;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_match);

		editTextRound = (EditText)findViewById(R.id.edit_round);
		editTextScore = (EditText)findViewById(R.id.edit_score);
		editTextCompetitor = (EditText)findViewById(R.id.edit_competitor);
		editTextDate = (EditText) findViewById(R.id.showdate);  
		editTextTime = (EditText) findViewById(R.id.showtime);
        pickDate = (Button) findViewById(R.id.pickdate); 
        pickTime = (Button) findViewById(R.id.picktime);
        
        
        pickDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
	           Message msg = new Message(); 
	           if (pickDate.equals((Button) v)) {  
	              msg.what = MatchesActivity.SHOW_DATAPICK;  
	           }  
	           mathchesActivity.dateandtimeHandler.sendMessage(msg); 
			}
		});
        
        pickTime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
	           Message msg = new Message(); 
	           if (pickTime.equals((Button) v)) {  
	              msg.what = MatchesActivity.SHOW_TIMEPICK;  
	           }  
	           mathchesActivity.dateandtimeHandler.sendMessage(msg); 
			}
		});
        
        
        // Button
        Button buttonConfirmPayment = (Button) findViewById(R.id.button_confirm_payment);
		buttonConfirmPayment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				// Validate1
				if(!ValidateUtil.isNumeric(String.valueOf(matchDialog.getEditTextRound().getText()).trim())){
					new  AlertDialog.Builder(matchDialog.getMathchesActivity())    
				    .setTitle("提示：" )
				    .setMessage("请输入数字!" )
				    .setPositiveButton("确定" ,null).show(); 
					return;
				}
				// Validate2
				if(String.valueOf(matchDialog.getEditTextRound().getText()).trim().length() > 2){
					new  AlertDialog.Builder(matchDialog.getMathchesActivity())    
				    .setTitle("提示：" )
				    .setMessage("不能输入超过2位数字!" )
				    .setPositiveButton("确定" ,null).show(); 
					return;
				}
				
				MatchTO matchTO = (MatchTO)BuildTOService.buildTO(
						MatchDialog.classPath,MatchTO.classPath,matchDialog);
				mathchesActivity.back(matchTO);
				dismiss();
			}
		});
	}
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
}
