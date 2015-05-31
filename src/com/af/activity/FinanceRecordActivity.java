package com.af.activity;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.af.adapter.FinanceRecordAdapter;
import com.android.club.R;
import com.cf.base.util.FileUtil;
import com.cf.base.util.SDCardUtil;
import com.cf.base.variable.FileVariable;
import com.cf.service.FinanceService;
import com.cf.to.FinanceTO;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class FinanceRecordActivity extends Activity {

	public static String playerName;
	
	List<FinanceTO> financePaymentList = new ArrayList<FinanceTO>();
	ListView listview;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_finance_record);

		List <FinanceTO> financePaymentList= getFinanceTOList();
		if(financePaymentList!=null
				&&financePaymentList.size()!=0){
			listview = (ListView) this.findViewById(R.id.listView_my);

			// 实例化自定义适配器
			FinanceRecordAdapter adapter = new FinanceRecordAdapter(this, financePaymentList);

			listview.setAdapter(adapter);

			listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// Toast.makeText(TeamActivity.this, "选中的是:" + position,
					// Toast.LENGTH_SHORT).show();

					listview.setItemChecked(position, true);
				}
			});
		}
	}

	public List <FinanceTO> getFinanceTOList(){
		String sdCardRootPath = SDCardUtil.getRootPath();
		InputStream inputStreamFinance = FileUtil.getFileInputStream(
				new File(sdCardRootPath,FileVariable.FINANCE_PAYMENT));
		List <FinanceTO> financeTOList = new ArrayList<FinanceTO>();;
		if(inputStreamFinance!=null){
			financeTOList = FinanceService.getFinanceTOList(inputStreamFinance,playerName);
		}
		inputStreamFinance = FileUtil.getFileInputStream(
				new File(sdCardRootPath,FileVariable.FINANCE_DEDUCTION));
		if(inputStreamFinance!=null){
			financeTOList.addAll(FinanceService.getFinanceTOList(inputStreamFinance,playerName));
		}
		
		return financeTOList;
	}
}
