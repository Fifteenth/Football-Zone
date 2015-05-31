package com.af.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.af.adapter.impl.CFAdapter;
import com.android.club.R;
import com.cf.fix.GlobleTacticalClass;
import com.cf.to.TacticalFileTO;


public class TacticalFileAdapter extends CFAdapter{
	
	
	private Context context;
	private List <TacticalFileTO>list;
	
	public TacticalFileAdapter(Context context, List<TacticalFileTO> list){
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
			convertView = LayoutInflater.from(context).inflate(R.layout.listview_tactical_file, null);
			
			// TextView 
			TextView textviewFileId = (TextView) convertView.findViewById(R.id.textview_file_id);
			TextView textviewFileFormation= (TextView) convertView.findViewById(R.id.textview_file_formation);
			TextView textviewFileDescription = (TextView) convertView.findViewById(R.id.textview_file_description);
			TextView textviewFileDatetime = (TextView) convertView.findViewById(R.id.textview_file_datetime);
			
			// TextView Text
			textviewFileId.setText(String.valueOf(Integer.valueOf(list.get(position).getSaveNum()) + 1));
			textviewFileFormation.setText(list.get(position).getFormation());
			textviewFileDescription.setText(list.get(position).getDescribtion());
			textviewFileDatetime.setText(list.get(position).getDatetime());
			
			// ImageView
			ImageView imageView = (ImageView) convertView.findViewById(R.id.image_file);
			int id;
			// saveNumberList必须先初始化
			if(GlobleTacticalClass.saveNumberList.contains(String.valueOf(position))){
				id = convertView.getResources().getIdentifier(
						"tactical_file_filled", "drawable" , context.getPackageName()); 
			}else{
				id = convertView.getResources().getIdentifier(
						"tactical_file_empty", "drawable" , context.getPackageName()); 
			}
			imageView.setBackgroundResource(id);
						
		}
		
		return convertView;
	}

}
