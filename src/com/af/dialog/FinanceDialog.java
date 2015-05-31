package com.af.dialog;

import java.util.List;

import com.af.activity.FinanceActivity;
import com.android.club.R;
import com.cf.base.util.DateUtil;
import com.cf.base.util.ValidateUtil;
import com.cf.base.variable.ConstantVariable;
import com.cf.base.variable.FileVariable;
import com.cf.service.FinanceService;
import com.cf.to.FinanceTO;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FinanceDialog extends Dialog{

	//定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener{
            public void back(String name);
    }
    
    
    private FinanceActivity financeActivity;
   
    private String name;
    private FinanceTO selectedFinanceTO;
    private List<FinanceTO>financeList;
	private List<FinanceTO>financePaymentList;
    private List<FinanceTO>financeDeductionList;
    private int financeListSelectedIndex;
    
    private OnCustomDialogListener customDialogListener;
    private EditText edittextAmount;
    private EditText edittextDescription;

    public int dialogType;
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public FinanceTO getSelectedFinanceTO() {
		return selectedFinanceTO;
	}

	public void setSelectedFinanceTO(FinanceTO selectedFinanceTO) {
		this.selectedFinanceTO = selectedFinanceTO;
	}
	
	public List<FinanceTO> getFinanceList() {
		return financeList;
	}

	public void setFinanceList(List<FinanceTO> financeList) {
		this.financeList = financeList;
	}

	public List<FinanceTO> getFinancePaymentList() {
		return financePaymentList;
	}

	public void setFinancePaymentList(List<FinanceTO> financePaymentList) {
		this.financePaymentList = financePaymentList;
	}

	public List<FinanceTO> getFinanceDeductionList() {
		return financeDeductionList;
	}

	public void setFinanceDeductionList(List<FinanceTO> financeDeductionList) {
		this.financeDeductionList = financeDeductionList;
	}
	
	public int getFinanceListSelectedIndex() {
		return financeListSelectedIndex;
	}

	public void setFinanceListSelectedIndex(int financeListSelectedIndex) {
		this.financeListSelectedIndex = financeListSelectedIndex;
	}

    public FinanceDialog(Context context,OnCustomDialogListener customDialogListener) {
    	super(context);
    	this.customDialogListener = customDialogListener;
    	this.financeActivity = (FinanceActivity) context;
    }
    
   @Override
   protected void onCreate(Bundle savedInstanceState) { 
           

	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.dialog_finance);
	   //设置标题
	   edittextAmount = (EditText)findViewById(R.id.edittext_amount);
	   edittextDescription = (EditText)findViewById(R.id.edittext_description);
	   Button clickBtn = (Button) findViewById(R.id.buttonConfirmPayment);
	   clickBtn.setOnClickListener(clickListener);
   }
    
   public void showDialog(int dialogType) {
	   switch(dialogType){
	 
		   case 1:
			   this.dialogType = ConstantVariable.FINANCE_TYPE_PAYMENT;
			   break;
		
		   case 2:
			   this.dialogType = ConstantVariable.FINANCE_TYPE_DEDUCTION;
			   break;
			   
		   case 3:
			   this.dialogType = ConstantVariable.FINANCE_TYPE_RECORD;
			   break;
	   
	   }
	   
	   setTitle(name);
	   if(edittextAmount!=null){
		   edittextAmount.setText("");   
	   }
	   super.show();
   };

	private View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			customDialogListener.back(String.valueOf(edittextAmount.getText()));
			FinanceDialog.this.dismiss();

			int amountText = 0;
			String amountString = "";
			String describition = "";
			Editable editableAmount = edittextAmount.getText();
			Editable editableDescribition = edittextDescription.getText();
			if(editableAmount != null){
				amountString = editableAmount.toString();
			}
			if(amountString.equals("")){
				return;
			}
			if(editableDescribition != null){
				describition = editableDescribition.toString();
			}
			selectedFinanceTO.setDescripition(describition);
			
			if(!ValidateUtil.isNumeric(amountString)){
				new  AlertDialog.Builder(financeActivity)    
			    .setTitle("提示：" )
			    .setMessage("请输入数字!" )
			    .setPositiveButton("确定" ,null).show(); 
				return;
			}
			
			try {
				switch (dialogType) {
				case 1:
					
					amountText = Integer.valueOf(amountString);
					financeAction(dialogType,amountText);
					break;

				case 2:
					
					amountText = Integer.valueOf(amountString);
					financeAction(dialogType,amountText);
					break;

				case 3:

					break;
				}
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    };
    
    
    public void financeAction(int dialogType,int amountText) throws Exception{
		int amountOld = selectedFinanceTO.getAmount();
		selectedFinanceTO.setAmount(amountText);
		// Replace 
		// Add Amount
		int amountTotal;
		selectedFinanceTO.setCurrentTime(DateUtil.getCurrentTimeYYYYMMDDhhmmss());
		if(dialogType==ConstantVariable.FINANCE_TYPE_PAYMENT){
			selectedFinanceTO.setType(ConstantVariable.SYSBOL_PLUS);
			financePaymentList.add(selectedFinanceTO);
			amountTotal = amountOld + amountText;
			// finance_payment.xml
			saveFinanceXML(financePaymentList,FileVariable.FINANCE_PAYMENT);
		}else{
			selectedFinanceTO.setType(ConstantVariable.SYSBOL_MINUS);
			financeDeductionList.add(selectedFinanceTO);
			amountTotal = amountOld - amountText;
			// finance_deduction.xml
			saveFinanceXML(financeDeductionList,FileVariable.FINANCE_DEDUCTION);
		}
		 
		financeList.get(financeListSelectedIndex).setAmount(amountTotal);
		
		
		// Finance.xml
		saveFinanceXML(financeList,FileVariable.FINANCE);
		// Refresh
		RefreshFinanceActivity();
    }
    
    public void RefreshFinanceActivity(){
    	financeList = null;
//		financeActivity.financeList.clear();
		financeActivity.onCreate(null);
    }
    
    public void saveFinanceXML(List<FinanceTO> financeTOList,String fileName) 
    		throws Exception{
    	
		FinanceService.getWriteXMLAndSave(financeTOList, fileName);
    }
    
    

}
