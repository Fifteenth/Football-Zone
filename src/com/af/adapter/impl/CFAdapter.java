package com.af.adapter.impl;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CFAdapter extends BaseAdapter{
	
	
	private List<?>list;
	
	public CFAdapter(Context context, List<?> list){
		this.list = list;
	}

//	@Override
//	public void registerDataSetObserver(DataSetObserver observer) {
//	}
//
//	@Override
//	public void unregisterDataSetObserver(DataSetObserver observer) {
//	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

}
