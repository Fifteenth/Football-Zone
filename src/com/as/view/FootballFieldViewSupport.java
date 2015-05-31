package com.as.view;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.cf.base.util.CoordinateUtil;
import com.cf.club.Player;
import com.cf.fix.GlobleTacticalClass;
import com.cf.to.Point;
import com.cf.to.TacticalFileTO;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class FootballFieldViewSupport {
	
	public final static int TYPE_POINT_START = 0;
	public final static int TYPE_POINT_END = 1;
	
	public final static String KEY_POINT_TYPE_START = "start";
	public final static String KEY_POINT_TYPE_END = "end";
	
	public final static String KEY_POINT_TYPE_START_X = "startX";
	public final static String KEY_POINT_TYPE_START_Y = "startY";
	public final static String KEY_POINT_TYPE_END_X = "endX";
	public final static String KEY_POINT_TYPE_END_Y = "endY";
	public final static String KEY_STEP_NUMBER = "stepNumber";

	/**
	 * 选中效果
	 */
	public static void selectedEffect(Canvas canvas,
			float pointX,float pointY,int pointSize,int moveAngle){
		Paint paint = new Paint(Paint.DITHER_FLAG);
		paint.setStyle(Paint.Style.STROKE); //绘制空心圆   
		paint.setAntiAlias(true); //消除锯齿
		
		// Color
		paint.setColor(Color.WHITE);
		
		// 球员选中效果
		RectF rect2 = new RectF(pointX - 4 , pointY - 4, pointX + pointSize + 4, pointY + pointSize + 4);
		
		paint.setARGB(200, 127, 255, 212);
		int ringWidth = 2;
		paint.setStrokeWidth(ringWidth);
		 
		//绘制不透明部分
		/*
		  	上面的代码当startAngle = 0时，绘制的是一个静态的透明度交替的圆弧。
		  	接着要让它转起来。增加代码：       
		     startAngle+=10;
		         if(startAngle == 180)
		             startAngle = 0;	    

		 */
		canvas.drawArc(rect2, 180 + moveAngle, 90, false, paint);
		canvas.drawArc(rect2, 0 + moveAngle, 90, false, paint);
		
		//绘制透明部分
		paint.setARGB(30, 127, 255, 212);
		canvas.drawArc(rect2, 90 + moveAngle, 90, false, paint);
		canvas.drawArc(rect2, 270 + moveAngle, 90, false, paint);
	}

	/**
	 * 记录移动轨迹
	 * 
	 * type 
	 * 	1.起点
	 * 	2.终点
	 */
	public static void recordMovingTrack(Player playerSelected,Point point,
			int pathAllTotal,String pointType){
		// 
		String keyPathAllIndex = String.valueOf(pathAllTotal - 1);
		
		JSONObject pathAllStepAllJSon = playerSelected.getPathAllStepAllJSon();
		JSONObject pathOneStepTotalJSon = playerSelected.getPathXStepTotalJSon();
		
		JSONObject pathOneJSon = null;
		JSONObject pathOneStepXJSon;
		Integer pathXStepTotalInt;
		try {
			
			if(pathAllStepAllJSon == null){
				pathOneJSon = new JSONObject();
				pathOneStepTotalJSon = new JSONObject();
				pathAllStepAllJSon = new JSONObject();
				pathXStepTotalInt = 0;
			}else{
				pathXStepTotalInt = pathOneStepTotalJSon.getInt(keyPathAllIndex);
				pathOneJSon = pathAllStepAllJSon.getJSONObject(keyPathAllIndex);
			}
			
			//
			if(pointType.equals(KEY_POINT_TYPE_START)){
					
				pathXStepTotalInt ++;
				pathOneStepXJSon = new JSONObject();
				pathOneStepXJSon.put(KEY_POINT_TYPE_START_X, point.getPointX());
				pathOneStepXJSon.put(KEY_POINT_TYPE_START_Y, point.getPointY());
				
			}else{
				int pathXStepIndex = pathXStepTotalInt - 1;
				pathOneStepXJSon = pathOneJSon.getJSONObject(String.valueOf(pathXStepIndex));
				// End
				pathOneStepXJSon.put(KEY_POINT_TYPE_END_X, point.getPointX());
				pathOneStepXJSon.put(KEY_POINT_TYPE_END_Y, point.getPointY());
			}
			
			// Put pathXStepTotalJSon
			pathOneStepTotalJSon.put(keyPathAllIndex, pathXStepTotalInt);
			
			// Put pathOneJSon
			String keyPathXStepIndex = String.valueOf(pathXStepTotalInt - 1);
			pathOneJSon.put(keyPathXStepIndex, pathOneStepXJSon);
			// Put pathAllStepAllJSon
			pathAllStepAllJSon.put(keyPathAllIndex, pathOneJSon);
			
			// Set
			playerSelected.setPathXStepTotalJSon(pathOneStepTotalJSon);
			playerSelected.setPathAllStepAllJSon(pathAllStepAllJSon);
			playerSelected.setPathAllTotal(pathAllTotal);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 判断是否重叠
	 */
	public static boolean isOver(List<Player> playerList,Player playerSelected,
			float touchX,float touchY,int pointSize){
		for(int i=0;i<playerList.size();i++){
			Player player = (Player)playerList.get(i);
			if(player!=playerSelected){
				float playerX = player.getPointX() + pointSize/2;
				float playerY = player.getPointY() + pointSize/2;
				float distanceX = playerX - touchX;
				float distanceY = playerY - touchY;
				//  勾股定理
				if(distanceX * distanceX + distanceY * distanceY < pointSize * pointSize){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 */
	public static void drawPath(Canvas canvas,	
			float coefficientWidth,float coefficientHeight,
			float startX,float startY,float endX,float endY){
		float interceptX = startX + (endX - startX) * (float)3.0 / (float)4.0;
        float interceptY = startY + (endY - startY) * (float)3.0 / (float)4.0;
        
        float dx = interceptX - startX;
        float dy = interceptY - startY;
        
        double angle = Math.atan(dy/dx);
        
        
        Path path1 = new Path();
        Path path2 = new Path();
        
        float interceptLenth1 = 9 * coefficientWidth;
        float interceptLenth2 = 15 * coefficientWidth;
        
        // 
        path1.moveTo(startX, startY);
        path1.lineTo(
        		(float) (interceptX - interceptLenth1 * Math.sin(angle)),
        		(float) (interceptY + interceptLenth1 * Math.cos(angle)));
        path1.lineTo(
        		(float) (interceptX + interceptLenth1 * Math.sin(angle)),
        		(float) (interceptY - interceptLenth1 * Math.cos(angle)));
        path1.close(); 
        
        // 
        path2.moveTo(endX, endY);
        path2.lineTo(
        		(float) (interceptX - interceptLenth2 * Math.sin(angle)),
        		(float) (interceptY + interceptLenth2 * Math.cos(angle)));
        path2.lineTo(
        		(float) (interceptX + interceptLenth2 * Math.sin(angle)),
        		(float) (interceptY - interceptLenth2 * Math.cos(angle)));
        path2.close(); 
        
        // paint
        Paint paint = new Paint(Paint.DITHER_FLAG);
		paint.setARGB(255, 153, 29, 29);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        
        canvas.drawPath(path1, paint); 
        canvas.drawPath(path2, paint); 
	}
	
	/**
	 * 画轨迹
	 */
	public static void drawPlayerPath(
			Canvas canvas,
			Player player,Player playerSelected,
			boolean isSlide,
			int pointSize,
			Point currentPoint,
			float coefficientWidth,float coefficientHeight,
			int moveAngle
			){
		
		float startX;
    	float startY;
    	float endX;
    	float endY;
		
        JSONObject moveStepssJSon = player.getPathAllStepAllJSon();
        
        /*
         * if 
         * 	画当前箭头
         * else
         * 	从记录中取出数据画箭头
         */
        if(moveStepssJSon == null){
        	if(isSlide && player == playerSelected){
        		startX = player.getPointX() + pointSize/2;
            	startY = player.getPointY() + pointSize/2;
            	endX = currentPoint.getPointX();
        		endY = currentPoint.getPointY();
        		
            	FootballFieldViewSupport.drawPath(canvas,
            			coefficientHeight,coefficientHeight,
            			Float.valueOf(startX),Float.valueOf(startY),
            			Float.valueOf(endX),Float.valueOf(endY));
        	}
        }else{
        	JSONObject jSonMoveStepsN;
            try {
    			jSonMoveStepsN = moveStepssJSon.getJSONObject(
    					String.valueOf(player.getPathAllTotal() - 1));
    			
    			for(int i=0;i<player.getPathAllTotal();i++){
    				JSONObject pathXStepTotalJSon = player.getPathXStepTotalJSon();
    				
    				String pathXStep = String.valueOf(i);
    				if(pathXStepTotalJSon !=null 
    						&& pathXStepTotalJSon.has(pathXStep)){
    					int stepNum = player.getPathXStepTotalJSon().getInt(pathXStep);
                        for(int j=0;j<stepNum;j++){
                        	JSONObject moveStepJSon = jSonMoveStepsN.getJSONObject(
                					String.valueOf(j));
                        	
                        	// 
                        	startX = Float.valueOf(String.valueOf(
                        			moveStepJSon.get(KEY_POINT_TYPE_START_X))) + pointSize/2;
                        	startY = Float.valueOf(String.valueOf(
                        			moveStepJSon.get(KEY_POINT_TYPE_START_Y))) + pointSize/2;
                        	if(j == stepNum - 1 
                        			&& isSlide
                        			&& player == playerSelected){
                        		// 
                        		endX = currentPoint.getPointX();
                        		endY = currentPoint.getPointY();
                        	}else{
                            	endX = Float.valueOf(String.valueOf(
                            			moveStepJSon.get(KEY_POINT_TYPE_END_X))) + pointSize/2;
                            	endY = Float.valueOf(String.valueOf(
                            			moveStepJSon.get(KEY_POINT_TYPE_END_Y))) + pointSize/2;
                        	}
                        	
                        	FootballFieldViewSupport.drawPath(canvas,
                        			coefficientHeight,coefficientHeight,
                        			startX,startY,endX,endY);
                        	
                        	// 足球不画箭头移动后选中效果
                        	if(!player.getNumber().equals("00")){
                        		FootballFieldViewSupport.selectedEffect(canvas,
                        				endX - pointSize/2,endY - pointSize/2,
                            			pointSize,moveAngle);
                        	}
                        }
    				}
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    			return;
    		}
        }
	}
	
	/**
	 *  处理读档数据
	 */
	public static void dealWithFileData(List<Player> playerAList,
			List<Player> playerBList,Player football){
		List <TacticalFileTO> tacticalFileTOList = GlobleTacticalClass.tacticalFileTOList;
		for(int i=0;i<tacticalFileTOList.size();i++){
			TacticalFileTO tacticalFileTO = tacticalFileTOList.get(i);
			String role = tacticalFileTO.getRole();
			if(role.equals("a")){
				sotMoveSteps(tacticalFileTO,playerAList);
			}else if(role.equals("b")){
				sotMoveSteps(tacticalFileTO,playerBList);
			}else{
				sotMoveSteps(tacticalFileTO,football);
			}
		}
	}
	
	
	public static void sotMoveSteps(TacticalFileTO tacticalFileTO,List<Player> list){
		for(int i=0;i<list.size();i++){
			Player player = list.get(i);
			// Set JSON
			sotMoveSteps(tacticalFileTO,player);
			
			// 
//			float distance = ComputingDistance(player);
//			System.out.println(distance);
		}
	}
	
	public static void sotMoveSteps(TacticalFileTO tacticalFileTO,Player player){
		if(tacticalFileTO.getNumber().equals(player.getNumber())){
			String pathAllStepAllJSon = tacticalFileTO.getPathAllStepAllJSon();
			try {
				if(pathAllStepAllJSon != null){
					player.setPathAllStepAllJSon(new JSONObject(pathAllStepAllJSon));
					player.setPathAllTotal(Integer.valueOf(tacticalFileTO.getPathAllTotal()));
					player.setPathXStepTotalJSon(new JSONObject(tacticalFileTO.getPathXStepTotalJSon()));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * All Path All Step Total Distance
	 * 
	 * JSon Structure:
	 * 	{
	 * 		"1":{
	 * 				"1":"{"startX":"1","startY":"1","endX":"1","endY":"1"}",
	 * 				"2","{"startX":"1","startY":"1","endX":"1","endY":"1"}"
	 * 			},	
	 * 		"2":{
	 * 				"1":"{"startX":"1","startY":"1","endX":"1","endY":"1"}",
	 * 				"2","{"startX":"1","startY":"1","endX":"1","endY":"1"}",
	 * 				"3","{"startX":"1","startY":"1","endX":"1","endY":"1"}"
	 * 			}
	 * 	}
	 */
	public static JSONObject ComputingPathAllStepAllDistance(Player player){
		
		JSONObject allPathAllStepDistanceJSon = new JSONObject();
		float distanceTotal = 0;
		try {
			JSONObject pathAllStepAllJSon = player.getPathAllStepAllJSon();
			JSONObject pathXStepTotalJSon = player.getPathXStepTotalJSon();
			
			// 一共几步:player.getPathAllTotal()
			int pathAllTotal = player.getPathAllTotal();
			
			// TODO
			if(pathAllTotal != 0){
				System.out.println();
			}
			
			for(int i=0;i<pathAllTotal;i++){
				String jsonKey = String.valueOf(i);
				JSONObject distanceJSon = new JSONObject();
				if(pathAllStepAllJSon.has(jsonKey)){
					JSONObject jSonPathOneStepss = pathAllStepAllJSon.getJSONObject(jsonKey);
					int pathXStepTotalInt = pathXStepTotalJSon.getInt(jsonKey);
					
					// One Path
					distanceJSon = ComputingPathXStepAllStepDistance(jSonPathOneStepss,
							pathXStepTotalInt);
				
					// 
					distanceTotal += Float.valueOf(distanceJSon.get(String.valueOf(pathXStepTotalInt)).toString());
				}
				
				if(distanceTotal != 0){
					allPathAllStepDistanceJSon.put(String.valueOf(i), distanceJSon);
				}
			}
			
			// Total
			allPathAllStepDistanceJSon.put(String.valueOf(pathAllTotal), distanceTotal);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return allPathAllStepDistanceJSon;
	}
	
	/**
	 * One Path All Step Total Distance
	 */
	public static JSONObject ComputingPathXStepAllStepDistance(JSONObject jSonPathOneStepss,
			int pathXStepTotalInt)throws NumberFormatException, JSONException{
		float distanceTotal = 0;
		JSONObject distanceJSonObject = new JSONObject();
		// 每一步一个几个移动箭头:pathXStepTotalJSon.getInt(jsonKey)
		for(int i=0;i<pathXStepTotalInt;i++){
			JSONObject jSonPathOnePathStepOne = jSonPathOneStepss.getJSONObject(
					String.valueOf(i));
			// Get Path Points
			Point[] pointss = gotPathPointss(jSonPathOnePathStepOne);
			
			float distance = CoordinateUtil.ComputingDistance(
					pointss[TYPE_POINT_START], pointss[TYPE_POINT_END]);

			// Put
			distanceJSonObject.put(String.valueOf(i), distance);
			
			distanceTotal += distance;
		}
		
		// Put
		distanceJSonObject.put(String.valueOf(pathXStepTotalInt), distanceTotal);
		return distanceJSonObject;
	}
	
	public static Point[] gotPathPointss(JSONObject jSonPathOnePathStepOne) 
			throws NumberFormatException, JSONException{
		// Path 
    	float startX = Float.valueOf(
    			String.valueOf(jSonPathOnePathStepOne.get(KEY_POINT_TYPE_START_X)));
    	float startY = Float.valueOf(
    			String.valueOf(jSonPathOnePathStepOne.get(KEY_POINT_TYPE_START_Y)));
    	float endX = Float.valueOf(
    			String.valueOf(jSonPathOnePathStepOne.get(KEY_POINT_TYPE_END_X)));
    	float endY = Float.valueOf(
    			String.valueOf(jSonPathOnePathStepOne.get(KEY_POINT_TYPE_END_Y)));
    	
    	// 
    	Point startPoint = new Point();
    	Point endPoint = new Point();
    	
    	startPoint.setPointX(startX);
    	startPoint.setPointY(startY);
    	endPoint.setPointX(endX);
    	endPoint.setPointY(endY);
    	
    	Point[] pathPoint = {startPoint,endPoint};
    	return pathPoint;
	}
	
	/**
	 * 
	 */
	public static void sotMoveTrack(List <Player> playerList){
		
		// Total steps = 20
		final int All_PATH_ALL_STEPS_TOTAL = 20;
		
		// One Player
		for(int playerIndex=0;playerIndex<playerList.size();playerIndex++){
			Player player = playerList.get(playerIndex);
			
			// Path All Total
			int pathAllTotal = player.getPathAllTotal();
			
			if(pathAllTotal!=0){
				System.out.println();	
			}
						
			JSONObject allPathAllStepDistanceJSon = FootballFieldViewSupport.
					ComputingPathAllStepAllDistance(player);
			
			try {
				for(int i=0;i<pathAllTotal;i++){
					// One Path
					JSONObject onePathAllStepDistanceJSon = allPathAllStepDistanceJSon.
							getJSONObject(String.valueOf(i));
					
					// Path One Step Total
					int pathOneStepTotal = player.getPathXStepTotalJSon().getInt(String.valueOf(i));
					
					// Path One Total Distance
					float onePathDistance = Float.valueOf(onePathAllStepDistanceJSon.get(String.valueOf(pathOneStepTotal)).toString());
					for(int j=0;j<pathOneStepTotal;j++){
						System.out.println();
						
						// Path One Step One 
						float onePathOneStepDistance = Float.valueOf(onePathAllStepDistanceJSon.
								get(String.valueOf(j)).toString());
					}
				}
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
		}
	}
}
