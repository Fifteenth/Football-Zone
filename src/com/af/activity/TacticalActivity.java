package com.af.activity;

import com.af.adapter.TacticalMenuAdapter;
import com.af.dialog.FormationSelectedDialog;
import com.af.dialog.TacticalFileDialog;
import com.af.view.FootballFieldView;
import com.android.club.R;
import com.cf.base.variable.ConstantVariable;
import com.cf.fix.GlobleTacticalClass;
import com.cf.fix.InitTacticalClass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class TacticalActivity extends Activity {
	
	public TacticalFileDialog tacticalFileDialog;
	
	public FootballFieldView footballField;
	
	public FootballFieldView getFootballField() {
		return footballField;
	}

	public void setFootballField(FootballFieldView footballField) {
		this.footballField = footballField;
	}

	public static String formation;
	public TacticalMenuAdapter adapter;
	private ListView listView;
	
    public ListView getListView() {
		return listView;
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
		// Tactical Init
		InitTacticalClass.init();
		
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_tactical);
        footballField = (FootballFieldView) this.findViewById(R.id.footballField);
    	
    	// ListView
    	listView = (ListView) this.findViewById(R.id.listview_menu_left);
    	
    	// 实例化自定义适配器
		adapter = new TacticalMenuAdapter(this,GlobleTacticalClass.menuLeftList);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				if(position == 0){
					if(footballField.moveType == FootballFieldView.MOVE_TYPE_POSITION){
						footballField.setShowDisplay(ConstantVariable.menuStart);
						footballField.moveType = FootballFieldView.MOVE_TYPE_ARROW;
					}else{
						++ footballField.pathAllTotalInt;
					}
				}else if(position == 1){
					footballField.setShowDisplay(ConstantVariable.menuPause);
				}else if(position == 2){
					footballField.setShowDisplay(ConstantVariable.menuStop);
					footballField.moveType = FootballFieldView.MOVE_TYPE_POSITION;
				}else if(position == 3){
					footballField.displayTactics();
				}else{
					// Setting
					Intent activity = new Intent(TacticalActivity.this, TacticalSettingActivity.class);
					startActivity(activity);
				}
				// Hide ListView
				listView.setVisibility(View.GONE);
			}
		});
    }
    
  	@Override
  	public boolean onCreateOptionsMenu(Menu menu) {
  		super.onCreateOptionsMenu(menu);
  		menu.add(0, 11, 0, ConstantVariable.MENU_BUTTOM_FORMATION_SELECT); 
  		menu.add(0, 12, 1, ConstantVariable.MENU_BUTTOM_FORMATION_SAVE); 
  		menu.add(0, 21, 2, ConstantVariable.MENU_BUTTOM_FILE_READ); 
  		menu.add(1, 22, 3, ConstantVariable.MENU_BUTTOM_FILE_SAVE); 
  		return true;
  	}
  	
  	@Override
  	public boolean onOptionsItemSelected(MenuItem item) {
  		
  		switch (item.getItemId()){
  				
  			case 11:
  				FormationSelectedDialog.lastSelection = FormationSelectedDialog.DEFAULT_SELECTION;
  				FormationSelectedDialog formationSelectedDialog = 
  					new FormationSelectedDialog(this);
  				
  				formationSelectedDialog.show();
  				break;
  				
  			// 读档
  			case 21:
  				if(GlobleTacticalClass.needUpdateSaveNumberList){
  					GlobleTacticalClass.updateSaveNumberList();
  				}
  				tacticalFileDialog = new TacticalFileDialog(this,TacticalFileDialog.DIALOG_TYPE_READ);
  				tacticalFileDialog.show();

  				break;
  				
  			// 存档
  			case 22:
  				if(GlobleTacticalClass.needUpdateSaveNumberList){
  					GlobleTacticalClass.updateSaveNumberList();
  				}
  				tacticalFileDialog = new TacticalFileDialog(this,TacticalFileDialog.DIALOG_TYPE_SAVE);
  				tacticalFileDialog.show();
  				
  				break;
  				
  			default:
  				break;
  		}
  		return true;
  	}
  	
}