package com.ff.widgets;

import com.android.club.R;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


@SuppressLint("NewApi")
public class WaitingPointBar extends LinearLayout{
    private static final int NUM=5;
    private Context context;
    private ImageView mOldDot;
    
    public static String MESSAGE_TEXT_WAITING = "Loading...";
    public static String MESSAGE_TEXT_ERROR_UNKNOWN_HOST_EXCEPTION = "UnknownHostException";
    public static String MESSAGE_TEXT_ERROR_THE_SERVER_CAN_NOT_ACCESS = "The server can not access";
    public static int MESSAGE_ERROR_UNKNOWN_HOST_EXCEPTION_NUMBER = 990;
    public static int MESSAGE_ERROR_THE_SERVER_CAN_NOT_ACCESS_NUMBER = 991;
    
    public WaitingPointBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context=context;
        init(context);
        // TODO Auto-generated constructor stub
    }

    public WaitingPointBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init(context);
        // TODO Auto-generated constructor stub
    }

    public WaitingPointBar(Context context) {
        super(context);
        this.context=context;
        init(context);
        // TODO Auto-generated constructor stub
    }
    
    private void init(Context context){
        this.setOrientation(LinearLayout.VERTICAL);
        this.setGravity(Gravity.CENTER);
        
        // Bar
        LinearLayout linearLayoutBar = new LinearLayout(context);
        linearLayoutBar.setGravity(Gravity.CENTER);
        linearLayoutBar.setOrientation(LinearLayout.HORIZONTAL);
        
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.widgets_waiting_point_bar_selected_no);
        LinearLayout.LayoutParams tLayoutParams=new LinearLayout.LayoutParams(bitmap.getWidth(),
                bitmap.getHeight());
        tLayoutParams.leftMargin=10;
        tLayoutParams.rightMargin=10;
        for (int i = 0; i < NUM; i++) {
            ImageView vDot = new ImageView(context);
            vDot.setLayoutParams(tLayoutParams);
            if (i == 0) {
                vDot.setBackgroundResource(R.drawable.widgets_waiting_point_bar_selected_yes);
            } else {
                vDot.setBackgroundResource(R.drawable.widgets_waiting_point_bar_selected_no);
            }
            linearLayoutBar.addView(vDot);
        }
        mOldDot=(ImageView) linearLayoutBar.getChildAt(0);
        new UpdateHandler().sendEmptyMessage(0);
        
        // Text
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0, 10, 0, 10);
        textView.setText(MESSAGE_TEXT_WAITING);
        
        this.addView(linearLayoutBar);
        this.addView(textView);
    }
    
    class UpdateHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            int tPosition=msg.what;
            if(mOldDot!=null){
            	mOldDot.setBackgroundResource(R.drawable.widgets_waiting_point_bar_selected_no);
            }
            // 
            LinearLayout linearLayoutBar = (LinearLayout)WaitingPointBar.this.getChildAt(0);
            ImageView currentDot = (ImageView) linearLayoutBar.getChildAt(tPosition);
            currentDot.setBackgroundResource(R.drawable.widgets_waiting_point_bar_selected_yes);
            mOldDot=currentDot;
            if(++tPosition==NUM){
            	tPosition=0;
            }
            this.sendEmptyMessageDelayed(tPosition, 200);
        }
    }
}
