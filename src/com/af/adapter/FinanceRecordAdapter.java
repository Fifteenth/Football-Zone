package com.af.adapter;

import java.util.List;

import com.android.club.R;
import com.cf.base.variable.ConstantVariable;
import com.cf.to.FinanceTO;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

public class FinanceRecordAdapter implements ListAdapter{

	private List<FinanceTO> list;

	/** 实例及其对应的视图布局的XML文件 */
	private LayoutInflater layoutInflater;

	public FinanceRecordAdapter(Context context, List<FinanceTO> list) {
		this.list = list;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * 控制ITEM 布局内容
	 */
	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {
		if (convertView == null) {
			// 加载布局
			convertView = layoutInflater.inflate(R.layout.listview_finance_record, null);

			FinanceTO financeTO = list.get(position);
			
			int amount = financeTO.getAmount();
			String amountString; 
			if(amount > 0 && amount < 10){
				amountString = "    " + amount + ConstantVariable.FINANCE_SYSMBOL;
			}else if(amount >= 10 && amount <100){
				amountString = "  " + amount + ConstantVariable.FINANCE_SYSMBOL;
			}else{
				amountString = ConstantVariable.SYSBOL_DOUBLE_QUOTES 
								+ amount 
								+ ConstantVariable.FINANCE_SYSMBOL;
			}
			
			
			TextView textviewMatchInfo = (TextView) convertView.findViewById(R.id.textview_matchInfo);
			textviewMatchInfo.setText(
					financeTO.getNumber()
					+ "-"
					+ financeTO.getName()
					+ "       "
					+ financeTO.getType()
					+ amountString);
			
			TextView textviewDatetime = (TextView) convertView.findViewById(R.id.textview_datetime);
			textviewDatetime.setText(financeTO.getCurrentTime());

		
			TextView textviewDescription = (TextView) convertView.findViewById(
					R.id.finance_record_decription);
			
			// Descripition
			String description = financeTO.getDescripition();
			if(description == null){
				description = "";
			}
			textviewDescription.setText(description);
		}
		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * 是否Item监听
	 */
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * true所有项目可选择可点击
	 */
	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * 是否显示分割线
	 */
	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return true;
	}
}
