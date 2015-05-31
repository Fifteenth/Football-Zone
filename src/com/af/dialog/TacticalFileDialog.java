package com.af.dialog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.af.activity.TacticalActivity;
import com.af.adapter.TacticalFileAdapter;
import com.af.view.FootballFieldView;
import com.android.club.R;
import com.as.view.FootballFieldViewSupport;
import com.cf.base.util.DateUtil;
import com.cf.base.variable.ConstantVariable;
import com.cf.base.variable.FileVariable;
import com.cf.club.Player;
import com.cf.fix.GlobleTacticalClass;
import com.cf.service.RWTOService;
import com.cf.to.TacticalFileTO;
import com.cf.to.TacticalSettingTO;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class TacticalFileDialog extends Dialog{
	
	TacticalFileDetailDialog tacticalFileDetailDialog;
	
	TacticalFileAdapter adapter;
	
	//定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener{
            public void back(String name);
    }
	
    public final static int DIALOG_TYPE_READ = 1;
    public final static int DIALOG_TYPE_SAVE = 2;
    
	private ListView listView;
	private int listviewSelectIndex;
	
	private TacticalActivity tacticalActivity;
	private int dialogType;
	
	public TacticalFileDialog(Context context,int dialogType) {
    	super(context);
    	this.tacticalActivity = (TacticalActivity)context;
    	this.dialogType = dialogType;
	}
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.dialog_tactical_file);
//        setContentView(R.style.dialog_tactical_style);
        
        listView = (ListView) this.findViewById(R.id.listview_file);
        
        // 实例化自定义适配器
 		adapter = new TacticalFileAdapter(
 				this.getContext(),GlobleTacticalClass.fileList);
 		listView.setAdapter(adapter);
 		
 		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				
				listviewSelectIndex = position;
				
				if(dialogType == DIALOG_TYPE_SAVE){
					tacticalFileDetailDialog = new TacticalFileDetailDialog(
							getContext(),new TacticalFileDetailDialog.OnCustomDialogListener(){

								@Override
								public void back(String formation,String descrition) {
									//回调函数
								}
							});
					
					// Show Dialog
					showDialog(DIALOG_TYPE_SAVE);
				}else if(dialogType == DIALOG_TYPE_READ){
					// TODO
				}
			}
		});
 		
 		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

