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


public class Single extends SurfaceView implements SurfaceHolder.Callback, Runnable{
	
//Draw part
	public int[][] map = new int[10][10];

	static Single sInstance = null;
	SurfaceHolder mSurfaceHolder = null;
	boolean mbLoop = false;
    public static Paint sPaint = null;
    public static Canvas sCanvas = null;
	float width=getWidth();
	float height=getHeight();
	float girdsize;
	
//Game Control
    Context context;
    Thread aThread;
	int turn=1;
	int selX;
	int selY;
	int gameOver=0;
	int order=1;
	boolean once=true;
	boolean startover=false;
	
//AI part
	 private boolean ptable[][][]=new boolean [10][10][192],ctable[][][]=new boolean [10][10][192]; // 对于每一种获胜情况（5个子），开始五子子都是true(其它都是false)
     private int pgrades[][]=new int [10][10],cgrades[][]=new int [10][10]; // 每一点的权值（每种获胜组合相加得到）
     private int win[][]=new int [2][192]; //记录玩家与计算机在各种获胜组合中填入了多少棋子；
     private int cgrade=0,pgrade=0;
     private int pcount,ccount;   //记录计算机与玩家各下了多少棋子
	private boolean start=true,over=false,pwin,cwin,tie;
     private int i,j,k,n,m,count=0,a=50,b=50;
     private int mat,nat,mde,nde;
	 int countpieces;
     int frameCount=0;
//picture source
	Bitmap backpic=null;
	Bitmap pBlack = null;
    Bitmap pWhite = null;
    Paint mPaint;


    private static MediaPlayer mp=null;
	
	public Single(Activity activity,float screenWidth,float screenHeight) {
		super(activity);
		context=activity;
		mbLoop = true;
		width = screenWidth;
		height = screenHeight;
		inimaps();
		
		mSurfaceHolder = this.getHolder();
		mSurfaceHolder.addCallback(this);
		
		setFocusable(true);
		setFocusableInTouchMode(true);
	}
	
    public static void init(Activity mActivity, int screenWidth,
    	    int screenHeight) {
    	sInstance = new Single(mActivity, screenWidth, screenHeight);
        }
    
