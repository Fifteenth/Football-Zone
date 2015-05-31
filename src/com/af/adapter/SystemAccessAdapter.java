package com.af.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.af.adapter.impl.CFAdapter;
import com.android.club.R;

public class SystemAccessAdapter extends CFAdapter{
	
	
	private Context context;
	private List <JSONObject>list;
	
	public SystemAccessAdapter(Context context, List<JSONObject> list){
		super(context, list);
		this.context = context;
		this.list = list;
	}
	/**
	 * 控制ITEM 布局内容
	 */
	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {
		
		if (convertView == null) {
			// 加载布局
			convertView = LayoutInflater.from(context).inflate(R.layout.listview_system_access, null);
			
			// TextView 
			TextView textviewDate = (TextView) convertView.findViewById(R.id.textview_date);
			TextView textviewIp = (TextView) convertView.findViewById(R.id.textview_ip);
			TextView textviewUri= (TextView) convertView.findViewById(R.id.textview_uri);
			
			// TextView Text
			try {
				JSONObject json = list.get(position);
				textviewIp.setText("IP:" + json.get("ip").toString());
				textviewUri.setText("URI:" + json.get("uri").toString());
				
				SimpleDateFormat sdf = new SimpleDateFormat("MM/ dd/yyyy HH:mm:ss");
				Date date= new Date((Long) json.get("accessDateTime"));
				textviewDate.setText("Date:" + sdf.format(date));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return convertView;
	}

}
