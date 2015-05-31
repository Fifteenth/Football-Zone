package com.af.view;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import android.view.SurfaceView;
import android.view.View;
import android.widget.ListView;

import com.af.activity.TacticalActivity;
import com.af.adapter.TacticalMenuAdapter;
import com.as.view.FootballFieldViewSupport;
import com.cf.base.util.BitmapUtil;
import com.cf.base.util.DateUtil;
import com.cf.base.variable.ConstantVariable;
import com.cf.base.variable.SystemVariable;
import com.cf.club.Player;
import com.cf.fix.GlobleTacticalClass;
import com.cf.to.MenuTO;
import com.cf.to.Point;
import com.cf.to.TacticalSettingTO;

public class FootballFieldView extends SurfaceView {
	
	// Screen
	private int viewHeight;
	private int viewWidth;
	
	private float coefficientHeight;
	private float coefficientWidth;
	
	// Current Point
	private float currentPointX;
	private float currentPointY;
	
    //画笔对象
  	private final Paint paint = new Paint(Paint.DITHER_FLAG);
  	
  	//点大小
    private static int pointSize = 38;
  	
    private Context context=null;
    private String showDisplay;//1.start 2.pause 3.stop
	
	private List <Player> playerList;
    private List <Player> playerAList;
	private List <Player> playerBList;
	private Player football;
	
	private boolean isSlide = false;// 不滑动
	
    private int moveAngle = 0;
    
    private Long lastActionTime;
    private Long currentActionTime;
    
    private Player playerSelected;
    private Player playerOrigin;
    private Player playerMoved;
    
    private float currentMovedX;
    private float currentMovedY;
    
    private float actionDownX;
    private float actionDownY;
    
    final public static int MOVE_TYPE_POSITION = 1;
    final public static int MOVE_TYPE_ARROW = 2;
    
    public int moveType = MOVE_TYPE_POSITION;
    
    public int pathAllTotalInt = 1;
    
	public List<Player> getPlayerList() {
		return playerList;
	}

	public void setPlayerList(List<Player> playerList) {
		this.playerList = playerList;
	}
	
    public Player getFootball() {
		return football;
	}

	public void setFootball(Player football) {
		this.football = football;
	}

	public String getShowDisplay() {
		return showDisplay;
	}

	public void setShowDisplay(String showDisplay) {
		this.showDisplay = showDisplay;
	}

    public List<Player> getPlayerAList() {
		return playerAList;
	}

	public void setPlayerAList(List<Player> playerAList) {
		this.playerAList = playerAList;
	}

	public List<Player> getPlayerBList() {
		return playerBList;
	}

	public void setPlayerBList(List<Player> playerBList) {
		this.playerBList = playerBList;
	}

    //构造函数
   	public FootballFieldView(Context context, AttributeSet attrs) {
   		
		super(context, attrs);
		this.context =context;
		
		//Register Listener
		registerListener();
		
	}
   	
   	//自动匹配不同分辨率下的棋子大小
    public void autoChangeSize (int h){

		if (1800 < h && h < 2000) {// 1920
			pointSize = 48;
		} else if (1100 < h && h < 1800) {// 1280
			pointSize = 48;
		} else if (900 < h && h < 1100) {// 960
			pointSize = 48;
		} else if (700 < h && h < 900) {// 800
			pointSize = 38;
		} else {
			pointSize = 24;
		}
    }
   	
	//重新开始
	public void restart_Club(){
		restart();
	}
	
	@Override
	protected void onSizeChanged(int width, int height, int oldw, int oldh) {
		// 得到屏幕分辩率
		viewWidth = width;
		viewHeight = height;
		
		// 适应各种分辨率 系数
		coefficientWidth = (float)viewWidth/(float)SystemVariable.STANDARD_WIDTH;
		coefficientHeight = (float)viewHeight/(float)SystemVariable.STANDARD_HEIGHT;
		
//		Log.d("Log Info", "*****" + "分辨率:" + width +"*" + height + "*****");
		autoChangeSize(height);	//自动匹配不同分辨率下的大小
	}
	
	/*设置 bitmap
	 * 
	 * Type:Round,RoundedRectangle
	 */
    public Bitmap drawBitmapForType(Drawable drawable,String type) {
    	
        Bitmap bitmap = Bitmap.createBitmap(pointSize, pointSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, pointSize, pointSize);
        drawable.draw(canvas);
        // Draw Circle
        return BitmapUtil.toRoundBitmap(bitmap,type);
    }
    
    /**
     * 重绘界面
     */
	public void refressCanvas(){
		//触发onDraw函数
        invalidate();
	}
	
