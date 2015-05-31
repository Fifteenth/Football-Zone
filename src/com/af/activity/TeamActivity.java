package com.af.activity;

import java.util.ArrayList;
import java.util.List;

import com.af.adapter.TeamAdapter;
import com.af.dialog.TeamPlayerDialog;
import com.android.club.R;
import com.cf.base.variable.ConstantVariable;
import com.cf.support.TeamSupport;
import com.cf.to.PlayerTO;
import com.cf.to.TeamTO;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

/**
 * 自定义ListAdapter
 * 
 * @author Administrator
 * 
 */
public class TeamActivity extends Activity {

	private TeamTO teamTO;
	private int listIndex = -1;
	
	private List<PlayerTO> listPlayerTO = new ArrayList<PlayerTO>();
	private ListView listViewTeam;
	private int layoutInt = R.layout.listview_team;
	private TeamPlayerDialog teamDialog;
	int dialogType = 0;
	
	
	public List<PlayerTO> getListPlayerTO() {
		return listPlayerTO;
	}

	public void setListPlayerTO(List<PlayerTO> listPlayerTO) {
		this.listPlayerTO = listPlayerTO;
	}
	
	public TeamTO getTeamTO() {
		return teamTO;
	}

	public void setTeamTO(TeamTO teamTO) {
		this.teamTO = teamTO;
	}
	
	public int getListIndex() {
		return listIndex;
	}

	public void setListIndex(int listIndex) {
		this.listIndex = listIndex;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_team);
		
		teamDialog = new TeamPlayerDialog(this);
		teamDialog.setTeamDialog(teamDialog);
		
		Resources resources = this.getResources();
		listViewTeam = (ListView) this.findViewById(R.id.listView_my);
		
		// Player
		listPlayerTO = TeamSupport.ReadTeamPlayer();
		
		// 实例化自定义适配器
		TeamAdapter adapter = new TeamAdapter(this,listPlayerTO,layoutInt,resources);
		if(listPlayerTO.size()>0){
			listViewTeam.setAdapter(adapter);
		}
		listViewTeam.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				listViewTeam.setItemChecked(position, true);
			}
		});
	
		listViewTeam.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int listIndex, long arg3) {
				setListIndex(listIndex);
				teamDialog.show();
				PlayerTO playerTO = listPlayerTO.get(listIndex);
				teamDialog.setEditTextNumber(playerTO.getNumber());
				teamDialog.setEditTextPosition(playerTO.getPosition());
				teamDialog.setEditTextName(playerTO.getName());
				
				// Caption Checkbox--Data_Team_Setting
				teamTO = TeamSupport.ReadTeamSetting();
				if(playerTO.getNumber().equals(teamTO.getCaptionPlayerNumber())){
					teamDialog.getCheckBoxIsCaptain().setChecked(true);
				}else{
					teamDialog.getCheckBoxIsCaptain().setChecked(false);
				}
				dialogType = ConstantVariable.DIALOG_UPDATE;
				return false;
			}
		});
		
		
		// Add Match
		Button buttonMatchAdd = (Button) findViewById(R.id.button_team_add);
		buttonMatchAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				teamDialog.show();
				teamDialog.setEditTextNumber("");
				teamDialog.setEditTextPosition("");
				teamDialog.setEditTextName("");
				dialogType = ConstantVariable.DIALOG_ADD;
			}
		});
	}
	
	
	// Dialog Back
	public void back(PlayerTO playerTO) {
		switch (dialogType) {
			case ConstantVariable.DIALOG_ADD: {
				listPlayerTO.add(playerTO);
				break;
			}
			case ConstantVariable.DIALOG_UPDATE: {
				listPlayerTO.set(listIndex, playerTO);
				break;
			}
		}
		// Set Default
		dialogType = ConstantVariable.DIALOG_DEFAULT;
		// Write
		TeamSupport.WriteTeamPlayer(listPlayerTO);
	}

}