    public static Single getInstance() {
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
    protected void set(String picture, String order){
    	if(picture.equalsIgnoreCase("Wood"))
    		backpic = BitmapFactory.decodeResource(getResources(),
    				R.drawable.wood);
    	else if(picture.equalsIgnoreCase("Stone"))
    		backpic = BitmapFactory.decodeResource(getResources(),
    				R.drawable.stone);
    	else if(picture.equalsIgnoreCase("Grass"))
    		backpic = BitmapFactory.decodeResource(getResources(),
    				R.drawable.grass);
    	backpic=Bitmap.createScaledBitmap(backpic, (int)width, (int)height, true);
    	
    	if(order.equalsIgnoreCase("Player first"))
    	{
    		this.order=1;
    		turn=1;
    	}
    	else if(order.equalsIgnoreCase("Computer first"))
    	{
    		this.order=2;
    		turn=2;
    	}
    }
    public void play(Context context,int resource){
    	stop(context);
    	
    	mp=MediaPlayer.create(context,resource);
    	mp.setLooping(true);
    	mp.start();

    }
    public void stop(Context context){
    	if(mp!=null){
    		mp.stop();
    		mp.release();
    		mp=null;
    	}
    }
    protected void inimaps(){
    	for(i=0;i<10;i++)         
    		for(j=0;j<6;j++)
    		{
    			for(k=0;k<5;k++)
    			{
    				ptable[j+k][i][count] = true;
    				ctable[j+k][i][count] = true;
    			}
    			count++;
    		}
    	for(i=0;i<10;i++)       
    		for(j=0;j<6;j++)
    		{
    			for(k=0;k<5;k++)
    			{
    				ptable[i][j+k][count] = true;
    				ctable[i][j+k][count] = true;
    			}
    			count++;
    		}
    	for(i=0;i<6;i++)          
    		for(j=0;j<6;j++)
    		{
    			for(k=0;k<5;k++)
    			{
    				ptable[j+k][i+k][count] = true;
    				ctable[j+k][i+k][count] = true;
    			}
    			count++;
    		}
    	for(i=0;i<6;i++)          
    		for(j=9;j>=4;j--)
    		{
    			for(k=0;k<5;k++)
    			{
    				ptable[j-k][i+k][count] = true;
    				ctable[j-k][i+k][count] = true;
    			}
    			count++;
    		}
    }
	protected void draw() {
		
		if(frameCount++<2)
			mPaint=new Paint();
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
		String mode="Regular"; 
		
		pBlack = BitmapFactory.decodeResource(getResources(),
				R.drawable.ai);
		pWhite = BitmapFactory.decodeResource(getResources(),
				R.drawable.human);
		
	    
		Paint background = new Paint();
		background.setColor(getResources().getColor(R.color.white));
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
		
		//draw all pieces
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<10;j++)
			{
				if(map[i][j]==1)
					DrawPiece(j,i, 1);
				else if(map[i][j]==2)
					DrawPiece(j,i, 2);
			}
				
		}
		sCanvas.drawText("Number of pieces: "+String.valueOf(countpieces),4*girdsize,3*girdsize,foreground);
		if(gameOver>0){
		if(gameOver==1)
		{
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
		if(order==2&&once==true)
		{
			computerdo();
			once=false;
			turn=1;
		}

		
}
	protected void restart(){
		turn=order;
		once=true;
		count=0;
		ccount=0;
		pcount=0;
		gameOver=0;
		frameCount=0;
		start=true;
		startover=false;
		for(int i=0;i<10;i++)
			for(int j=0;j<10;j++)
				map[i][j]=0;		
	
		
		for(i=0;i<=1;i++)
		    for(j=0;j<192;j++)
		    	win[i][j]=0;
		

		for(i=0;i<10;i++)
		    for(j=0;j<10;j++)
		    {
			pgrades[i][j]=0;
			cgrades[i][j]=0;
			map[i][j]=0;
		    }
	        count=0;
		for(i=0;i<10;i++)    
			for(j=0;j<6;j++)
			{
				for(k=0;k<5;k++)
				{
					ptable[j+k][i][count] = true;
					ctable[j+k][i][count] = true;
				}
				count++;
			}
		for(i=0;i<10;i++)       
			for(j=0;j<6;j++)
			{
				for(k=0;k<5;k++)
				{
					ptable[i][j+k][count] = true;
					ctable[i][j+k][count] = true;
				}
				count++;
			}
		for(i=0;i<6;i++)         
			for(j=0;j<6;j++)
			{
				for(k=0;k<5;k++)
				{
					ptable[j+k][i+k][count] = true;
					ctable[j+k][i+k][count] = true;
				}
				count++;
			}
		for(i=0;i<6;i++)        
			for(j=9;j>=4;j--)
			{
				for(k=0;k<5;k++)
				{
					ptable[j-k][i+k][count] = true;
					ctable[j-k][i+k][count] = true;
				}
				count++;
			}
		
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_DOWN)
			return super.onTouchEvent(event);

			
		if(startover==true&&event.getX()>7*girdsize&&event.getX()<9*girdsize&&event.getY()>3*girdsize&&event.getY()<4*girdsize)
		{
				restart();
		}
		if(startover==false&&turn==1&&event.getX()>girdsize/2&&event.getX()<10.5*girdsize&&event.getY()>(height/4-girdsize/2)&&event.getY()<height/4+9.5*girdsize)
		{

			m=Math.round((event.getX()-girdsize)/girdsize);
			n=Math.round((event.getY()-height/4)/girdsize);
			
			if(map[m][n]==0)
				{	
					map[m][n]=1;
					//
					pcount++;
					countpieces++;
                    if((ccount==50)&&(pcount==50))
                    {   tie=true;
                        over=true;

                    }
                    for(i=0;i<192;i++)
                    {
                       if(ptable[m][n][i]&&win[0][i]!=7)
                           win[0][i]++;
                       if(ctable[m][n][i]==true)
                       {
                          ctable[m][n][i]=false;
                          win[1][i]=7;  
                       }
                    }
                    turn=2;//
				}
			if(judge(map,1))
				gameOver=1;
			
			if(ccount==50)
				gameOver=3;
			computerdo();
			if(judge(map,turn))
				gameOver=2;
			turn=1;

		}
		return true;
	}
	protected void computerdo(){
		if(turn==2){
	           for(i=0;i<=9;i++)       
	     	      for(j=0;j<=9;j++)
	     	      {
	     		pgrades[i][j]=0;
	     		if(map[i][j]==0) 
	     		  for(k=0;k<192;k++)
	     		     if(ptable[i][j][k]==true)
	     		     {
	     			switch(win[0][k])
	     			{
	     			case 1:
	     			    pgrades[i][j]+=5;
	     				break;
	     			case 2:
	     				pgrades[i][j]+=50;
	     				break;
	     			case 3:
	     				pgrades[i][j]+=100;
	     				break;
	     			case 4:
	     				pgrades[i][j]+=400;
	     				break;
	     			}
	     		    }
	     	     }
	           //
	           for(i=0;i<=9;i++)     
	      	     for(j=0;j<=9;j++)
	      	     {
	      		cgrades[i][j]=0;
	      		if(map[i][j]==0)
	      		    for(k=0;k<192;k++){
	      			if(ctable[i][j][k])
	      			{
	      			   switch(win[1][k])
	      			   {
	      			     case 1:
	      				cgrades[i][j]+=5;
	      				break;
	      			     case 2:
	      				cgrades[i][j]+=50;
	      				break;
	      			     case 3:
	      				cgrades[i][j]+=100;
	      				break;
	      			     case 4:
	      				cgrades[i][j]+=400;
	      				break;
	      			   }
	      			}
	      		    }
	      	      }

	           if(start)         
	      	 {
	      		if(map[4][4]==0)
	      		{
	      			m = 4;
	      			n = 4;
	      		}
	      		else
	      		{
	      			m = 5;
	      			n = 5;
	      		}
	      		start = false;
	      	 }
	      	 else
	      	 {
	      	    for(i=0;i<10;i++)
	      	       for(j=0;j<10;j++)
	      		  if(map[i][j]==0)
	      		  {
	      		     if(cgrades[i][j]>=cgrade)
	      		     {
	      			cgrade = cgrades[i][j];
	      			mat = i;
	      			nat = j;
	      		     }
	      		     if(pgrades[i][j]>=pgrade)
	      		     {
	      			pgrade = pgrades[i][j];
	      			mde = i;
	      			nde = j;
	      		     }
	      		  }
	      	    if(pgrade>=cgrade&&pgrade>100)  
	      	    {
	      		m = mde;
	      		n = nde;
	      	    }
	      	    else           
	      	    {
	      		m = mat;
	      		n = nat;
	      	    }
	      	 }
	           //

	      	 cgrade = 0;
	      	 pgrade = 0;
	      	 map[m][n] = 2;    
	      	
	               a=m;
	               b=n;
	      	 ccount++;

	      	 if((ccount==50)&&(pcount==50))
	      	 {
	      	     tie = true;
	      	     over = true;
	      	 }
	      	 for(i=0;i<192;i++)
	      	 {
	      	     if(ctable[m][n][i] && win[1][i] != 7)
	      		{
	      	    	 win[1][i]++;
	
	      		}
	      	     
	      	     if(ptable[m][n][i])
	      	     {
	      	 	ptable[m][n][i] = false;
	      	   	win[0][i]=7;
	      	     }
	      	 }
		}
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
