package com.af.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

public class MatchesListView extends ListView{
	
	private int touchAction = -1;

	
	public int getTouchAction() {
		return touchAction;
	}

	public MatchesListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	

	public MatchesListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MatchesListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch(ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
            Log.i("ZZZZ", "ChildLayout onInterceptTouchEvent ACITON_DOWN:");
            break;
        case MotionEvent.ACTION_MOVE:
            Log.i("ZZZZ", "ChildLayout onInterceptTouchEvent ACITON_MOVE:");
            break;
        case MotionEvent.ACTION_CANCEL:
            Log.i("ZZZZ", "ChildLayout onInterceptTouchEvent ACITON_CANCEL:");
            break;
        case MotionEvent.ACTION_UP:
            Log.i("ZZZZ", "ChildLayout onInterceptTouchEvent ACITON_UP:");
            break;
        }
        return false;
    }
	
	@Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean result = super.onTouchEvent(ev);
        switch(ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
            Log.i("ZZZZ", "ChildLayout onTouchEvent ACTION_DOWN");
            break;
        case MotionEvent.ACTION_MOVE:
            Log.i("ZZZZ", "ChildLayout onTouchEvent ACTION_MOVE");
            break;
        case MotionEvent.ACTION_CANCEL:
            Log.i("ZZZZ", "ChildLayout onTouchEvent ACTION_CANCEL");
        case MotionEvent.ACTION_UP:
            Log.i("ZZZZ", "ChildLayout onTouchEvent ACTION_UP");
            break;
        }

        result = true;
        Log.i("ZZZZ", "ChildLayout onTouchEvent return "+result);
        return result;
   }
	
	
	
}
