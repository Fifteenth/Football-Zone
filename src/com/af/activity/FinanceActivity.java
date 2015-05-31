package com.af.activity;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.af.adapter.FinanceAdapter;
import com.android.club.R;
import com.cf.base.util.FileUtil;
import com.cf.base.util.SDCardUtil;
import com.cf.base.variable.FileVariable;
import com.cf.service.FinanceService;
import com.cf.to.FinanceTO;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class FinanceActivity extends Activity{

	
	private ListView listView;
	private List <FinanceTO>financeList;
	private List <FinanceTO>financePaymentList;
	private List <FinanceTO>financeDeductionList;
	private FinanceTO selectedFinanceTO;
	
	
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
	
	public FinanceTO getSelectedFinanceTO() {
		return selectedFinanceTO;
	}

	public void setSelectedFinanceTO(FinanceTO selectedFinanceTO) {
		this.selectedFinanceTO = selectedFinanceTO;
	}
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Resources resources = this.getResources();
		setContentView(R.layout.activity_finance);
		
		InputStream inputStreamFinance = null;
		InputStream inputStreamFinancePayment = null;
		InputStream inputStreamFinanceDeduction = null;
		try {
			/*
			 * this.getBaseContext().getFilesDir()
			 */
			String sdCardRootPath = SDCardUtil.getRootPath();
			inputStreamFinance = FileUtil.getFileInputStream(
					new File(sdCardRootPath,FileVariable.FINANCE));
			inputStreamFinancePayment = FileUtil.getFileInputStream(
					new File(sdCardRootPath,FileVariable.FINANCE_PAYMENT));
			inputStreamFinanceDeduction = FileUtil.getFileInputStream(
					new File(sdCardRootPath,FileVariable.FINANCE_DEDUCTION));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//通过Resources，获得XmlResourceParser实例   
		if(inputStreamFinance!=null){
			financeList = FinanceService.getFinanceTOList(inputStreamFinance,null);
			financePaymentList = FinanceService.getFinanceTOList(inputStreamFinancePayment,null);
			financeDeductionList = FinanceService.getFinanceTOList(inputStreamFinanceDeduction,null);
		}else{
			financeList = new ArrayList<FinanceTO>();
		}
		
		listView = (ListView) this.findViewById(R.id.listView_my);
		// 实例化自定义适配器
		FinanceAdapter adapter = new FinanceAdapter(this,financeList,resources);
		
		// financeList
		if(financeList != null && financeList.size() > 0){
			listView.setAdapter(adapter);
		}
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				listView.setItemChecked(position, true);
			}
		});
	}

}
