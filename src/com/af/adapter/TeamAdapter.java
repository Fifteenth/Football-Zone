package com.af.adapter;

import java.util.List;

import com.android.club.R;
import com.cf.support.TeamSupport;
import com.cf.to.PlayerTO;
import com.cf.to.TeamTO;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class TeamAdapter implements ListAdapter{
	
	/** 实例及其对应的视图布局的XML文件 */
	private Context context;
	private LayoutInflater layoutInflater;
	private int layoutInt;
	private List<PlayerTO> list; 
	private Resources resources;
	
	public TeamAdapter(Context context, List<PlayerTO> list,int layoutInt,Resources resources) {
		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
		this.list = list;
		this.layoutInt = layoutInt;
		this.resources = resources;
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
			convertView = layoutInflater.inflate(layoutInt, null);
			
			// Number Bitmap
			ImageView imageView = (ImageView) convertView.findViewById(R.id.image_team_number);
			
			String number = list.get(position).getNumber();
			String pngName;
			if(number.length()==1){
				pngName = "player_avatar_0" + number;
			}else{
				pngName = "player_avatar_" + number;
			}
			
			int id = resources.getIdentifier(pngName, "drawable" , context.getPackageName()); 
			
			imageView.setBackgroundResource(id);
			// Position
			TextView tv_1 = (TextView) convertView.findViewById(R.id.textview_team_position);
			tv_1.setText(list.get(position).getPosition());
			// Name
			TextView tv_2 = (TextView) convertView.findViewById(R.id.textview_team_name);
			tv_2.setText(list.get(position).getName());
			// Captain Bitmap
			ImageView ivc = (ImageView) convertView.findViewById(R.id.image_team_captain);
			TeamTO teamTO = TeamSupport.ReadTeamSetting();
			if(number.equals(teamTO.getCaptionPlayerNumber())){
				ivc.setBackgroundResource(R.drawable.player_caption);
			}
						
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


	
	/**
	 * ListView内容实体类
	 * 
	 * @author Administrator
	 * 
	 */
	class DetailEntity {
		/** 布局ID */
		private int layoutID;

		/** 图片ID */
		private int bitmap;

		/** 标题 */
		private String title;

		/** 内容 */
		private String text;

		/** 按钮名称 */
		private String BtnText;

		public String getBtnText() {
			return BtnText;
		}

		public void setBtnText(String btnText) {
			BtnText = btnText;
		}

		public int getLayoutID() {
			return layoutID;
		}

		public int getBitmap() {
			return bitmap;
		}

		public void setBitmap(int bitmap) {
			this.bitmap = bitmap;
		}

		public void setLayoutID(int layoutID) {
			this.layoutID = layoutID;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}
	}
}