//				listviewSelectIndex = position;
//				
//				if(dialogType == DIALOG_TYPE_READ){
//					tacticalFileDetailDialog = new TacticalFileDetailDialog(
//							getContext(),new TacticalFileDetailDialog.OnCustomDialogListener(){
//
//								@Override
//								public void back(String formation,String descrition) {
//									//回调函数
//								}
//							});
//					tacticalFileDetailDialog.show();
//					showDialog(DIALOG_TYPE_READ);
//				}
				
				// Intercept onItemClick
				return true;
			}
		});
 		
 		// 
 		Button clickBtn = (Button) findViewById(R.id.button_file_save);
 		clickBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(dialogType == DIALOG_TYPE_SAVE){
					// Get othersFileList
					List <TacticalFileTO>othersFileList = readFile(DIALOG_TYPE_SAVE);
					saveFile(othersFileList);
				}else if(dialogType == DIALOG_TYPE_READ){
					readFile(DIALOG_TYPE_READ);
				}
				
				// End
				dismiss();
			}
		});
	}
	
	@Override
	public void show() {
		setTitle(ConstantVariable.DIALOG_TITLE_TACTICAL_CN);
		super.show();
	}
	
	public void saveFile(List <TacticalFileTO> tacticalFileTOList){
		// 存到选中的文件夹中
		List <Player> listA = tacticalActivity.getFootballField().getPlayerAList();
		List <Player> listB = tacticalActivity.getFootballField().getPlayerBList();
		
		List <Player> listC = new ArrayList<Player>();
		listC.add(tacticalActivity.getFootballField().getFootball());
		
		ArrayList <TacticalFileTO>currentList = new ArrayList<TacticalFileTO>();
		
		// a
		for(int i=0;i<listA.size();i++){
			Player player = listA.get(i);
			currentList = sotTacticalFileTOAttibute(currentList,player,"a");
		}
		
		// b
		List <TacticalSettingTO>list = GlobleTacticalClass.gotTacticalSetting();
		if(list.get(0).getDisplayCompetitor().equals("true")){
			for(int i=0;i<listB.size();i++){
				Player player = listB.get(i);
				currentList = sotTacticalFileTOAttibute(currentList,player,"b");
			}
		}
		
		// c
		currentList = sotTacticalFileTOAttibute(
				currentList,tacticalActivity.getFootballField().getFootball(),"c");
		
		tacticalFileTOList.addAll(currentList);
		try {
			RWTOService.writeXMLFromListTOAndSave(tacticalFileTOList,
					TacticalFileTO.classPath, FileVariable.FILE_NAME_PLAYER_MOVE_FILE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List <TacticalFileTO> readFile(int type){
		List <TacticalFileTO> savedFileList = null;
		try {
			savedFileList = (List <TacticalFileTO>)RWTOService.gotListTOFromXML(
					TacticalFileTO.classPath, FileVariable.FILE_NAME_PLAYER_MOVE_FILE);
			// DIALOG_TYPE_READ
			if(type == DIALOG_TYPE_READ){
				List <TacticalFileTO> selectedList = new ArrayList<TacticalFileTO>();
				for(int i=0;i<savedFileList.size();i++){
					TacticalFileTO tacticalFileTO = savedFileList.get(i);
					if(tacticalFileTO.getSaveNum().equals(String.valueOf(listviewSelectIndex))){
						selectedList.add(tacticalFileTO);
					}
					GlobleTacticalClass.tacticalFileTOList = selectedList;
					
					FootballFieldView footballFieldView = tacticalActivity.getFootballField();
					
					FootballFieldViewSupport.dealWithFileData(
							footballFieldView.getPlayerAList(),
							footballFieldView.getPlayerBList(),
							footballFieldView.getFootball());
					
				}
				return selectedList;
			}else if(type == DIALOG_TYPE_SAVE){
				List <TacticalFileTO> othersFileList = new ArrayList<TacticalFileTO>();
				for(int i=0;i<savedFileList.size();i++){
					TacticalFileTO tacticalFileTO = savedFileList.get(i);
					if(!savedFileList.get(i).getSaveNum().equals(String.valueOf(listviewSelectIndex))){
						othersFileList.add(tacticalFileTO);
					}
				}
				GlobleTacticalClass.needUpdateSaveNumberList = true;
				return othersFileList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return savedFileList;
	}
	
	public ArrayList <TacticalFileTO> sotTacticalFileTOAttibute(
			ArrayList <TacticalFileTO> list,Player player,String type){
		TacticalFileTO tacticalFileTO = new TacticalFileTO();
		tacticalFileTO.setNumber(player.getNumber());
		
		JSONObject moveStepJSon = player.getPathAllStepAllJSon();
		if(moveStepJSon!= null){
			tacticalFileTO.setPathAllStepAllJSon(moveStepJSon.toString());
			tacticalFileTO.setPathAllTotal(String.valueOf(player.getPathAllTotal()));
			tacticalFileTO.setPathXStepTotalJSon(String.valueOf(player.getPathXStepTotalJSon()));
		}
		tacticalFileTO.setSaveNum(String.valueOf(listviewSelectIndex));
		tacticalFileTO.setRole(type);
		tacticalFileTO.setFormation(tacticalFileDetailDialog.getFormation());
		tacticalFileTO.setDescribtion(tacticalFileDetailDialog.getDescription());
		tacticalFileTO.setDatetime(DateUtil.getCurrentTimeYYYYMMDDhhmmss());
		list.add(tacticalFileTO);
		return list;
	}
	
	/**
	 * 
	 */
	public void showDialog(int dialogType){
		
		boolean enabled;
		if(dialogType == DIALOG_TYPE_READ){
			enabled = false;
		}else{
			enabled = true;
		}
		// Inin Dialog
		tacticalFileDetailDialog.show();
		// Show Detail
		TacticalFileTO tacticalFileTO = GlobleTacticalClass.fileList.get(
				listviewSelectIndex);
		if(isFileSave(listviewSelectIndex)){
			EditText edittextFormation =  tacticalFileDetailDialog.getEdittextFormaiton();
			EditText edittextDescription =  tacticalFileDetailDialog.getEdittextDescription();
			
			edittextFormation.setEnabled(enabled);
			edittextDescription.setEnabled(enabled);
			
			edittextFormation.setText(
					tacticalFileTO.getFormation());
			edittextDescription.setText(
					tacticalFileTO.getDescribtion());
		}
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public boolean isFileSave(int index){
		if(GlobleTacticalClass.fileList.get(index).getType().equals(
				GlobleTacticalClass.TYPE_FILE_SAVEED)){
			return true;
		}else{
			return false;
		}
	}
	
}
