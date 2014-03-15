package com.xhhlz.lzshow.ui;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

/**
 * 这个界面类对应的个人秀的初始界面,
 * 采用EditText的方法有个问题就是界面是可以编辑的，可以用其他方法进行编辑，显然这是不可接受的
 * @author enming xie
 * 
 */
public class PersonalShowEditView extends EditText{
    private Paint mPaint;
	
	public PersonalShowEditView(Context context, AttributeSet attrs) {
		 super(context, attrs);
		// TODO Auto-generated constructor stub
		 
         mPaint = new Paint();
         mPaint.setStyle(Paint.Style.STROKE);
         mPaint.setColor(0xE0E0E0E0);
	}
	
	/**
	 * 制作个人秀的界面的横线
	 */
	@Override
    protected void onDraw(Canvas canvas) {
        int height = canvas.getHeight();//获得屏幕的宽度
        int width = canvas.getWidth();//获得屏幕的高度
		
        Log.d("screenW", width+"");
        Log.d("screenH", height+"");
        
        Paint paint = mPaint;
        int count = 6;
        int x = height/9;
        int step = height/8;
        for (int i = 0; i < count ; i++) {
            canvas.drawLine(10, x, width-10, x, paint);
            x = x+step;
        }

        super.onDraw(canvas);
    }
}