	//重新开始
	private void restart() {
		//刷新一下
		refressCanvas();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		// Init playerA
		{
			if(playerAList == null){
				// Player A
		        playerAList = GlobleTacticalClass.playerAList;
				GlobleTacticalClass.initPlayerAPosition(coefficientWidth, coefficientHeight);
				listBindBitmap(playerAList,ConstantVariable.PLAY_TYPE_A_,
						ConstantVariable.BITMAP_TYPE_ROUND);
			}
			drawTeam(playerAList,canvas);
		}
		
		// playerB
		if(GlobleTacticalClass.isDisplayCompetitor().equals("true")){
			if(playerBList == null){
				playerBList = GlobleTacticalClass.buildPlayerBList(coefficientWidth, coefficientHeight);
				listBindBitmap(playerBList,ConstantVariable.PLAY_TYPE_B_,ConstantVariable.BITMAP_TYPE_ROUND);
			}
			drawTeam(playerBList,canvas);
		}
    	
		// football
		{
			if(football == null){
				football = GlobleTacticalClass.buildFootball(coefficientWidth, coefficientHeight);
				// Bind
				bindBitmap(football, ConstantVariable.PLAY_TYPE_C_, ConstantVariable.BITMAP_TYPE_ROUND);
			}
			drawPlayer(canvas,football);
		}
		
		// Init PlayerList
		if(playerList == null){
			playerList = new ArrayList<Player>();
			playerList.addAll(playerAList);
			if(GlobleTacticalClass.isDisplayCompetitor().equals("true")){
				playerList.addAll(playerBList);
			}
			playerList.add(football);
		}
	}
	
	/**
	 * 画球队
	 */
	public void drawTeam(List<Player> list,Canvas canvas){
		for (int i = 0; i < list.size(); i++) {
			Player player = list.get(i);
			drawPlayer(canvas,player);
		}
	}
	
	/**
	 * 画球员
	 */
	public void drawPlayer(Canvas canvas,Player player){
		// 画选中球员
		canvas.drawBitmap(player.getPlayerBitmap(),
				player.getPointX(), player.getPointY(),paint);
		
		// 选中效果
		if(player.gotIsSelect()){
			FootballFieldViewSupport.selectedEffect(canvas,
					player.getPointX(),player.getPointY(),pointSize,moveAngle);
		}
		
		// 画移动箭头
		if(moveType == MOVE_TYPE_ARROW){
			Point currentPoint = new Point();
			currentPoint.setPointX(currentPointX);
			currentPoint.setPointY(currentPointY);
			
			FootballFieldViewSupport.drawPlayerPath(
					canvas,
					player,playerSelected,
					isSlide,
					pointSize,
					currentPoint,
					coefficientWidth,coefficientHeight,
					moveAngle);
		}
	}

	/**
	 * 
	 */
	public void listBindBitmap(List<Player> list,String playerRole,String bitmapForType){
        for(int i=0;i<list.size();i++){
        	Player player = list.get(i);
        	bindBitmap(player,playerRole,bitmapForType);
        }
   	}
	
	public void bindBitmap(Player player,String playerRole,String bitmapForType){
		Resources resources = getContext().getResources();
		// 得到动态的id
    	int id = context.getResources().getIdentifier(playerRole
    			+ player.getNumber(), "drawable" , context.getPackageName());  
    	player.setPlayerBitmap(drawBitmapForType(resources.getDrawable(id),bitmapForType));
	}
	
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//
//	}
	
	
	/**
	 * 
	 */
	public boolean onTouchView(MotionEvent event){
			
		float touchX  = event.getX();
		float touchY =  event.getY();
		
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			actionDown(touchX,touchY);
				
			/*
			 * API
			 * event.getAction() == MotionEvent.ACTION_DOWN
			 * return true:拦截长按
			 * return false:不拦截长按
			 */
			if(playerSelected!=null
					|| playerMoved != null){
				return true;
			}else{
				return false;
			}
		}
		
