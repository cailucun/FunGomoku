package com.cailucun.gomoku;

import android.app.Activity;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import android.media.MediaPlayer;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.cailucun.gomoku.R;

import java.util.*;

public class Risk extends SurfaceView implements SurfaceHolder.Callback, Runnable{
//Draw
	Paint mPaint;
	SurfaceHolder mSurfaceHolder = null;
	boolean mbLoop = false;
    public static Paint sPaint = null;
    public static Canvas sCanvas = null;
	float width=getWidth();
	float height=getHeight();
	float girdsize;
	
//Game Control
	public int[][] map = null;
	static Risk sInstance = null;
	int turn=1;
	int selX;
	int selY;
	int gameOver=0;
	int capture;
	boolean startover=false;
	Thread aThread;
	Context context;
	private static int ccount; 
    private static MediaPlayer mp=null;
    int frameCount=0;
    int count=0;
	
	
//Picture sources
	Bitmap backpic=null;
	Bitmap pBlack = null;
    Bitmap pWhite = null;

    	
	public Risk(Activity activity,float screenWidth,float screenHeight) {
		super(activity);
		context=activity;
		mbLoop = true;
		width = screenWidth;
		height = screenHeight;
		map = new int[10][10];
		mSurfaceHolder = this.getHolder();
		mSurfaceHolder.addCallback(this);
		setFocusable(true);
		setFocusableInTouchMode(true);
	}
	
    public static void init(Activity mActivity, int screenWidth,
    	    int screenHeight) {
    	sInstance = new Risk(mActivity, screenWidth, screenHeight);
        }
    
    public static Risk getInstance() {
	return sInstance;
    }
	
