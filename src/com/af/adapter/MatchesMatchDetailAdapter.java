package com.af.adapter;

import java.util.List;

import com.android.club.R;
import com.cf.to.MatchTO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.BaseAdapter;

public class MatchesMatchDetailAdapter extends BaseAdapter{
	
	/** 实例及其对应的视图布局的XML文件 */
	private List<MatchTO> list; 
	private Context context;

	public MatchesMatchDetailAdapter(Context context, List<MatchTO> list) {
		this.list = list;
		this.context = context;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.listview_matches_match_detail, null);
		}
		
		ImageView imageView = (ImageView) convertView.findViewById(R.id.img_goal);
		int id = convertView.getResources().getIdentifier(
				"img_goal", "drawable" , context.getPackageName()); 
		imageView.setBackgroundResource(id);
		
		TextView textviewGoalPlayer = (TextView) convertView.findViewById(
				R.id.textview_goal_player);
		textviewGoalPlayer.setText("2哥");
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