		if(playerSelected!=null
				|| playerMoved != null){
			if(event.getAction() == MotionEvent.ACTION_MOVE){
				actionMove(touchX,touchY);
			}else if(event.getAction() == MotionEvent.ACTION_UP){
				actionUp(touchX,touchY);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Down
	 */
	public void actionDown(float touchX,float touchY){
		//
		actionDownX = touchX;
		actionDownY = touchY;
		
		// DOWN:Init
		isSlide = false;
		
		// DOWN
		{
			playerSelected = gotSelectedPlayer(touchX,touchY);	
		
			if(playerMoved != null){
				Log.d("Log Info", "*****" + "playerMoved--x:" + playerMoved.getPointX() + "|touchX:" + touchX);
				Log.d("Log Info", "*****" + "playerMoved--y:" + playerMoved.getPointY() + "|touchY:" + touchY);
			}
			
			// playerMoved
			if(playerMoved !=null ){
				
				// 同一点
				if(playerMoved.getPointX() - touchX > pointSize/2
						||playerMoved.getPointX() - touchX < -pointSize/2 
						||playerMoved.getPointY() - touchY > pointSize/2
						||playerMoved.getPointY() - touchY < -pointSize/2){
					// 
					playerMoved = null;
				}else{
					playerSelected = playerOrigin;
					
					// 记录起点
					Point pointStart = new Point();
					pointStart.setPointX(playerMoved.getPointX() - pointSize/2);
					pointStart.setPointY(playerMoved.getPointY() - pointSize/2);
					
					FootballFieldViewSupport.recordMovingTrack(
							playerSelected,
							pointStart,
							pathAllTotalInt,
							FootballFieldViewSupport.KEY_POINT_TYPE_START);
						
				}
			}
		}
	}
	
	/**
	 * Move
	 */
	public void actionMove(float touchX,float touchY){
		
//		Toast toast = Toast.makeText(context,
//				"dx:" + (actionDownX - touchX) 
//				+ "dy:" + (actionDownY - touchY)
//				, Toast.LENGTH_LONG);
//		toast.setGravity(Gravity.CENTER, 0, 0);
//		toast.show();
		
		if(actionDownX == touchX
				&& actionDownY == touchY){
			return;
		}
		
		// Set
		isSlide = true;
				
		if(!playerSelected.gotIsSelect()){
			return;
		}
		
		currentMovedX = touchX;
		currentMovedY = touchY;
		
		/** Action **/
		// 移动判断
		boolean canMove = canMove(touchX,touchY);
		
		// 可以移动
		if(canMove){
			
			// 设置新位置坐标
			if(moveType == MOVE_TYPE_POSITION){
				sotNewPosition(playerSelected,touchX,touchY,pointSize/2);
			}
			
			// 增加选中旋转效果
			selectedWhirl();
			
			// 刷新
			refressCanvas();
		}
	}

	/**
	 * Up
	 */
	public void actionUp(float touchX,float touchY){
		
		if(!isSlide){
			playerSelected.sotIsSelect(!playerSelected.gotIsSelect());
			// 刷新
		}else{
			if(moveType == MOVE_TYPE_ARROW){
				
				if(playerSelected.gotIsSelect()){
					
					if(playerMoved == null){

						// 记录起点
						Point pointStart = new Point();
						pointStart.setPointX(playerSelected.getPointX());
						pointStart.setPointY(playerSelected.getPointY());
						
						FootballFieldViewSupport.recordMovingTrack(
								playerSelected,
								pointStart,
								pathAllTotalInt,
								FootballFieldViewSupport.KEY_POINT_TYPE_START);
					}
					
					// 记录终点
					Point pointEnd = new Point();
					pointEnd.setPointX(touchX - pointSize/2);
					pointEnd.setPointY(touchY - pointSize/2);
					
					FootballFieldViewSupport.recordMovingTrack(
							playerSelected,
							pointEnd,
							pathAllTotalInt,
							FootballFieldViewSupport.KEY_POINT_TYPE_END);
					
					// playerMoved
	    			if(playerMoved == null){
	    				playerMoved = new Player();
	    				playerMoved.sotIsSelect(true);
	    			}
	    			playerMoved.setPointX(Float.valueOf(currentMovedX));
	    			playerMoved.setPointY(Float.valueOf(currentMovedY));
				}
			}
		}
		
		// 
		refressCanvas();
		
		// 
		touchClear();
	}
	
	public void touchClear(){
		playerOrigin = playerSelected;
		
		moveAngle = 0;
		playerSelected = null;
	}
	
	
	/**
 	 *	演练战术
 	 */
 	public void displayTactics(){
 		// Got Move Track
 		FootballFieldViewSupport.sotMoveTrack(playerList);
 		
 		// Thread Start
 		new RefreshThread().start();
 	}
 	
 	/**
 	 * 
 	 */
	public class RefreshThread extends Thread {

		@Override
		public void run() {

	 			if(playerList!=null){
	 				for(int i=0;i<playerList.size();i++){
	 					Player player = playerList.get(i);
	 					// 
	 				}
	 				/*Toast.makeText(context, "演示已完成", Toast.LENGTH_SHORT );*/
	 					
 					/*
 					Looper.prepare();
 					new  AlertDialog.Builder(context)    
				    .setTitle("提示：" )
				    .setMessage("已完成演示!" )
				    .setPositiveButton("确定" ,null).show();  
 					Looper.loop(); 
 					*/
	 			}
		}
	}
	
	
	/*
	 * Register Listener
	 */
	private void registerListener(){
		
		// OnLongClickListener
		setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				
				// Long -> 显示
				TacticalActivity tacticalActivity = (TacticalActivity)context;
				ListView listview = tacticalActivity.getListView();
				listview.setVisibility(View.VISIBLE);
				
				TacticalMenuAdapter adapter = tacticalActivity.adapter;
				if(moveType == MOVE_TYPE_ARROW){
					List<String> list = new ArrayList<String>();
					
					list.add(ConstantVariable.MENU_LEFT_NEXT);
					list.add(ConstantVariable.MENU_LEFT_PAUSE);
					list.add(ConstantVariable.MENU_LEFT_STOP);
					list.add(ConstantVariable.MENU_LEFT_TACTICAL_DISPLAY);
					list.add(ConstantVariable.MENU_LEFT_SETTING);
					
					adapter = new TacticalMenuAdapter(
							tacticalActivity,list);
					listview.setAdapter(adapter);
			    	
					//  
					adapter.notifyDataSetChanged();
					adapter.notifyDataSetInvalidated();
				}
				
				return false;
			}
		});
		