    public void run() {
    	

    	while (mbLoop) {
    	    try {
    	    	synchronized (mSurfaceHolder) {
    	    		sCanvas = mSurfaceHolder.lockCanvas();
    	    		draw();
    	    		mSurfaceHolder.unlockCanvasAndPost(sCanvas);
    	    	    }
    	    Thread.sleep(33);
    	    } catch (Exception e) {

    	    }


    	   }
    	}
    protected void set(String picture){
    	if(picture.equalsIgnoreCase("Wood"))
    		backpic = BitmapFactory.decodeResource(getResources(),
    				R.drawable.wood);
    	else if(picture.equalsIgnoreCase("Stone"))
    		backpic = BitmapFactory.decodeResource(getResources(),
    				R.drawable.stone);
    	else 
    		backpic = BitmapFactory.decodeResource(getResources(),
    				R.drawable.grass);
    	backpic=Bitmap.createScaledBitmap(backpic, (int)width, (int)height, true);
    }
    public void play(Context context,int resource){
    	stop(context);
    	//if(Prefs.getMusic(context)){
    	mp=MediaPlayer.create(context,resource);
    	mp.setLooping(true);
    	mp.start();
    	//}
    }
    public void stop(Context context){
    	if(mp!=null){
    		mp.stop();
    		mp.release();
    		mp=null;
    	}
    }
	protected void draw() {
		
		//
		
	    if(frameCount++<2)
	    	mPaint = new Paint();
	    	sCanvas.drawBitmap(backpic, 0, 0,mPaint);
			mPaint.setColor(getResources().getColor(R.color.black));
			mPaint.setStyle(Style.FILL);
			mPaint.setTextSize(girdsize * 0.75f);
			mPaint.setTextScaleX(width / height);
			mPaint.setTextAlign(Paint.Align.CENTER);

		if(startover==false)
		{
			for (int i = 0; i < 10; i++) {
				sCanvas.drawLine(girdsize, i * girdsize+height/4, 10*girdsize, i*girdsize+height/4, mPaint);
				sCanvas.drawLine(girdsize*(i+1), height/4, girdsize*(i+1), 9*girdsize+height/4, mPaint);
		    }
		}
		// Draw the background...
		width=getWidth();
		height=getHeight();
		girdsize=getWidth()/11;
		String mode="Risk"; 
		
		//backpic = BitmapFactory.decodeResource(getResources(),
			//	R.drawable.wood);
		pBlack = BitmapFactory.decodeResource(getResources(),
				R.drawable.ai);
		pWhite = BitmapFactory.decodeResource(getResources(),
				R.drawable.human);
		
		
	    
		Paint background = new Paint();
		background.setColor(getResources().getColor(R.color.white));
		//sCanvas.drawRect(0, 0, getWidth(), getHeight(), background);
		Paint light = new Paint();
		light.setColor(getResources().getColor(R.color.black));
		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setColor(getResources().getColor(R.color.black));
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(girdsize * 0.75f);
		foreground.setTextScaleX(width / height);
		foreground.setTextAlign(Paint.Align.CENTER);

		if(turn==1)
			sCanvas.drawBitmap(pBlack,(float) (5.5*girdsize),(float) (1.5*girdsize),foreground);
		else
			sCanvas.drawBitmap(pWhite,(float) (5.5*girdsize),(float) (1.5*girdsize),foreground);

		sCanvas.drawText("Mode:",4*girdsize,girdsize,foreground);
		sCanvas.drawText(mode,6*girdsize,girdsize,foreground);
		sCanvas.drawText("Turn:",4*girdsize,2*girdsize,foreground);
		if(startover==true)
		{
			Paint red = new Paint(Paint.ANTI_ALIAS_FLAG);
			red.setColor(getResources().getColor(R.color.red));
			red.setStyle(Style.FILL);
			red.setTextSize(girdsize * 0.75f);
			red.setTextScaleX(width / height);
			red.setTextAlign(Paint.Align.CENTER);
			sCanvas.drawText("Restart",8*girdsize,(float) (3.5*girdsize),red);
		}
		
		if(capture==1)
			sCanvas.drawText("Successfully captured!",3*girdsize,4*girdsize,foreground);
		else if(capture==2)
			sCanvas.drawText("Capture Failed!",3*girdsize,4*girdsize,foreground);
			
		
		//draw all pieces
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<10;j++)
			{
				if(map[i][j]==1)
					DrawPiece(i,j, 1);
				else if(map[i][j]==2)
					DrawPiece(i,j, 2);
			}
				
		}
		sCanvas.drawText("Number of pieces: "+String.valueOf(count),4*girdsize,3*girdsize,foreground);
		if(gameOver>0){
		if(gameOver==1){
			sCanvas.drawText("Black Player won",2*girdsize,4*girdsize,foreground);
			startover=true;
		}
			
		else if(gameOver==2)
		{
			sCanvas.drawText("White Player won",2*girdsize,4*girdsize,foreground);
			startover=true;
		}
		else if(gameOver==3)
		{
			sCanvas.drawText("It's a Tie",2*girdsize,4*girdsize,foreground);
			startover=true;
		}
			

		}
		
		
}
	protected void restart(){
		gameOver=0;
		frameCount=0;
		startover=false;
		
		for(int i=0;i<10;i++)
			for(int j=0;j<10;j++)
				map[i][j]=0;
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setColor(getResources().getColor(R.color.black));
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(girdsize * 0.75f);
		foreground.setTextScaleX(width / height);
		foreground.setTextAlign(Paint.Align.CENTER);
		if (event.getAction() != MotionEvent.ACTION_DOWN)
			return super.onTouchEvent(event);
		if(startover==true&&event.getX()>7*girdsize&&event.getX()<9*girdsize&&event.getY()>3*girdsize&&event.getY()<4*girdsize)
		{
				restart();
		}
		if(startover==false&&event.getX()>girdsize/2&&event.getX()<10.5*girdsize&&event.getY()>(height/4-girdsize/2)&&event.getY()<height/4+9.5*girdsize)
		{
			capture=0;
			selX=Math.round((event.getX()-girdsize)/girdsize);
			selY=Math.round((event.getY()-height/4)/girdsize);
			
			if(map[selY][selX]==0)
				{	
					map[selY][selX]=turn;
					if(turn==2)
						ccount++;
					else if (turn==1)
						count++;
						
				}
			else if(map[selY][selX]==1&&turn==2)
			{
				Random rdm = new Random(System.currentTimeMillis());
				int intRd = Math.abs(rdm.nextInt())%100;
				if(intRd>49){
					map[selY][selX]=2;
					capture=1;
				}
				else
					capture=2;
				
			}
			
				
			else if(map[selY][selX]==2&&turn==1)
			{
				Random rdm = new Random(System.currentTimeMillis());
				int intRd = Math.abs(rdm.nextInt())%100;
				if(intRd>49){
					map[selY][selX]=1;
					capture=1;
				}
				else
					capture=2;
			}
				
			if(judge(map,turn))
				gameOver=turn;
			//getRect();
			if(ccount==50)
				gameOver=3;
			if(turn==1)
				turn=2;
			else if(turn==2)
				turn=1;

		}
		return true;
	}

	 static boolean judge(int a[][],int color){
	      int i,j,count;
	      for(i=0;i<10;i++){    
	    	  count=0;
	          for(j=0;j<10;j++)
	              if(a[i][j]==color){   
	            	  count++;
	                 if (count==5)
	                   return true;}
	             else   count=0;
	      }          
	      for(j=0;j<10;j++){    
	    	  count=0;
	           for(i=0;i<10;i++)
	               if(a[i][j]==color)
	               {count++;
	               if(count==5) 
	                   return true;}
	               else count=0;
	          }
	      for(j=4;j<10;j++){    
	    	  count=0;  int m=j;
	          for(i=0;i<=j;i++){
	             if(a[i][m--]==color){
	            	 count++;
	                  if(count==5)
	                  return true;}
	                  else count=0;}
	          }          
	      for(j=5;j>=0;j--){    
	    	  count=0;  int m=j;
	          for(i=0;i<=9-j;i++){
	            
	              if(a[i][m++]==color){
	            	  count++;
	                  if(count==5)
	                  return true;}
	                  else count=0;}
	          }           
	      for(i=5;i>=0;i--){    
	    	  count=0;    int n=i;
	          for(j=0;j<10-i;j++){
	              if(a[n++][j]==color){
	            	  count++;
	                  if(count==5)
	                  return true;}
	                  else count=0;}
	          }          
	       for(j=5;j>=0;j--){    
	    	   count=0; int m=j; 
	          for(i=9;i>=j;i--){
	            
	             if(a[i][m++]==color){
	            	 count++;
	                  if(count==5)
	                  return true;}
	                  else count=0;}
	          }                 
	       return false;  
	   }

	private void DrawPiece(float x, float y, int color)
	{
		Bitmap pBlack = BitmapFactory.decodeResource(getResources(),
				R.drawable.ai);
		Bitmap pWhite = BitmapFactory.decodeResource(getResources(),
				R.drawable.human);
		float pieceWidth;
		float pieceHeight;
		
		

		pieceWidth=pBlack.getWidth();
		pieceHeight=pBlack.getHeight();
		Paint paint = new Paint();
		if(color==1)
		{
			sCanvas.drawBitmap(pBlack,(1+y)*girdsize-pieceWidth/2,height/4+x*girdsize-pieceHeight/2,paint);
		}
		else
		{
			sCanvas.drawBitmap(pWhite,(1+y)*girdsize-pieceWidth/2,height/4+x*girdsize-pieceHeight/2,paint);
		}
	}
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
	// TODO Auto-generated method stub

    }
    public void surfaceCreated(SurfaceHolder arg0) {
    	aThread=new Thread(this);
    	aThread.start();

    }
    public void surfaceDestroyed(SurfaceHolder arg0) {
	mbLoop = false;
	stop(context);
	Thread dummy=aThread;
	
	aThread=null;
	dummy.interrupt();
	
    }
    
   
}
