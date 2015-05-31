package com.af.adapter;

import java.util.List;

import com.af.activity.MatchesActivity;
import com.android.club.R;
import com.cf.support.MatchesSupport;
import com.cf.to.MatchTO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.BaseAdapter;

public class MatchesAdapter extends BaseAdapter{
	
	/** 实例及其对应的视图布局的XML文件 */
	private LayoutInflater layoutInflater;
	private int layoutInt;
	private List<MatchTO> list; 
	private Button button;
	private MatchesActivity matchesActivity;
	
	Button buttonMatchDel;
	
	public Button getButton() {
		return button;
	}

	public MatchesAdapter(Context context, List<MatchTO> list,int layoutInt) {
		this.list = list;
		this.layoutInt = layoutInt;
		this.layoutInflater = LayoutInflater.from(context);
		this.matchesActivity = (MatchesActivity)context;
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
			convertView = layoutInflater.inflate(layoutInt, null);
		}
		
//		convertView.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//
//				if(event.getAction() == MotionEvent.ACTION_DOWN){
//					System.out.println("----------------------Adapt:ACTION_DOWN");
//					return false;
//				}
//				if(event.getAction() == MotionEvent.ACTION_UP){
//					System.out.println("----------------------ACTION_UP");
//					return false;
//				}
//				return false;
//			}
//		});
		
		// Bind Data
		TextView tv_1 = (TextView) convertView.findViewById(R.id.title);
		tv_1.setText(
				  "第" 
				+ list.get(position).getRound()
				+ "轮"
				+ "    " 
				+ "JAVA足球队"
				+ " "
				+ list.get(position).getScore()
				+ " "
				+ list.get(position).getCompetitor());
		
		buttonMatchDel = (Button)convertView.findViewById(R.id.button_matchDel);
		buttonMatchDel.setBackgroundResource(R.drawable.button_delete);
		// Del Match
		buttonMatchDel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// AvoidResponseOnTouch
//				matchesActivity.setAvoidResponseOnTouch(true);
				// Remove
				list.remove(position);
				
				// Reflesh
				notifyDataSetChanged();
				
				// hidden Button
				matchesActivity.hiddenRight(null);
				
				// Save
				MatchesSupport.WriteMatches(list);
			}
		});
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
