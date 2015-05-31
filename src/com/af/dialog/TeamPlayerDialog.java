package com.af.dialog;



import java.util.List;

import com.af.activity.TeamActivity;
import com.android.club.R;
import com.cf.service.BuildTOService;
import com.cf.support.TeamSupport;
import com.cf.to.PlayerTO;
import com.cf.to.TeamTO;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
 

public class TeamPlayerDialog extends Dialog {
	
	// Class Path
	final public static String classPath = TeamPlayerDialog.class.getName();
	
	private Boolean needSaveCaption = false;

	////Controls ////
	// EditText
	private EditText editTextNumber;
	private EditText editTextName;
	private EditText editTextPosition;
	// CheckBox
	private CheckBox checkBoxIsCaptain;
	
	//// Property ////
	private TeamPlayerDialog teamDialog;
	private TeamActivity teamActivity;
	
	
	public EditText getEditTextNumber() {
		return editTextNumber;
	}

	public EditText getEditTextName() {
		return editTextName;
	}

	public EditText getEditTextPosition() {
		return editTextPosition;
	}
	
	public CheckBox getCheckBoxIsCaptain() {
		return checkBoxIsCaptain;
	}

	public void setEditTextNumber(String number) {
		this.editTextNumber.setText(number);
	}

	public void setEditTextName(String name) {
		this.editTextName.setText(name);
	}

	public void setEditTextPosition(String position) {
		this.editTextPosition.setText(position);
	}
	
	public void setCheckBoxIsCaptain(CheckBox checkBoxIsCaptain) {
		this.checkBoxIsCaptain = checkBoxIsCaptain;
	}
	
	
	
	public TeamPlayerDialog getTeamDialog() {
		return teamDialog;
	}

	public void setTeamDialog(TeamPlayerDialog teamDialog) {
		this.teamDialog = teamDialog;
	}
	
	
	public TeamPlayerDialog(Context context) {
		super(context);
		teamActivity = (TeamActivity) context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_team_player);
		
		editTextNumber = (EditText)findViewById(R.id.edit_number);
		editTextPosition = (EditText)findViewById(R.id.edit_position);
		editTextName = (EditText)findViewById(R.id.edit_name);
		
		checkBoxIsCaptain = (CheckBox)findViewById(R.id.checkbox_is_caption);
		checkBoxIsCaptain.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				
				List<PlayerTO> listPlayerTO = teamActivity.getListPlayerTO();
				
				if(teamActivity.getTeamTO() == null
						|| listPlayerTO.get(teamActivity.getListIndex()).getNumber().equals(
						teamActivity.getTeamTO().getCaptionPlayerNumber())){
					// Save
					needSaveCaption = true;
				}else{
					if(isChecked){
						// Save
						needSaveCaption = true;
					}else{
						needSaveCaption = false;
					}
				}
			}
		});
		
		Button buttonConfirm = (Button) findViewById(R.id.button_confirm);
		buttonConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				PlayerTO playerTO = (PlayerTO)BuildTOService.buildTO(
						TeamPlayerDialog.classPath,PlayerTO.classPath,teamDialog);
				
				teamActivity.back(playerTO);
				teamActivity.onCreate(null);
				
				// Save Caption Setting
				if(needSaveCaption){
					TeamTO teamTO = new TeamTO();
					if(checkBoxIsCaptain.isChecked()){
						teamTO.setCaptionPlayerNumber(playerTO.getNumber());
					}else{
						teamTO.setCaptionPlayerNumber("");
					}
					TeamSupport.WriteTeamSetting(teamTO);
				}
				
				// Close Dialog
				TeamPlayerDialog.this.dismiss();
			}
		});
	}
	
	
	
	
}
