package com.af.dialog;



import com.android.club.R;
import com.cf.base.variable.ConstantVariable;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TacticalFileDetailDialog extends Dialog{
	
	private OnCustomDialogListener customDialogListener;
	
	private EditText edittextFormaiton;
	private EditText edittextDescription;
	
	public OnCustomDialogListener getCustomDialogListener() {
		return customDialogListener;
	}

	public void setCustomDialogListener(OnCustomDialogListener customDialogListener) {
		this.customDialogListener = customDialogListener;
	}

	public EditText getEdittextFormaiton() {
		return edittextFormaiton;
	}

	public void setEdittextFormaiton(EditText edittextFormaiton) {
		this.edittextFormaiton = edittextFormaiton;
	}

	public EditText getEdittextDescription() {
		return edittextDescription;
	}

	public void setEdittextDescription(EditText edittextDescription) {
		this.edittextDescription = edittextDescription;
	}
	
	public String getFormation(){
		Editable editable = edittextFormaiton.getText();
		if(editable != null){
			return editable.toString();
		}
		return null;
	}
	
	public String getDescription(){
		Editable editable = edittextDescription.getText();
		if(editable != null){
			return editable.toString();
		}
		return null;
	}
	
	//定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener{
            public void back(String formation,String description);
    }
	
	public TacticalFileDetailDialog(Context context,
			OnCustomDialogListener customDialogListener) {
    	super(context);
    	this.customDialogListener = customDialogListener;
   	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.dialog_tactical_detail_file);

        
        edittextFormaiton = (EditText)findViewById(R.id.edit_hint_formtion);
        edittextDescription = (EditText)findViewById(R.id.edit_hint_description);
        
        Button clickBtn = (Button) findViewById(R.id.button_confirm);
 		clickBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				customDialogListener.back(edittextFormaiton.getText().toString()
//						,edittextDescription.getText().toString());
				// End
				dismiss();
			}
		});
	}
	
	@Override
	public void show() {
		setTitle(ConstantVariable.DIALOG_TITLE_TACTICAL_DETAIL_CN);
		super.show();
	}
	
}