		// OnTouchListener
		setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				currentPointX = event.getX();
				currentPointY = event.getY();
//				Log.d("Log Info", "*****" + "currentPointX:" + currentPointX);
//				Log.d("Log Info", "*****" + "currentPointY:" + currentPointY);
				
				// DOWN -> 隐藏
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					TacticalActivity tacticalActivity = (TacticalActivity)context;
					ListView listview = tacticalActivity.getListView();
					listview.setVisibility(View.GONE);
					listview.setFocusable(true);
					listview.setFocusableInTouchMode(true);
				}
					
				return onTouchView(event);
			}
		});
	}
	
	public Player gotSelectedPlayer(float touchX,float touchY){
		
		// playerAList
		for(int i=0;i<playerAList.size();i++){
			Player player = playerAList.get(i);
			// 通过位置，判断被选中的球员
			if(touchX >= player.getPointX() 
					&& touchX <= player.getPointX() + pointSize
					&& touchY >= player.getPointY() 
					&& touchY <= player.getPointY() + pointSize){
				return player;
			}
		}
		
		// playerBList
		List <TacticalSettingTO>list = GlobleTacticalClass.gotTacticalSetting();
		if(list.get(0).getDisplayCompetitor().equals("true")){
			for(int i=0;i<playerBList.size();i++){
				Player player = playerBList.get(i);
				if(touchX >= player.getPointX() 
						&& touchX <= player.getPointX() + pointSize
						&& touchY >= player.getPointY() 
						&& touchY <= player.getPointY() + pointSize){
					
					// 通过位置，判断被选中的球员
					return player;
				}
			}
		}
		
		// football
		if(touchX >= football.getPointX() 
				&& touchX <= football.getPointX() + pointSize
				&& touchY >= football.getPointY() 
				&& touchY <= football.getPointY() + pointSize){
			return football;
		}
		return null;
	}
	
	
	public void sotNewPosition(Player player,float touchX,float touchY,int pointSize){
		player.setPointX((int)touchX - pointSize);
		player.setPointY((int)touchY - pointSize);
	}
	
	public boolean canMove(float touchX,float touchY){
		boolean canMove = true;
		
		// 1.移动条件1：边界
		if(!(touchX - pointSize/2 > 0
				&& touchX + pointSize/2 < viewWidth)){
			canMove = false;
		}
		if(!(touchY - pointSize/2 > 0
				&& touchY + pointSize/2 < viewHeight)){
			canMove = false;
		}
		
		// 2.移动条件2：覆盖
		if(canMove){
			if(playerList==null){
				playerList.addAll(playerAList);
				
				if(GlobleTacticalClass.isDisplayCompetitor().equals("true")){
					playerList.addAll(playerBList);
				}
				playerList.add(football);
			}	
			// 得到是否覆盖
			canMove = !FootballFieldViewSupport.isOver(
					playerList,playerSelected,touchX,touchY,pointSize);
		}
		return canMove;
	}
	
	/**
	 * 选中旋转效果
	 */
	public void selectedWhirl(){
		// 
	    currentActionTime = System.currentTimeMillis();
	    /*
	     * 转动效果
	     *  moveAngle 增加角度
	     *  gotTimedifferenceBySecond 并控制转速
	     */
    	if(lastActionTime == null || DateUtil.gotTimedifferenceBySecond(
				currentActionTime,lastActionTime) > 0.1){
    		moveAngle += 45;
    		lastActionTime = currentActionTime;
    	}
	}
	
}
