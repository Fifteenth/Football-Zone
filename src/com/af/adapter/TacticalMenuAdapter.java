package com.af.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.af.adapter.impl.CFAdapter;
import com.android.club.R;


public class TacticalMenuAdapter extends CFAdapter{
	
	
	private Context context;
	private List <String>list;
	private TextView textviewMenuItem;
	
	public TacticalMenuAdapter(Context context, List<String> list){
		super(context, list);
		this.context = context;
		this.list = list;
	}
	
	public List<String> getList() {
		return list;
	}
	public void setList(List<String> list) {
		this.list = list;
	}
	/**
	 * 控制ITEM 布局内容
	 */
	@Override
	public View getView(int position, View convertView,
			ViewGroup parent) {
		
		if (convertView == null) {
			// 加载布局
			convertView = LayoutInflater.from(context).inflate(R.layout.listview_tactical_menu, null);
			
			// Position
			textviewMenuItem = (TextView) convertView.findViewById(R.id.textview_menu_item);
		}
		textviewMenuItem.setText(list.get(position));
		return convertView;
	}

}
